#!/usr/bin/env python3
"""
Générateur de PDF pur Python (sans dépendances externes).
Produit un document PDF 1.4 officiel lisible par tous les visualiseurs (Acrobat, Chrome, Firefox, macOS Preview).
"""
import os
import re

def create_pdf(md_file, pdf_file):
    with open(md_file, "r", encoding="utf-8") as f:
        md_text = f.read()

    # Nettoyage et translittération des caractères spéciaux pour le standard PDF Type 1
    def clean(s):
        s = s.replace("⇄", "<->").replace("→", "->").replace("•", "*")
        s = s.replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e")
        s = s.replace("à", "a").replace("â", "a").replace("î", "i").replace("ï", "i")
        s = s.replace("ô", "o").replace("ù", "u").replace("û", "u").replace("ç", "c")
        s = s.replace("É", "E").replace("È", "E").replace("À", "A").replace("Â", "A")
        s = s.replace("“", "\"").replace("”", "\"").replace("’", "'").replace("‘", "'")
        s = s.replace("–", "-").replace("—", "-")
        s = re.sub(r'[^\x20-\x7E]', ' ', s)
        s = s.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")
        return s

    lines = md_text.split("\n")

    pages = []
    current_stream = []
    y = 800
    page_h = 842
    page_w = 595
    margin = 40
    bottom = 45
    leading = 13
    page_count = 1

    def close_page():
        nonlocal y, page_count, current_stream
        # Footer
        current_stream.append(f"BT /F3 8 Tf 0.5 0.5 0.5 rg 40 30 Td (Fianarantsoa Taxi-be - Guide Technique et Architecture) Tj ET")
        current_stream.append(f"BT /F3 8 Tf 0.5 0.5 0.5 rg 520 30 Td (Page {page_count}) Tj ET")
        pages.append("\n".join(current_stream))
        current_stream = []
        page_count += 1
        y = 800

    for line in lines:
        stripped = line.strip()
        if not stripped:
            y -= 8
            if y < bottom:
                close_page()
            continue

        if y < bottom + 25:
            close_page()

        if stripped.startswith("# "):
            title = clean(stripped[2:])
            current_stream.append(f"BT /F2 15 Tf 0.0 0.29 0.25 rg {margin} {y} Td ({title}) Tj ET")
            y -= 18
            # Ligne de séparation
            current_stream.append(f"0.7 0.7 0.7 RG 1 w {margin} {y+4} m {page_w - margin} {y+4} l S")
            y -= 8
        elif stripped.startswith("## "):
            sec = clean(stripped[3:])
            y -= 6
            current_stream.append(f"BT /F2 11 Tf 0.1 0.35 0.3 rg {margin} {y} Td ({sec}) Tj ET")
            y -= 15
        elif stripped.startswith("### "):
            subsec = clean(stripped[4:])
            y -= 4
            current_stream.append(f"BT /F2 9.5 Tf 0.2 0.2 0.2 rg {margin} {y} Td ({subsec}) Tj ET")
            y -= 13
        elif stripped.startswith("|"):
            if "---" in stripped:
                continue
            cols = [clean(c.strip()) for c in stripped.split("|")[1:-1]]
            if not cols:
                continue
            col_x = [40, 140, 240, 310]
            for idx, col in enumerate(cols[:4]):
                pos = col_x[idx] if idx < len(col_x) else 40 + idx*80
                col_sub = col[:36] if idx == 3 else col[:16]
                current_stream.append(f"BT /F3 8 Tf 0.1 0.1 0.1 rg {pos} {y} Td ({col_sub}) Tj ET")
            y -= 11
        elif stripped.startswith("- ") or stripped.startswith("* "):
            bullet = clean(stripped[2:])
            words = bullet.split()
            line_buf = ""
            current_stream.append(f"BT /F1 8.5 Tf 0.2 0.2 0.2 rg 45 {y} Td (*) Tj ET")
            for w in words:
                if len(line_buf) + len(w) + 1 > 92:
                    current_stream.append(f"BT /F1 8.5 Tf 0.2 0.2 0.2 rg 55 {y} Td ({line_buf}) Tj ET")
                    y -= leading
                    if y < bottom + 15:
                        close_page()
                    line_buf = w
                else:
                    line_buf = (line_buf + " " + w).strip()
            if line_buf:
                current_stream.append(f"BT /F1 8.5 Tf 0.2 0.2 0.2 rg 55 {y} Td ({line_buf}) Tj ET")
            y -= leading
        else:
            text_p = clean(stripped)
            words = text_p.split()
            line_buf = ""
            for w in words:
                if len(line_buf) + len(w) + 1 > 95:
                    current_stream.append(f"BT /F1 8.5 Tf 0.1 0.1 0.1 rg {margin} {y} Td ({line_buf}) Tj ET")
                    y -= leading
                    if y < bottom + 15:
                        close_page()
                    line_buf = w
                else:
                    line_buf = (line_buf + " " + w).strip()
            if line_buf:
                current_stream.append(f"BT /F1 8.5 Tf 0.1 0.1 0.1 rg {margin} {y} Td ({line_buf}) Tj ET")
            y -= leading

    if current_stream:
        current_stream.append(f"BT /F3 8 Tf 0.5 0.5 0.5 rg 40 30 Td (Fianarantsoa Taxi-be - Guide Technique et Architecture) Tj ET")
        current_stream.append(f"BT /F3 8 Tf 0.5 0.5 0.5 rg 520 30 Td (Page {page_count}) Tj ET")
        pages.append("\n".join(current_stream))

    # Construction du document PDF
    objects = []
    
    # 1: Catalog
    # 2: Pages root
    # 3: Font F1 (Helvetica)
    # 4: Font F2 (Helvetica-Bold)
    # 5: Font F3 (Courier)
    # Pour chaque page i:
    #   obj_page: Page
    #   obj_stream: Content stream
    
    total_pages = len(pages)
    page_obj_ids = []
    
    # Réservons les IDs
    # 1: Catalog, 2: Outlines, 3: Pages, 4: F1, 5: F2, 6: F3
    current_obj_id = 7
    page_entries = [] # (page_id, content_id, content_stream)
    for p in pages:
        p_id = current_obj_id
        c_id = current_obj_id + 1
        page_entries.append((p_id, c_id, p))
        page_obj_ids.append(p_id)
        current_obj_id += 2

    # Objets fixes
    objects.append((1, "<</Type /Catalog /Pages 3 0 R>>"))
    objects.append((2, "<</Type /Outlines /Count 0>>"))
    
    kids_str = " ".join([f"{pid} 0 R" for pid in page_obj_ids])
    objects.append((3, f"<</Type /Pages /Count {total_pages} /Kids [{kids_str}]>>"))
    
    objects.append((4, "<</Type /Font /Subtype /Type1 /BaseFont /Helvetica>>"))
    objects.append((5, "<</Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold>>"))
    objects.append((6, "<</Type /Font /Subtype /Type1 /BaseFont /Courier>>"))

    for p_id, c_id, stream_content in page_entries:
        page_obj = f"""<</Type /Page /Parent 3 0 R /MediaBox [0 0 595 842] /Resources <<
/Font << /F1 4 0 R /F2 5 0 R /F3 6 0 R >>
>> /Contents {c_id} 0 R>>"""
        objects.append((p_id, page_obj))
        
        stream_bytes = stream_content.encode("latin-1", "replace")
        stream_obj = f"""<</Length {len(stream_bytes)}>>
stream
{stream_content}
endstream"""
        objects.append((c_id, stream_obj))

    # Écriture du fichier avec table xref
    with open(pdf_file, "wb") as f:
        f.write(b"%PDF-1.4\n")
        offsets = {}
        for obj_id, obj_body in objects:
            offsets[obj_id] = f.tell()
            f.write(f"{obj_id} 0 obj\n{obj_body}\nendobj\n".encode("latin-1"))

        xref_offset = f.tell()
        f.write(f"xref\n0 {len(objects) + 1}\n".encode("latin-1"))
        f.write(b"0000000000 65535 f \n")
        for obj_id in range(1, len(objects) + 1):
            f.write(f"{offsets[obj_id]:010d} 00000 n \n".encode("latin-1"))

        trailer = f"""trailer
<</Size {len(objects) + 1} /Root 1 0 R>>
startxref
{xref_offset}
%%EOF"""
        f.write(trailer.encode("latin-1"))

    print(f"Génération réussie : {pdf_file} ({total_pages} pages, {os.path.getsize(pdf_file)} octets)")

if __name__ == "__main__":
    create_pdf("GUIDE_CODE_EXPLICATION.md", "GUIDE_CODE_EXPLICATION.pdf")

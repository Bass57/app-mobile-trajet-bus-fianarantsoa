package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.datasource.FianarBusData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Bus Fianarantsoa", appName)
  }

  @Test
  fun `verify bus lines and university stop are present`() {
    val lines = FianarBusData.LINES
    assertTrue(lines.size >= 10)
    val line38 = lines.find { it.id == "L38" }
    assertNotNull(line38)
    assertEquals("Ligne 38", line38!!.number)
    assertTrue(line38.stops.contains("Andrainjato Université"))

    val line40 = lines.find { it.id == "L40" }
    assertNotNull(line40)
    assertEquals("Ligne 40", line40!!.number)

    val line30 = lines.find { it.id == "L30" }
    assertNotNull(line30)
    assertEquals("Ligne 30", line30!!.number)
  }

  @Test
  fun `verify bus fares standard 600 Ar and exceptions for line 38 and collective barriere CB at 500 Ar`() {
    val lines = FianarBusData.LINES

    // Ligne 38 : Exception 500 Ar
    val line38 = lines.find { it.id == "L38" }
    assertNotNull(line38)
    assertEquals(500, line38!!.fareAriary)

    // Bus Collectif Barrière : Exception 500 Ar avec id CB
    val lineCollectif = lines.find { it.id == "CB" }
    assertNotNull(lineCollectif)
    assertEquals(500, lineCollectif!!.fareAriary)
    assertEquals("CB", lineCollectif.id)
    assertTrue(lineCollectif.stops.contains("Barrière d'Andrainjato"))
    assertTrue(lineCollectif.stops.contains("Andrainjato Université"))

    // Lignes standard : 600 Ar
    val line40 = lines.find { it.id == "L40" }
    assertNotNull(line40)
    assertEquals(600, line40!!.fareAriary)

    val line30 = lines.find { it.id == "L30" }
    assertNotNull(line30)
    assertEquals(600, line30!!.fareAriary)

    val line48 = lines.find { it.id == "L48" }
    assertNotNull(line48)
    assertEquals(600, line48!!.fareAriary)
  }

  @Test
  fun `verify bus lines 32 and 33 exist and have standard fare 600 Ar`() {
    val lines = FianarBusData.LINES

    // Ligne 32 : Ambatovory ⇄ Soanierana
    val line32 = lines.find { it.id == "L32" }
    assertNotNull(line32)
    assertEquals("Ligne 32", line32!!.number)
    assertEquals("Ambatovory", line32.departure)
    assertEquals("Soanierana", line32.terminus)
    assertEquals(600, line32.fareAriary)
    assertTrue(line32.stops.contains("Ambatovory"))
    assertTrue(line32.stops.contains("Soanierana"))

    // Ligne 33 : Tsianolondroa ⇄ Ankofafa
    val line33 = lines.find { it.id == "L33" }
    assertNotNull(line33)
    assertEquals("Ligne 33", line33!!.number)
    assertEquals("Tsianolondroa", line33.departure)
    assertEquals("Ankofafa", line33.terminus)
    assertEquals(600, line33.fareAriary)
    assertTrue(line33.stops.contains("Tsianolondroa"))
    assertTrue(line33.stops.contains("Ankofafa"))
  }
}


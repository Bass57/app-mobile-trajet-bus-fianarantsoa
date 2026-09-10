package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BusDao {

    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteItem): Long

    @Query("DELETE FROM favorites WHERE itemId = :itemId AND type = :type")
    suspend fun deleteFavoriteByItemId(itemId: String, type: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId AND type = :type)")
    fun isFavorite(itemId: String, type: String): Flow<Boolean>

    // Trip notes
    @Query("SELECT * FROM trip_notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<TripNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: TripNote): Long

    @Query("DELETE FROM trip_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)
}

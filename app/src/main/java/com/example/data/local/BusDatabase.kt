package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteItem::class, TripNote::class],
    version = 1,
    exportSchema = false
)
abstract class BusDatabase : RoomDatabase() {

    abstract fun busDao(): BusDao

    companion object {
        @Volatile
        private var INSTANCE: BusDatabase? = null

        fun getDatabase(context: Context): BusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BusDatabase::class.java,
                    "fianar_bus_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

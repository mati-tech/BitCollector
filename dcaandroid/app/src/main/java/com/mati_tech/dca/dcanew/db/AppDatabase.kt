package com.mati_tech.dca.dcanew.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PositionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun positionDao(): PositionDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trading_database"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}
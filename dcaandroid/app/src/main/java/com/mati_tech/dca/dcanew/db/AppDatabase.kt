package com.mati_tech.dca.dcanew.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PositionEntity::class, PositionDetailEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun positionDao(): PositionDao
    abstract fun positionDetailDao(): PositionDetailDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        // Migration to add the new table
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new table to the database
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `position_details` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `positionId` INTEGER NOT NULL, 
                        `tradeType` TEXT NOT NULL, 
                        `tradeAmount` REAL NOT NULL, 
                        `tradePrice` REAL NOT NULL, 
                        `totalCost` REAL NOT NULL, 
                        `timestamp` INTEGER NOT NULL, 
                        FOREIGN KEY(`positionId`) REFERENCES `position`(`id`) ON DELETE CASCADE
                    )
                    """
                )
            }
        }
        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trading_database"
                ).addMigrations(MIGRATION_4_5)   // 💥 This resets the database when the schema changes
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }

}
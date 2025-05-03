package com.mati_tech.dca.dcanew.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mati_tech.dca.dcanew.dao.PositionDao
import com.mati_tech.dca.dcanew.dao.PositionDetailDao
import com.mati_tech.dca.dcanew.dao.PositionDetailEntity
import com.mati_tech.dca.dcanew.dao.PositionEntity

@Database(entities = [PositionEntity::class, PositionDetailEntity::class], version = 7)
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
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `position_details` ADD COLUMN `availableUsd` REAL DEFAULT 0.0 NOT NULL")
            }
        }
        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `positions` ADD COLUMN `hasDetails` INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trading_database"
                ).addMigrations(MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)   // 💥 This resets the database when the schema changes
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }

}
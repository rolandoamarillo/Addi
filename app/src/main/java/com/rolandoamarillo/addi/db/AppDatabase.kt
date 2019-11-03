package com.rolandoamarillo.addi.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rolandoamarillo.addi.db.dao.ProspectDao
import com.rolandoamarillo.addi.model.Prospect

/**
 * Application Room database
 */
@Database(entities = [Prospect::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * DAO for all transactions with {@link Prospect}
     */
    abstract fun prospectDao(): ProspectDao

}
package com.rolandoamarillo.addi.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rolandoamarillo.addi.model.Prospect

@Dao
interface ProspectDao {

    @Query("SELECT * FROM prospect WHERE contact = 0")
    fun getProspects(): LiveData<List<Prospect>>

    @Query("SELECT * FROM prospect WHERE contact = 1")
    fun getContacts(): LiveData<List<Prospect>>

    @Query("SELECT * FROM prospect WHERE doc_type = :documentType AND doc_number = :documentNumber")
    fun getFromDocument(documentType: String, documentNumber: String): Prospect?

    @Insert
    fun insert(prospect: Prospect)

    @Update
    fun update(prospect: Prospect)
}
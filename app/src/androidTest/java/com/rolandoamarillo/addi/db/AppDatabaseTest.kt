package com.rolandoamarillo.addi.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rolandoamarillo.addi.TestUtil
import com.rolandoamarillo.addi.db.dao.ProspectDao
import com.rolandoamarillo.addi.model.Prospect
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var prospectDao: ProspectDao
    private lateinit var db: AppDatabase

    @Mock
    lateinit var prospectObserver: Observer<List<Prospect>>

    @Mock
    lateinit var contactObserver: Observer<List<Prospect>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private fun cleanObservers() {
        prospectDao.getProspects().removeObserver(prospectObserver)
        prospectDao.getContacts().removeObserver(contactObserver)
    }

    @Before
    fun createDb() {
        MockitoAnnotations.initMocks(this)
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        prospectDao = db.prospectDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun givenAProspectWhenInsertThenShouldAddToDb() {
        // given
        val docType = "docType"
        val docNumber = "docNumber"
        val prospect: Prospect = TestUtil.createProspect(docType, docNumber)
        assertEquals(prospectDao.getProspects().value, null)
        assertEquals(prospectDao.getContacts().value, null)

        //when
        prospectDao.insert(prospect)

        // then
        val fromDocument = prospectDao.getFromDocument(docType, docNumber)
        assertEquals(fromDocument!!.docType, docType)
        assertEquals(fromDocument.docNumber, docNumber)

        prospectDao.getProspects().observeForever(prospectObserver)
        prospectDao.getContacts().observeForever(contactObserver)

        verify(prospectObserver).onChanged(Collections.singletonList(prospect))
        assertEquals(prospectDao.getContacts().value, null)

        cleanObservers()
    }

    @Test
    fun givenAProspectWhenUpdateThenShouldUpdateDb() {
        // given
        val docType = "docType"
        val docNumber = "docNumber"
        val prospect: Prospect = TestUtil.createProspect(docType, docNumber)

        assertEquals(prospectDao.getProspects().value, null)
        assertEquals(prospectDao.getContacts().value, null)

        //when
        prospectDao.insert(prospect)
        prospect.contact = true
        prospectDao.update(prospect)

        // then
        val fromDocument = prospectDao.getFromDocument(docType, docNumber)
        assertEquals(fromDocument!!.docType, docType)
        assertEquals(fromDocument.docNumber, docNumber)
        assertTrue(fromDocument.contact)

        prospectDao.getProspects().observeForever(prospectObserver)
        prospectDao.getContacts().observeForever(contactObserver)

        verify(contactObserver).onChanged(Collections.singletonList(prospect))
        verify(prospectObserver).onChanged(Collections.emptyList())

        cleanObservers()
    }

}
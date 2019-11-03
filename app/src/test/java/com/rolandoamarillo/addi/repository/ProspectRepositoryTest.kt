package com.rolandoamarillo.addi.repository

import androidx.lifecycle.MutableLiveData
import com.rolandoamarillo.addi.TestUtil
import com.rolandoamarillo.addi.db.dao.ProspectDao
import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.network.ProspectApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ProspectRepositoryTest {

    @Mock
    lateinit var prospectDao: ProspectDao

    @Mock
    lateinit var prospectApi: ProspectApi

    @InjectMocks
    lateinit var repository: ProspectRepository

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getProspectsTest() {
        val prospectLiveData = MutableLiveData<List<Prospect>>()
        `when`(prospectDao.getProspects()).thenReturn(prospectLiveData)

        assertEquals(repository.prospects, prospectLiveData)
        verify(prospectDao).getProspects()
    }

    @Test
    fun getContactsTest() {
        val prospectLiveData = MutableLiveData<List<Prospect>>()
        `when`(prospectDao.getContacts()).thenReturn(prospectLiveData)

        assertEquals(repository.contacts, prospectLiveData)
        verify(prospectDao).getContacts()
    }

    @Test
    fun getPublicInformationTest() {
        val docType = "docType"
        val docNumber = "docNumber"
        val mockResponse = TestUtil.createPublicInformationResponse(docType, docNumber)
        val call = retrofit2.mock.Calls.response(mockResponse)
        `when`(prospectApi.getPublicInformation(docType, docNumber)).thenReturn(call)

        val publicInformationCall = repository.getPublicInformation(docType, docNumber)

        assertEquals(publicInformationCall, call)
        verify(prospectApi).getPublicInformation(docType, docNumber)
    }

    @Test
    fun getJusticeInformationTest() {
        val docType = "docType"
        val docNumber = "docNumber"
        val mockResponse = TestUtil.createJusticeInformationResponse(docType, docNumber, 0)
        val call = retrofit2.mock.Calls.response(mockResponse)
        `when`(prospectApi.getJusticeInformation(docType, docNumber)).thenReturn(call)

        val justiceInformationCall = repository.getJusticeInformation(docType, docNumber)

        assertEquals(justiceInformationCall, call)
        verify(prospectApi).getJusticeInformation(docType, docNumber)
    }

    @Test
    fun getRatingTest() {
        val docType = "docType"
        val docNumber = "docNumber"
        val mockResponse = TestUtil.createRatingInformationResponse(docType, docNumber, 60)
        val call = retrofit2.mock.Calls.response(mockResponse)
        `when`(prospectApi.getRating(docType, docNumber)).thenReturn(call)

        val ratingCall = repository.getRating(docType, docNumber)

        assertEquals(ratingCall, call)
        verify(prospectApi).getRating(docType, docNumber)
    }

    @Test
    fun getProspectTest() {
        val docType = "docType"
        val docNumber = "docNumber"
        val prospect = TestUtil.createProspect(docType, docNumber)

        `when`(prospectDao.getFromDocument(docType, docNumber)).thenReturn(prospect)

        val result = repository.getProspect(docType, docNumber)

        verify(prospectDao).getFromDocument(docType, docNumber)
        assertEquals(prospect, result)
    }

    @Test
    fun addProspectTest() {
        val docType = "docType"
        val docNumber = "docNumber"
        val prospect = TestUtil.createProspect(docType, docNumber)

        repository.addProspect(prospect)

        verify(prospectDao).insert(prospect)
    }

    @Test
    fun addContactTest() {
        val docType = "docType"
        val docNumber = "docNumber"
        val prospect = TestUtil.createProspect(docType, docNumber)

        repository.addContact(prospect)

        assertTrue(prospect.contact)
        verify(prospectDao).update(prospect)
    }
}
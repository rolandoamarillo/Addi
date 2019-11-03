package com.rolandoamarillo.addi.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.model.ProspectValidations
import com.rolandoamarillo.addi.repository.ProspectDataSource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class AddProspectViewModelTest {

    @Mock
    lateinit var prospectDataSource: ProspectDataSource

    @InjectMocks
    lateinit var addProspectViewModel: AddProspectViewModel

    @Mock
    lateinit var addedProspectObserver: Observer<Prospect>

    @Mock
    lateinit var prospectValidationsObserver: Observer<Set<ProspectValidations>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun givenValidInputsWhenAddingProspectShouldAddProspect() {
        //given
        val docType = "docType"
        val docNumber = "docNumber"
        val docExpedition = "docExpedition"
        val name = "name"
        val lastname = "lastname"
        val phone = "2343451223"
        val email = "email@email.com"
        val signal = CountDownLatch(1)

        //when
        `when`(prospectDataSource.getProspect(docType, docNumber)).thenReturn(null)

        addProspectViewModel.addedProspect.observeForever(addedProspectObserver)
        addProspectViewModel.prospectValidations.observeForever(prospectValidationsObserver)

        addProspectViewModel.addProspect(
            docType,
            docNumber,
            docExpedition,
            name,
            lastname,
            phone,
            email
        )

        signal.await(5, TimeUnit.SECONDS)

        //should
        verify(prospectValidationsObserver).onChanged(setOf(ProspectValidations.OK))
        verify(addedProspectObserver).onChanged(any(Prospect::class.java))
    }

    @Test
    fun givenInvalidInputsWhenAddingProspectShouldNotAddProspect() {
        //given
        val docType = ""
        val docNumber = ""
        val docExpedition = ""
        val name = ""
        val lastname = ""
        val phone = "asd"
        val email = "123"
        val signal = CountDownLatch(1)

        //when
        `when`(prospectDataSource.getProspect(docType, docNumber)).thenReturn(null)

        addProspectViewModel.addedProspect.observeForever(addedProspectObserver)
        addProspectViewModel.prospectValidations.observeForever(prospectValidationsObserver)

        addProspectViewModel.addProspect(
            docType,
            docNumber,
            docExpedition,
            name,
            lastname,
            phone,
            email
        )

        signal.await(5, TimeUnit.SECONDS)

        //should
        verify(addedProspectObserver, never()).onChanged(any(Prospect::class.java))
        verify(prospectValidationsObserver).onChanged(
            setOf(
                ProspectValidations.EMPTY_DOC_TYPE,
                ProspectValidations.EMPTY_DOC_NUMBER,
                ProspectValidations.EMPTY_DOC_EXPIRATION_DATE,
                ProspectValidations.OK,
                ProspectValidations.EMPTY_NAME,
                ProspectValidations.EMPTY_LASTNAME,
                ProspectValidations.INVALID_PHONE,
                ProspectValidations.INVALID_EMAIL
            )
        )
    }
}
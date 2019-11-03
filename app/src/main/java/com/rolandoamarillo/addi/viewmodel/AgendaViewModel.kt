package com.rolandoamarillo.addi.viewmodel

import androidx.lifecycle.ViewModel
import com.rolandoamarillo.addi.repository.ProspectDataSource

class AgendaViewModel(private val prospectDataSource: ProspectDataSource) : ViewModel() {

    val contacts get() = prospectDataSource.contacts

}

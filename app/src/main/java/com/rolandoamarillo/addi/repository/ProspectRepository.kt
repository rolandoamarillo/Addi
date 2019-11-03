package com.rolandoamarillo.addi.repository

import com.rolandoamarillo.addi.db.dao.ProspectDao
import com.rolandoamarillo.addi.model.Prospect
import com.rolandoamarillo.addi.network.ProspectApi

/**
 * Repository for transactions related with {@link Prospect}
 */
class ProspectRepository(

    /**
     * DAO instance
     */
    private val prospectDao: ProspectDao,

    /**
     * API instance
     */
    private val prospectApi: ProspectApi
) : ProspectDataSource {

    override val prospects get() = prospectDao.getProspects()

    override val contacts get() = prospectDao.getContacts()

    override fun getPublicInformation(documentType: String, documentNumber: String) =
        prospectApi.getPublicInformation(documentType, documentNumber)

    override fun getJusticeInformation(documentType: String, documentNumber: String) =
        prospectApi.getJusticeInformation(documentType, documentNumber)

    override fun getRating(documentType: String, documentNumber: String) =
        prospectApi.getRating(documentType, documentNumber)

    override fun getProspect(documentType: String, documentNumber: String): Prospect? =
        prospectDao.getFromDocument(documentType, documentNumber)

    override fun addProspect(prospect: Prospect) {
        prospectDao.insert(prospect)
    }

    override fun addContact(prospect: Prospect) {
        prospect.contact = true
        prospectDao.update(prospect)
    }
}
package com.seancoyle.spacex.framework.datasource.data.company

import android.app.Application
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class CompanyInfoDataFactory
@Inject
constructor(
    private val application: Application,
    private val infoFactory: CompanyInfoFactory
) {

    fun produceCompanyInfo(): CompanyInfoDomainEntity {
        return Gson()
            .fromJson(
                getDataFromFile("company_info.json"),
                object : TypeToken<CompanyInfoDomainEntity>() {}.type
            )
    }

    fun getDataFromFile(fileName: String): String? {
        return readJSONFromAsset(fileName)
    }

    private fun readJSONFromAsset(fileName: String): String? {
        val json: String? = try {
            val inputStream: InputStream = (application.assets as AssetManager).open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun createCompanyInfo(
        id: String,
        employees: String,
        founded: Int,
        founder: String,
        launchSites: Int,
        name: String,
        valuation: String
    ) = infoFactory.createCompanyInfo(
        id = id,
        employees = employees,
        founded = founded,
        founder = founder,
        launchSites = launchSites,
        name = name,
        valuation = valuation
    )
}














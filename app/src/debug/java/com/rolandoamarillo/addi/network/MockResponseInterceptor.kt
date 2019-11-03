package com.rolandoamarillo.addi.network

import android.content.Context
import com.google.gson.Gson
import com.rolandoamarillo.addi.network.response.JusticeInformationResponse
import com.rolandoamarillo.addi.network.response.ProspectRatingResponse
import com.rolandoamarillo.addi.network.response.PublicInformationResponse
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URLConnection
import java.util.*

/**
 * OkHttp3 interceptor which provides a mock response from local resource file.
 */
class MockResponseInterceptor : Interceptor {

    private var context: Context? = null
    private var scenario: String? = null
    private var gson: Gson? = null
    private var mockMode = MockMode.FIXED

    constructor(context: Context, scenario: String, gson: Gson) {
        this.context = context.applicationContext
        this.scenario = scenario
        this.mockMode = MockMode.FIXED
        this.gson = gson
    }

    constructor(context: Context, gson: Gson) {
        this.context = context.applicationContext
        this.gson = gson
    }

    constructor(context: Context, gson: Gson, mockMode: MockMode) {
        this.context = context.applicationContext
        this.gson = gson
        this.mockMode = mockMode
    }

    fun setMode(mode: MockMode) {
        this.mockMode = mode
    }

    fun setScenario(scenario: String) {
        this.scenario = scenario
        this.mockMode = MockMode.FIXED
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (mockMode == MockMode.DYNAMIC) interceptDynamic(chain) else interceptFixed(chain)
    }

    private fun interceptDynamic(chain: Interceptor.Chain): Response {

        val url = chain.request().url()
        val path = url.url().path.removePrefix("/")

        val documentType = url.queryParameter("document_type")
        val documentNumber = url.queryParameter("document")

        val responseObject: Any = when (path) {
            Endpoints.PUBLIC_INFORMATION -> PublicInformationResponse(
                documentType!!,
                documentNumber!!
            )
            Endpoints.JUSTICE_INFORMATION -> JusticeInformationResponse(
                documentType!!,
                documentNumber!!,
                0
            )
            Endpoints.RATING_INFORMATION -> ProspectRatingResponse(
                documentType!!,
                documentNumber!!,
                59
            )
            else -> throw Exception("Couldn't find mapping for $path")
        }

        val response = gson!!.toJson(responseObject)

        val mimeType = "application/json"

        // Build and return mock response.
        return Response.Builder()
            .addHeader("content-type", mimeType)
            .body(ResponseBody.create(MediaType.parse(mimeType), response.toByteArray()))
            .code(200)
            .message("Mock response from $path")
            .protocol(Protocol.HTTP_2)
            .request(chain.request())
            .build()
    }

    private fun interceptFixed(chain: Interceptor.Chain): Response {
        // Get resource ID for mock response file.
        var fileName = getFilename(chain.request(), scenario)
        var resourceId = getResourceId(fileName)
        if (resourceId == 0) {
            // Attempt to fallback to default mock response file.
            fileName = getFilename(chain.request(), null)
            resourceId = getResourceId(fileName)
            if (resourceId == 0) {
                throw IOException("Could not find res/raw/$fileName")
            }
        }

        // Get input stream and mime type for mock response file.
        val inputStream = context!!.resources.openRawResource(resourceId)
        var mimeType: String? = URLConnection.guessContentTypeFromStream(inputStream)
        if (mimeType == null) {
            mimeType = "application/json"
        }

        // Build and return mock response.
        return Response.Builder()
            .addHeader("content-type", mimeType)
            .body(ResponseBody.create(MediaType.parse(mimeType), toByteArray(inputStream)))
            .code(200)
            .message("Mock response from res/raw/$fileName")
            .protocol(Protocol.HTTP_2)
            .request(chain.request())
            .build()
    }

    private fun getFilename(request: Request, scenario: String?): String {
        val requestedMethod = request.method()
        val prefix = if (scenario == null) "" else scenario + "_"
        var filename = prefix + requestedMethod + request.url().url().path
        filename = filename.replace("/", "_").replace("-", "_").toLowerCase(Locale.US)
        return filename
    }

    private fun getResourceId(filename: String): Int {
        return context!!.resources.getIdentifier(filename, "raw", context!!.packageName)
    }

    companion object {

        private const val BUFFER_SIZE = 1024 * 4

        @Throws(IOException::class)
        private fun toByteArray(inputStream: InputStream): ByteArray {
            val output = ByteArrayOutputStream()
            output.use {
                val b = ByteArray(BUFFER_SIZE)
                var n = inputStream.read(b)
                while (n != -1) {
                    it.write(b, 0, n)
                    n = inputStream.read(b)
                }
                return it.toByteArray()
            }
        }
    }
}
package com.gateopener

import androidx.car.app.CarContext
import androidx.car.app.CarToast
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.GridItem
import androidx.car.app.model.GridTemplate
import androidx.car.app.model.ItemList
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class GateOpenerScreen(carContext: CarContext) : Screen(carContext) {
    private var requestInProgress = false

    override fun onGetTemplate(): Template {
        val listBuilder = ItemList.Builder()

        val gateImage = CarIcon
            .Builder(IconCompat.createWithResource(carContext, R.drawable.gate))
            .build()

        val gateItem = GridItem.Builder()
            .setTitle("Gate")

        if (requestInProgress) {
            gateItem.setLoading(true)
        }
        else {
            gateItem
                .setImage(gateImage)
                .setOnClickListener {
                    openGate()
                    invalidate()
                }
        }

        listBuilder.addItem(
            gateItem.build()
        )

        return GridTemplate.Builder()
            .setTitle("Devices")
            .setHeaderAction(Action.APP_ICON)
            .setSingleList(listBuilder.build())
            .build()
    }

    private fun openGate(){
        val serviceAddress = BuildConfig.SERVICE_ADDRESS
        val endpoint = BuildConfig.SERVICE_ENDPOINT
        val token = BuildConfig.API_KEY

        val mediaTypeJson = "application/json; charset=utf-8".toMediaType()
        val requestBody = """
            {
            	"entity_id" : "cover.brama"
            }
        """.trimIndent()

        val request = Request
            .Builder()
            .url(serviceAddress + endpoint)
            .header("User-Agent", "OkHttp GetOpenerScreen.kt")
            .addHeader("Content-Type", mediaTypeJson.toString())
            .addHeader("Authorization", "Bearer $token")
            .post(requestBody.toRequestBody(mediaTypeJson))
            .build()

        val client = OkHttpClient()
        requestInProgress = true
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.code == 200){
                    CarToast
                        .makeText(carContext, "Gate 1 opened", CarToast.LENGTH_SHORT)
                        .show()
                }
                else{
                    CarToast
                        .makeText(carContext, "Gate 1 open failed(HTTP response code: ${response.code})", CarToast.LENGTH_SHORT)
                        .show()
                }
                requestInProgress = false
                invalidate()
            }

            override fun onFailure(call: Call, e: IOException) {
                CarToast
                    .makeText(carContext, "Gate 1 open failed(Unknown error)", CarToast.LENGTH_SHORT)
                    .show()
                requestInProgress = false
                invalidate()
            }
        })
    }
}
package com.origo.ultimatehotspot.manager.hotspot

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import com.android.dx.stock.ProxyBuilder
import com.origo.ultimatehotspot.HOTSPOT_DEFAULT_SSID
import com.origo.ultimatehotspot.HOTSPOT_DEFAULT_PASSWORD
import timber.log.Timber

/**
 * Enable the wifi hotspot on android oreo.
 * Condition:
 * - works starting of android version oreo and higher
 * Warning:
 * The ssid and password value here cannot be setup by the app due to security restriction,
 * you have to set the value in the settings yourself. The app just display the value.
 * Pros:
 * - uses the android framework hotspot, making this solution more reliable
 * Cons:
 * - the ssid/password cannot be set by the app, as it is not possible access to the API anymore starting of Android N, meaning you need to know them beforehand or make them self-deterministic
 */
@RequiresApi(Build.VERSION_CODES.O)
class OreoWifiManager(private val context: Context) : HotspotManager() {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * This enables tethering using the ssid/password defined in Settings App>Hotspot & tethering
     * Does not require app to have system/privileged access
     * Credit: Vishal Sharma - https://stackoverflow.com/a/52219887
     */
    override fun start() {
        val outputDir = context.codeCacheDir
        val proxy: Any?

        try {
            proxy = ProxyBuilder.forClass(onStartTetheringCallbackClass())
                .dexCache(outputDir).handler { createdProxy, method, args ->
                    when (method.name) {
                        "onTetheringStarted" -> hotspotLiveData.postValue(HotspotEvent(HotspotEvent.Type.START_SUCCESS).apply {
                            ssid = HOTSPOT_DEFAULT_SSID
                            password = HOTSPOT_DEFAULT_PASSWORD
                        })
                        "onTetheringFailed" -> hotspotLiveData.postValue(HotspotEvent(HotspotEvent.Type.START_ERROR))
                        else -> ProxyBuilder.callSuper(createdProxy, method, *args)
                    }
                    null
                }.build()

            try {
                val method = connectivityManager.javaClass.getDeclaredMethod(
                    "startTethering",
                    Int::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType,
                    onStartTetheringCallbackClass(),
                    Handler::class.java
                )
                method.invoke(
                    connectivityManager,
                    ConnectivityManager.TYPE_MOBILE,
                    false,
                    proxy,
                    null
                )
                Timber.d("startTethering invoked")
            } catch (e: Exception) {
                Timber.e("Error in enableTethering")
                e.printStackTrace()
                hotspotLiveData.postValue(
                    HotspotEvent(
                        HotspotEvent.Type.START_ERROR,
                        "Error in enableTethering"
                    )
                )
            }
        } catch (e: Exception) {
            Timber.e("Error in enableTethering ProxyBuilder")
            e.printStackTrace()
            hotspotLiveData.postValue(
                HotspotEvent(
                    HotspotEvent.Type.START_ERROR,
                    "Error in enableTethering ProxyBuilder"
                )
            )
        }
    }

    override fun stop() {
        try {
            val method = connectivityManager.javaClass.getDeclaredMethod(
                "stopTethering",
                Int::class.javaPrimitiveType
            )
            method.invoke(connectivityManager, ConnectivityManager.TYPE_MOBILE)
            Timber.d("stopTethering invoked")
            hotspotLiveData.postValue(HotspotEvent(HotspotEvent.Type.STOP_SUCCESS))
        } catch (e: Exception) {
            Timber.e("stopTethering error: $e")
            e.printStackTrace()
            hotspotLiveData.postValue(
                HotspotEvent(
                    HotspotEvent.Type.STOP_ERROR,
                    "Error ${e.message}"
                )
            )
        }
    }

    @SuppressLint("PrivateApi")
    private fun onStartTetheringCallbackClass(): Class<*>? {
        try {
            return Class.forName("android.net.ConnectivityManager\$OnStartTetheringCallback")
        } catch (e: ClassNotFoundException) {
            Timber.e("onStartTetheringCallbackClass error: %s", e.toString())
            e.printStackTrace()
        }

        return null
    }
}

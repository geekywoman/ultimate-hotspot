package com.origo.ultimatehotspot.manager.hotspot

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import timber.log.Timber
import com.origo.ultimatehotspot.HOTSPOT_DEFAULT_SSID
import com.origo.ultimatehotspot.HOTSPOT_DEFAULT_PASSWORD

/**
 * Enable android framework hotspot. Works only for marshmallow version and below
 * Constraints:
 * -  works under android version marshmallow and lower
 * Pros:
 * - uses the android framework hotspot, making this solution more reliable
 */
@Suppress("DEPRECATION")
class MarshmallowWifiManager(context: Context) : HotspotManager() {

    private val wifiManager: WifiManager? =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun start() {
        try {
            if (wifiManager == null) {
                hotspotLiveData.value = HotspotEvent(
                    HotspotEvent.Type.START_ERROR, "wifiManager not initialised"
                )
                return
            }

            //turn off wifi to enable tethering
            if (wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = false
            }
            val method = wifiManager.javaClass.getMethod(
                TETHERING_ENABLE_METHOD,
                WifiConfiguration::class.java,
                Boolean::class.javaPrimitiveType
            )
            val wifiConfiguration = getWifiConfiguration()
            val result = method.invoke(wifiManager, wifiConfiguration, true) as Boolean
            if (result) {
                Timber.d("wifi started: success!")
                hotspotLiveData.value = HotspotEvent(
                    HotspotEvent.Type.START_SUCCESS
                ).apply {
                    ssid = wifiConfiguration.SSID
                    password = wifiConfiguration.BSSID
                }
            } else {
                hotspotLiveData.value = HotspotEvent(
                    HotspotEvent.Type.START_ERROR, "Wifi was not started"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            hotspotLiveData.value = HotspotEvent(
                HotspotEvent.Type.START_ERROR, "Something went wrong with the Wifi Manager"
            )
        }
    }

    override fun stop() {
        try {
            if (wifiManager == null) {
                hotspotLiveData.value = HotspotEvent(
                    HotspotEvent.Type.START_ERROR, "wifiManager not initialised"
                )
                return
            }

            val method = wifiManager.javaClass.getMethod(
                TETHERING_ENABLE_METHOD,
                WifiConfiguration::class.java,
                Boolean::class.javaPrimitiveType
            )
            val wifiConfiguration = getWifiConfiguration()
            val result = method.invoke(wifiManager, wifiConfiguration, false) as Boolean
            if (result) {
                Timber.d("wifi stopped: success!")
                hotspotLiveData.value = HotspotEvent(HotspotEvent.Type.STOP_SUCCESS)
            } else {
                hotspotLiveData.value =
                    HotspotEvent(HotspotEvent.Type.STOP_ERROR, "Wifi was not stopped")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            hotspotLiveData.value =
                HotspotEvent(HotspotEvent.Type.STOP_ERROR, "Wifi was not stopped ${e.message}")
        }
    }

    private fun getWifiConfiguration(): WifiConfiguration {
        val configuration = WifiConfiguration()
        configuration.SSID = HOTSPOT_DEFAULT_SSID
        configuration.BSSID = HOTSPOT_DEFAULT_PASSWORD
        configuration.status = WifiConfiguration.Status.ENABLED
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
        configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
        configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
        configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN)

        return configuration
    }


    @SuppressLint("PrivateApi")
    //todo turn the switch on if the wifi hotspot is already active?
    fun isActive(): Boolean {
        return try {
            //note: it will often not work on devices from other vendors, and it may suddenly stop working (if the API is removed) or crash spectacularly
            val method = wifiManager!!.javaClass.getDeclaredMethod(GET_WIFI_AP_STATE)
            method.isAccessible = true // in the case of visibility change in future APIs
            val wifiApState = method.invoke(wifiManager) as Int
            Timber.d("isWifiApEnabled state: %s", wifiApState)
            //Based on source code https://android.googlesource.com/platform/frameworks/base/+/a029ea1/wifi/java/android/net/wifi/WifiManager.java
            //we are checking if WIFI AP is enabled or being enabled (codes 12 and 13 are from HotspotManager.java source code).
            wifiApState == 12 || wifiApState == 13
        } catch (ignored: Throwable) {
            Timber.d("isWifiApEnabled exception")
            false
        }
    }

    companion object {
        private const val GET_WIFI_AP_STATE = "getWifiApState"
        private const val TETHERING_ENABLE_METHOD = "setWifiApEnabled"
    }
}
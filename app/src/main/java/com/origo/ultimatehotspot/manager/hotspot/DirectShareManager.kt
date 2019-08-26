package com.origo.ultimatehotspot.manager.hotspot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.origo.ultimatehotspot.manager.RootManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import shinil.direct.share.DirectNetShare
import timber.log.Timber

/**
 * Direct share is an alternative to the android framework hotspot. Share the wifi via Wifi direct.
 * Conditions:
 * - any android versions
 * - device needs to be rooted in order to setup the DHCP
 * Pros:
 * - do not depend of the android version, so if your device is rooted you can use this regardless of the android system installed
 * Cons:
 * - device needs to be rooted
 */
class DirectShareManager(private val context: Context) : HotspotManager() {
    private val groupCreatedListener = object : DirectNetShare.DirectNetShareListener {
        override fun onFailure() {
            hotspotLiveData.postValue(HotspotEvent(HotspotEvent.Type.START_ERROR, "Group cannot be created"))
        }

        override fun onGroupCreated(ssid: String, password: String) {
            val hotspotSuccess = HotspotEvent(HotspotEvent.Type.START_SUCCESS)
            hotspotSuccess.ssid = ssid
            hotspotSuccess.password = password
            hotspotLiveData.postValue(hotspotSuccess)
        }
    }

    private val share: DirectNetShare = DirectNetShare(context, groupCreatedListener)

    private fun checkWifiAndStart() {
        if (!isWifiEnabled()) {
            context.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action != null && intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                        val noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                        if (!noConnectivity) {
                            startDirectShare()
                        }
                    }
                }
            }, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            enableWifi()
        } else {
            startDirectShare()
        }
    }

    private fun startDirectShare() {
        share.start()
        CoroutineScope(Dispatchers.Default).launch {
            val success = RootManager.setupDhcp()
            Timber.d("DHCP setup successful? $success")
        }
    }

    override fun start() {
        checkWifiAndStart()
    }

    override fun stop() {
        share.stop()
    }

    private fun enableWifi(): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.setWifiEnabled(true)
    }

    private fun isWifiEnabled(): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }
}
package com.origo.ultimatehotspot.manager.hotspot

import androidx.lifecycle.MutableLiveData

abstract class HotspotManager {
    val hotspotLiveData: MutableLiveData<HotspotEvent> = MutableLiveData()

    abstract fun start()
    abstract fun stop()
}

class HotspotEvent(val type: Type, val error: String? = null) {
    enum class Type {
        START_SUCCESS,
        START_ERROR,
        STOP_SUCCESS,
        STOP_ERROR
    }

    var ssid: String? = null
    var password: String? = null

    fun asCredentials(): Boolean {
        return !ssid.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    fun hasError(): Boolean {
        return !error.isNullOrEmpty()
    }
}
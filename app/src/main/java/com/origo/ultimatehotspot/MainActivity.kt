package com.origo.ultimatehotspot

import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.origo.ultimatehotspot.manager.PermissionsManager
import com.origo.ultimatehotspot.manager.RootManager
import com.origo.ultimatehotspot.manager.hotspot.*
import timber.log.Timber


class MainActivity : AppCompatActivity(), PermissionsManager.PermissionManagerListener {
    private val permissionsManager = PermissionsManager(this)
    private lateinit var infoTv: TextView
    private lateinit var switch: Switch
    private var hasRootGranted: Boolean = false
    private var hotspotManager: HotspotManager? = null

    private val hotspotObserver: Observer<HotspotEvent> = Observer {
        switch.isChecked = it.type == HotspotEvent.Type.START_SUCCESS

        if (it.type == HotspotEvent.Type.START_SUCCESS) infoTv.text =
            resources.getString(R.string.info_message_success, it.ssid, it.password)
        else if (it.type == HotspotEvent.Type.START_ERROR || it.type == HotspotEvent.Type.STOP_ERROR) infoTv.text =
            resources.getString(R.string.info_message_failure, it.error)
    }

    private val onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            startHotspot()
        } else {
            stopHotspot()
        }
    }

    private val rootObserver = Observer<Boolean> {
        hasRootGranted = it

        if (!hasRootGranted && isNougat()) {
            infoTv.text = getString(R.string.error_not_rooted)
            switch.isEnabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        initViews()

        RootManager.hasRootGranted.observe(this, rootObserver)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionsManager.checkPermissions(this)
        } else {
            initHotspotManager()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initViews() {
        infoTv = findViewById(R.id.info)
        switch = findViewById(R.id.wifi_switch)
        switch.isEnabled =
            false //need to make sure everything is properly initialised to activate it
    }

    private fun stopHotspot() {
        Timber.d("stopHotspot")
        toggleHotspot(false)
        infoTv.text = ""
    }

    private fun startHotspot() {
        Timber.d("startHotspot")
        toggleHotspot(true)
    }

    private fun toggleHotspot(toggle: Boolean) {
        if (toggle) {
            hotspotManager?.start()
        } else {
            hotspotManager?.stop()
        }
    }

    override fun onPermissionGranted() {
        initHotspotManager()
    }

    override fun onPermissionRevoked() {
        switch.isEnabled = false
        infoTv.text = resources.getString(R.string.permission_not_granted)
    }

    private fun initHotspotManager() {
        when {
            isMarshmallow() -> hotspotManager =
                MarshmallowWifiManager(this)
            isOreoOrAbove() -> hotspotManager = OreoWifiManager(this)
            isNougat() -> {
                if (hasRootGranted) {
                    hotspotManager = DirectShareManager(this)
                } else {
                    infoTv.text =
                        getString(R.string.error_not_rooted)

                }
            }
        }

        Timber.d("initHotspotManager $hotspotManager")
        hotspotManager?.hotspotLiveData?.observe(this, hotspotObserver)

        //hotspot switch is enabled only if we have our way to enable to hotspot
        switch.isEnabled = hotspotManager != null
        switch.setOnCheckedChangeListener(onCheckedChangeListener)
    }
}
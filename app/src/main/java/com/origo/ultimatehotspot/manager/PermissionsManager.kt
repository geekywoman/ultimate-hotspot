package com.origo.ultimatehotspot.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.origo.ultimatehotspot.R
import java.util.*

class PermissionsManager(private val context: Context) : ActivityCompat.OnRequestPermissionsResultCallback {
    private var listener: PermissionManagerListener? = null

    interface PermissionManagerListener {
        fun onPermissionGranted()
        fun onPermissionRevoked()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.isEmpty()) {
                listener?.onPermissionRevoked()
            } else {
                var permissionGranted = true
                for (i in grantResults.indices) {
                    permissionGranted = permissionGranted && (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                }

                if (permissionGranted) {
                    listener?.onPermissionGranted()
                } else {
                    //we exit the app even if only one permission is not granted
                    Toast.makeText(
                        context,
                        String.format(
                            Locale.getDefault(),
                            context.getString(R.string.permission_not_granted),
                            Arrays.toString(getMissingPermissions())
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                    listener?.onPermissionRevoked()
                }
            }
        }
        //to avoid memory leak
        listener = null
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        const val PERMISSION_REQUEST = 1001
    }

    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMissingPermissions(): Array<String> {
        return PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
    }

    fun checkPermissions(activity: Activity) {
        this.listener = activity as PermissionManagerListener
        val missingPermissions = getMissingPermissions()

        // we request all missing permissions
        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, missingPermissions, PERMISSION_REQUEST)
        } else {
            listener?.onPermissionGranted()
            //to avoid memory leak
            listener = null
        }

        checkSystemWritePermission(activity)
    }

    private fun checkSystemWritePermission(activity: Activity): Boolean {
        if (!Settings.System.canWrite(activity)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:${activity.packageName}")
            activity.startActivity(intent)
            return false
        }
        return true
    }
}
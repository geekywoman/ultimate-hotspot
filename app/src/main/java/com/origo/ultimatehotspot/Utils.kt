package com.origo.ultimatehotspot

import android.os.Build

const val HOTSPOT_DEFAULT_SSID : String = "Android hotspot"
const val HOTSPOT_DEFAULT_PASSWORD : String ="Th!s!s4Gr34tP4ssw0rd"

fun isNougat(): Boolean {
    return Build.VERSION.SDK_INT == Build.VERSION_CODES.N || Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1
}

fun isMarshmallow(): Boolean {
    return Build.VERSION.SDK_INT == Build.VERSION_CODES.M
}

fun isOreoOrAbove(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}
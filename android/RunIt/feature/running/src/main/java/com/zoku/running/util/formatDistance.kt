package com.zoku.running.util

fun meterToKiloString(meter: Int): String {
    return "${meter/1000}.${meter/100}"
}

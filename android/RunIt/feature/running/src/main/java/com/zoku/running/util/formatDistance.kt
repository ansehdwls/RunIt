package com.zoku.running.util

fun meterToKiloString(meter: Int): String {
    val kilometer = meter / 1000
    val decimalPart = (meter % 1000) / 10
    return String.format("%d.%02d", kilometer, decimalPart)
}

package com.zoku.running.util

fun meterToKilo(meter: Int): String {
    return "${meter/1000}.${meter/100}"
}
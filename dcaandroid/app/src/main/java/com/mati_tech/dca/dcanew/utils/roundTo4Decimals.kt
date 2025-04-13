package com.mati_tech.dca.dcanew.utils

fun Double.roundTo4Decimals(): Double {
    return (this * 10000.0).toLong() / 10000.0
}
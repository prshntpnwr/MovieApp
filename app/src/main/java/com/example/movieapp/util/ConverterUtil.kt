package com.example.movieapp.util

class ConverterUtil {

    private val NAMES = arrayOf(
        "Thousand",
        "Million",
        "Billion",
        "Trillion"
    )

    fun amountFormat(value: Double, iteration: Int): String {
        val d = value.toLong() / 100 / 10.0
        val isRound = d * 10 % 10 == 0.0
        return if (d < 1000)
            (if (d > 99.9 || isRound || !isRound && d > 9.99)
                d.toInt() * 10 / 10
            else
                d.toString() + "").toString() + " " + NAMES[iteration]
        else
            amountFormat(d, iteration + 1)
    }

}
package com.perpetio.util.pagination

import kotlin.math.ceil
import kotlin.math.floor


interface Pagination {
    val page: Int
    val size: Int
}

val Pagination.firstIndex: Int
    get() = page * size

fun calculatePagesNumber(datasetSize: Long, pageSize: Int): Int {
    return ceil(datasetSize.toDouble() / pageSize).toInt()
}

fun calculatePagesNumber(datasetSize: Int, pageSize: Int): Int =
    calculatePagesNumber(
        datasetSize.toLong(),
        pageSize
    )

fun calculatePage(firstIndex: Int, resultsSize: Int): Int = if (resultsSize > 0) {
    floor(firstIndex.toFloat() / resultsSize).toInt()
} else {
    0
}

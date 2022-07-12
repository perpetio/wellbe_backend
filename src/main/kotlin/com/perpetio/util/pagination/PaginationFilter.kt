package com.perpetio.util.pagination

import io.ktor.http.*

const val defaultLargePageSize = 20

fun handlePagination(queryParams: Parameters): SimplePagination {
    return SimplePagination(
        page = queryParams["page"]?.toInt() ?: 0,
        size = queryParams["size"]?.toInt() ?: defaultLargePageSize
    )
}

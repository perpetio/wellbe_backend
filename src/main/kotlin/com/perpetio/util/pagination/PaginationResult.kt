package com.perpetio.util.pagination

import kotlinx.serialization.Serializable

@Serializable
data class SimplePagination(
    override val page: Int,
    override val size: Int
) : Pagination

@Serializable
data class PaginationResult<T>(
    override val page: Int,
    val pagesNumber: Int,
    val results: List<T>,
    override val size: Int
) : Pagination

fun <T> emptyPaginationResult() = PaginationResult<T>(0, 0, emptyList(), 0)

fun <T> List<T>.createPaginationResult(
    pagination: Pagination,
    commonObjectsNumber: Long
) = PaginationResult(
    pagination.page,
    calculatePagesNumber(
        commonObjectsNumber,
        pagination.size
    ),
    this,
    pagination.size
)

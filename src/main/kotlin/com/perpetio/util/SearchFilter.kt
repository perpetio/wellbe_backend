package com.perpetio.util

import io.ktor.http.*

fun handleSearchFilters(queryParams: Parameters): SearchFilter? {
    var searchFilter: SearchFilter? = null
    if (queryParams.contains("search")) {
        searchFilter = SearchFilter(queryParams["search"].toString())
    }

    return searchFilter
}

data class SearchFilter(
    val search: String
)
package dev.urieloalves.studyconnect.domain.shared.pagination

data class Pagination<T>(
    val currentPage: Int,
    val perPage: Int,
    val total: Long,
    val items: List<T>
)

package com.example.weightbridge.ui.utils

inline fun <T, R : Comparable<R>> List<T>.sortByField(
    crossinline selector: (T) -> R?,
    isDescending: Boolean = true
): List<T> {
    return if (isDescending) {
        this.sortedByDescending(selector)
    } else {
        this.sortedBy(selector)
    }
}
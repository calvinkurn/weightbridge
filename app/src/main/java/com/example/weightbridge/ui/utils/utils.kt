package com.example.weightbridge.ui.utils

// please take a note between capital letter
// A = 65 || a = 97
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
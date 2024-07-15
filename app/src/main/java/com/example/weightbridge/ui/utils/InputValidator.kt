package com.example.weightbridge.ui.utils

// not empty
internal fun isDriverNameError(input: String): Boolean {
    return input.isEmpty()
}

// license format [up to 2 character] [0-9999] [up to 3 character]
internal fun isLicenseNumberError(input: String): Boolean {
    val regex = Regex("""^[A-Za-z]{1,2} \d{1,4} [A-Za-z]{1,3}$""")
    return !regex.matches(input)
}

// inbound is number
internal fun isInBoundError(inboundWeight: String): Boolean {
    return try {
        inboundWeight.toInt()
        false
    } catch (_: Exception) {
        true
    }
}

// net weight should greater than 0 AND outbound weight should be number
internal fun isOutBoundError(netWeight: Int, outBoundWeight: String): Boolean {
    return try {
        !(netWeight > 0 && outBoundWeight.toInt() > 0)
    } catch (_: Exception) {
        true
    }
}
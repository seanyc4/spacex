package com.seancoyle.core.ui.util

/**
 * Convert ISO 3166-1 alpha-2 country code to flag emoji
 */
fun String.toCountryFlag(): String {
    if (this.length != 2) return ""
    val firstChar = Character.codePointAt(this, 0) - 0x41 + 0x1F1E6
    val secondChar = Character.codePointAt(this, 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}

package com.mashiverse.mashit.utils.helpers

import com.mashiverse.mashit.data.models.mashi.NftDetails

fun parseName(name: String): NftDetails {
    try {
        val (fullName: String, authorName: String) = if (" by " in name) {
            val parts = name.split(" by ", limit = 2)
            parts[0] to parts[1]
        } else {
            "" to ""
        }

        val (name: String, mint: Int) = if (" #" in fullName) {
            val index = fullName.lastIndexOf(" #")
            val namePart = fullName.substring(0, index)
            val mintPart = fullName.substring(index + 2).toIntOrNull() ?: -1

            namePart to mintPart
        } else {
            "" to -1
        }

        return NftDetails(
            authorName = authorName,
            name = name,
            mint = mint
        )
    } catch (e: Exception) {
        return NftDetails(
            authorName = "",
            name = "",
            mint = -1
        )
    }
}
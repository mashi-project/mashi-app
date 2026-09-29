package com.serhij.mashi.utils.helpers

fun String.fromIpfsScheme() = this.replace("ipfs://", "https://ipfs.filebase.io/ipfs/")

fun String.toIpfsUri() = this.replace("https://ipfs.filebase.","https://ipfs.")

fun String.toFilebaseUri() = this
    .replace("https://ipfs.", "https://ipfs.filebase.")
    .replace("https://alchemy.mypinata.cloud", "https://ipfs.filebase.io")

fun String.toIpfsPartialUri() = this.replace("https://ipfs.filebase.io/ipfs/", "")
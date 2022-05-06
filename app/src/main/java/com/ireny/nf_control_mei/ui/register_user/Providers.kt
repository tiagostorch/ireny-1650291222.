package com.ireny.nf_control_mei.ui.register_user

data class ProviderInfo(
    val uid: String,
    val provider: String
)

fun ProviderInfo.isExternalProvider(): Boolean {
    return !provider.contains("firebase")
}
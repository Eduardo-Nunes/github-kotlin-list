package com.okashitech.githubkotlinlist.extensions

import com.okashitech.githubkotlinlist.R
import com.okashitech.githubkotlinlist.components.infobar.InfoBarView


private const val INTERNET_ALERT_TIMEOUT = 3000L

fun InfoBarView.hasConnectionAlert() {
    show(R.drawable.ic_smile, R.string.boa, R.string.reestablished_internet_connection)
    hideOnDelay(INTERNET_ALERT_TIMEOUT)
}

fun InfoBarView.noConnectionAlert() =
    show(R.drawable.ic_attention_circle, R.string.opa, R.string.no_internet_connection)

package edu.bluejack23_2.demarj.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

@Parcelize
data class Notification(
    val notifId: String? = null,
    val userId: String? = null,
    val preOrderId: String? = null,
    val notifTime: String? = null
) : Parcelable

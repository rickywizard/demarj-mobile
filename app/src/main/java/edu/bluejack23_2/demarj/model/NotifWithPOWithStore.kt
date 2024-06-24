package edu.bluejack23_2.demarj.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotifWithPOWithStore(
    val notif: Notification,
    val po_with_store: PreOrderWithStore
) : Parcelable

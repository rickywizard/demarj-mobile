package edu.bluejack23_2.demarj.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    val transactionId: String? = null,
    val poId: String? = null,
    val userId: String? = null,
    val quantity: Int? = 0,
    val large: Boolean? = false,
    val notes: String? = null,
    val total_price: Int? = 0,
    val paid: Boolean? = false,
) : Parcelable

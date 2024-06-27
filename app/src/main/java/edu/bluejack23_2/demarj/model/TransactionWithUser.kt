package edu.bluejack23_2.demarj.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionWithUser(
    val transaction: Transaction,
    val user: User
) : Parcelable

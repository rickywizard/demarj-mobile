package edu.bluejack23_2.demarj.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val transaction: Transaction,
    val preOrder: PreOrder,
    val store: User
) : Parcelable

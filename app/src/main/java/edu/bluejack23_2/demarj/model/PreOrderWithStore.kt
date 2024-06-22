package edu.bluejack23_2.demarj.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PreOrderWithStore(
    val preOrder: PreOrder,
    val store: User
) : Parcelable

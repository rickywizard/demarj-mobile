package edu.bluejack23_2.demarj.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: String? = null,
    val profile_picture: String? = null,
    val fullname: String? = null,
    val email: String? = null,
    val phone_number: String? = null,
    val role: String? = null,
    val store_name: String? = null,
) : Parcelable

package edu.bluejack23_2.demarj.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class PreOrder(
    val poId: String? = null,
    val po_img: String? = null,
    val po_name: String? = null,
    val po_desc: String? = null,
    val po_price: Int? = 0,
    val po_large_price: Int? = 0,
    val po_end_date: String? = null,
    val po_ready_date: String? = null,
    val po_stock: Int? = 0,
    val po_ownerId: String? = null
) : Parcelable

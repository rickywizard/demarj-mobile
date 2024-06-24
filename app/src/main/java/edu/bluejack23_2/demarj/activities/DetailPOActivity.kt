package edu.bluejack23_2.demarj.activities

import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.ActivityDetailPoactivityBinding
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import java.text.NumberFormat
import java.util.*

class DetailPOActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPoactivityBinding

    fun formatToRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPoactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.detailBackBttn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val data = intent.getParcelableExtra("DATA", PreOrderWithStore::class.java)

        if (data != null) {
            consumeData(data)
        }
    }

    private fun consumeData(data: PreOrderWithStore) {
        Glide.with(this).load(data.preOrder.po_img).into(binding.detailPoImage)
        binding.detailPoName.text = data.preOrder.po_name
        binding.detailPoPrice.text = formatToRupiah(data.preOrder.po_price!!)
        binding.detailPoDesc.text = data.preOrder.po_desc
        binding.detailPoEndDate.text = data.preOrder.po_end_date
        binding.detailPoReadyDate.text = data.preOrder.po_ready_date
        binding.detailLargeSize.text = data.preOrder.po_large_price.toString()

        if (!data.store.profile_picture.isNullOrEmpty()) {
            Glide.with(this).load(data.store.profile_picture).into(binding.photoProfileDetailPo)
        } else {
            binding.detailPoImage.setImageResource(R.drawable.dummy_profile)
        }

        binding.detailStoreNameText.text = data.store.store_name
    }
}
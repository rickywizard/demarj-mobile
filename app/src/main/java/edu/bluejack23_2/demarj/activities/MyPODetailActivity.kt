package edu.bluejack23_2.demarj.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.databinding.ActivityMyPodetailBinding
import edu.bluejack23_2.demarj.model.History
import edu.bluejack23_2.demarj.model.PreOrder
import java.text.NumberFormat
import java.util.*

class MyPODetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPodetailBinding
    private lateinit var sharedPreferences: SharedPreferences

    private fun formatToRupiah(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount).replace("Rp", "Rp ").replace(",00", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPodetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("DATA", PreOrder::class.java)
        } else {
            intent.getParcelableExtra<PreOrder>("DATA")
        }

        if (data != null) {
            consumeData(data)
        }
    }

    private fun consumeData(data: PreOrder) {
        Glide.with(this).load(data.po_img).into(binding.detailPoImage)
        binding.detailPoName.text = data.po_name
        binding.detailPoPrice.text = formatToRupiah(data.po_price!!)
        binding.detailPoDesc.text = data.po_desc
        binding.detailPoEndDate.text = data.po_end_date
        binding.detailPoReadyDate.text = data.po_ready_date

        Glide.with(this).load(sharedPreferences.getString("profile_picture", null)).into(binding.photoProfileDetailPo)
        binding.detailStoreNameText.text = sharedPreferences.getString("store_name", null)
    }
}
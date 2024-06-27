package edu.bluejack23_2.demarj.activities

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.ActivityUpdatePoactivityBinding
import edu.bluejack23_2.demarj.model.PreOrder
import edu.bluejack23_2.demarj.model.PreOrderWithStore
import edu.bluejack23_2.demarj.viewmodel.PreOrderViewModel
import edu.bluejack23_2.demarj.viewmodel.TransactionViewModel
import java.util.*

class UpdatePOActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePoactivityBinding
    private var preOrderImg: Uri? = null
    private lateinit var viewModel: PreOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdatePoactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel = ViewModelProvider(this).get(PreOrderViewModel::class.java)

        val galeryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.photoPreorderEdit.setImageURI(it)
                preOrderImg = it
            }
        )

        binding.editTakePreorderPhotoBttn.setOnClickListener {
            galeryImage.launch("image/*")
        }

        binding.createEndDatePickerEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.poEndDateEdit.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        binding.createReadyDatePickerEdit.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.poReadyDateEdit.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("DATA", PreOrder::class.java)
        } else {
            intent.getParcelableExtra<PreOrder>("DATA")
        }

        if (data != null) {
            consumeData(data)

            updateListener(data)
        }
    }

    private fun updateListener(data: PreOrder) {
        viewModel.updateStatus.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
            }
        })

        val po_name = binding.poNameEdit.text.toString()
        val po_desc = binding.poDescEdit.text.toString()
        val po_price = binding.poPriceEdit.text.toString()
        val po_large_price = binding.poAdditionalEdit.text.toString()
        val po_end_date = binding.poEndDateEdit.text.toString()
        val po_ready_date = binding.poReadyDateEdit.text.toString()
        val po_stock = binding.poStockEdit.text.toString()


    }

    private fun consumeData(data: PreOrder) {
        Glide.with(this).load(data.po_img).into(binding.photoPreorderEdit)
        binding.poNameEdit.setText(data.po_name)
        binding.poDescEdit.setText(data.po_desc)
        binding.poPriceEdit.setText(data.po_price.toString())
        if (data.po_large_price != 0) {
            binding.poAdditionalEdit.setText(data.po_large_price.toString())
        }
        binding.poEndDateEdit.text = data.po_end_date
        binding.poReadyDateEdit.text = data.po_ready_date
        binding.poStockEdit.setText(data.po_stock.toString())
    }
}
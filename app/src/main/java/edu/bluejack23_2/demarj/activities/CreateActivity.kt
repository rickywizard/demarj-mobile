package edu.bluejack23_2.demarj.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bluejack23_2.demarj.databinding.ActivityCreateBinding
import edu.bluejack23_2.demarj.viewmodel.AddPreOrderViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private val addPreOrderViewModel: AddPreOrderViewModel by viewModels()
    private var preOrderImg: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createBackBttn.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        addPreOrderViewModel.initSharedPreferences(sharedPreferences)

        val galeryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.photoPreorderCreate.setImageURI(it)
                preOrderImg = it
            }
        )

        binding.takePreorderPhotoBttn.setOnClickListener {
            galeryImage.launch("image/*")
        }

        binding.createEndDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.poEndDateCreate.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        binding.createReadyDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.poReadyDateCreate.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }


        binding.createBttn.setOnClickListener{
            val po_name = binding.poNameCreate.text.toString()
            val po_desc = binding.poDescCreate.text.toString()
            val po_price = binding.poPriceCreate.text.toString()
            val po_large_price = binding.poAdditionalCreate.text.toString()
            val po_end_date = binding.poEndDateCreate.text.toString()
            val po_ready_date = binding.poReadyDateCreate.text.toString()
            val po_stock = binding.poStockCreate.text.toString()

            if (preOrderImg == null) {
                Toast.makeText(this, "Please select a pre order image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addPreOrderViewModel.validateAddPreOrder(
                preOrderImg!!,
                po_name,
                po_desc,
                po_price,
                po_large_price,
                po_end_date,
                po_ready_date,
                po_stock
            )

        }

        addPreOrderViewModel.preOrderResult.observe(this, Observer { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Add Pre Order Successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Add Pre OrderFailed", Toast.LENGTH_SHORT).show()
            }
        })

        addPreOrderViewModel.errorMessage.observe(this, Observer { errorMsg ->
            errorMsg?.let { err ->
                if(err != null){
                    Toast.makeText(this, err, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
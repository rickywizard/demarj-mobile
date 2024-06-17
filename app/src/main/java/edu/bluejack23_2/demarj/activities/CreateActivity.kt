package edu.bluejack23_2.demarj.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import edu.bluejack23_2.demarj.databinding.ActivityCreateBinding
import edu.bluejack23_2.demarj.viewmodel.AddPreOrderViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private val addPreOrderViewModel: AddPreOrderViewModel by viewModels()
    private var preOrderImg: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createBackBttn.setOnClickListener{
            onBackPressed()
        }

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

        binding.createBttn.setOnClickListener{
            val po_name = binding.poNameCreate.text.toString()
            val po_desc = binding.poDescCreate.text.toString()
            val po_price_text = binding.poPriceCreate.text.toString()
            val po_large_price_text = binding.poAdditionalCreate.text.toString()
            val po_end_date_text = binding.poEndDateCreate.text.toString()
            val po_ready_date_text = binding.poReadyDateCreate.text.toString()
            val po_stock_text = binding.poStockCreate.text.toString()

            val po_price = po_price_text.toIntOrNull()
            val po_large_price = po_large_price_text.toIntOrNull()
            val po_stock = po_stock_text.toIntOrNull()

            val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val po_end_date: LocalDate? = try {
                LocalDate.parse(po_end_date_text, dateFormatter)
            } catch (e: DateTimeParseException) {
                null
            }

            val po_ready_date: LocalDate? = try {
                LocalDate.parse(po_ready_date_text, dateFormatter)
            } catch (e: DateTimeParseException) {
                null
            }

            if (preOrderImg == null) {
                Toast.makeText(this, "Please select a pre order image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }
    }
}
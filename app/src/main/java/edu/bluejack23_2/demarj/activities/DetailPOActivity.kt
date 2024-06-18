package edu.bluejack23_2.demarj.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack23_2.demarj.databinding.ActivityDetailPoactivityBinding

class DetailPOActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPoactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPoactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
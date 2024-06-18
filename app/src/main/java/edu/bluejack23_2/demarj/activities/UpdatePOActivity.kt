package edu.bluejack23_2.demarj.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack23_2.demarj.databinding.ActivityUpdatePoactivityBinding

class UpdatePOActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePoactivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdatePoactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package edu.bluejack23_2.demarj.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.ActivityMainBinding
import edu.bluejack23_2.demarj.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        replaceFragment(HomeFragment())

//        binding.botNav.seton
    }

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentTranscation = supportFragmentManager.beginTransaction()
//        fragmentTranscation.replace(R.id.fragmenContainer, fragment)
//        fragmentTranscation.commit()
//    }
}
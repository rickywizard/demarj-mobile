package edu.bluejack23_2.demarj.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import edu.bluejack23_2.demarj.R
import edu.bluejack23_2.demarj.databinding.ActivityMainBinding
import edu.bluejack23_2.demarj.fragments.HomeFragment
import edu.bluejack23_2.demarj.fragments.NotificationFragment
import edu.bluejack23_2.demarj.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.botNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenu -> {
                    replaceFragment(HomeFragment())
                }
                R.id.notificationMenu -> {
                    replaceFragment(NotificationFragment())
                }
                R.id.profileMenu -> {
                    replaceFragment(ProfileFragment())
                }
            }
            false
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
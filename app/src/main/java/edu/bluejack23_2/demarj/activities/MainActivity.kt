package edu.bluejack23_2.demarj.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val role = sharedPreferences.getString("role", "")

        Log.d("USER", "Role: $role")

        if(role.equals("User")){
            binding.botNav.menu.findItem(R.id.notificationMenu).isVisible = true
            binding.botNav.menu.findItem(R.id.createMenu).isVisible = false
        }
        else if(role.equals("Store Owner")){
            binding.botNav.menu.findItem(R.id.createMenu).isVisible = true
            binding.botNav.menu.findItem(R.id.notificationMenu).isVisible = false
        }

        binding.botNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeMenu -> {
                    replaceFragment(HomeFragment())
                }
                R.id.notificationMenu -> {
                    replaceFragment(NotificationFragment())
                }
                R.id.createMenu -> {
                    val intent = Intent(this, CreateActivity::class.java)
                    startActivity(intent)
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
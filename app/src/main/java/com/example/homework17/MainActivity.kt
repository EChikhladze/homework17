package com.example.homework17

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.homework17.databinding.ActivityMainBinding
import com.example.homework17.login.LogInFragmentDirections

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkRememberedUser()
    }

    private fun checkRememberedUser() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)
        val rememberUser = sharedPref.getBoolean("rememberUser", false)

        if (rememberUser && email != null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            val action = LogInFragmentDirections.actionLogInFragmentToHomeFragment(email)
            navController.navigate(action)
        }
    }
}
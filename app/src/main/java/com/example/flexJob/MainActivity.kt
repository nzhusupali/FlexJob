package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.flexJob.R.string
import com.example.flexJob.databinding.ActivityMainBinding
import com.example.flexJob.fragment.StateAdapter
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val btnLogin = _binding.goingTologgin


        initViewPager2WithFragments()

        btnLogin.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settings_bottom_nav_menu ->
                    startActivity(
                        Intent(
                            this@MainActivity,
                            SettingsActivity::class.java
                        )
                    )
            }
            return@OnNavigationItemSelectedListener true
        })


        /**
         * Google Auth Start
         */
        val auth by lazy {
            FirebaseAuth.getInstance()
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(string.default_web_client_id))
            .requestEmail()
            .build()
        /**
         * Google Auth End
         */
    }

    private fun initViewPager2WithFragments() {
        val viewPager2: ViewPager2 = findViewById(R.id.viewpager)
        val adapter = StateAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        val string: String = resources.getString(string.findJob)
        val string1: String = resources.getString(R.string.find_employee)

        val tabLayout: TabLayout = findViewById(R.id.tablayout)
        val names: Array<Any> = arrayOf(string, string1)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = names[position].toString()
        }.attach()
    }

}


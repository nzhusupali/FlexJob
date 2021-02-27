package com.example.flexJob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.forget_pass_activity.*

class   forgetPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_pass_activity)
        backPressed_forget_Pass.setOnClickListener {
            onBackPressed()
        }
    }
}

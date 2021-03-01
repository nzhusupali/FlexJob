package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.forget_pass_activity.*


class ForgetPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_pass_activity)

        BTN_reset_pass_forget_pass_activity.setOnClickListener {
            val email = ET_email_forget_pass_activity.text.toString().trim()
            if (email.isEmpty()) {
                ET_email_forget_pass_activity.error = "Email Required"
                ET_email_forget_pass_activity.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ET_email_forget_pass_activity.error = "Valid Email Required"
                ET_email_forget_pass_activity.requestFocus()
                return@setOnClickListener
            }
            progressbar.visibility = View.VISIBLE
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    progressbar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgetPassActivity,
                            "Check your email",
                            Toast.LENGTH_LONG

                        ).show()
                        startActivity(Intent(this@ForgetPassActivity,LoginActivity::class.java))
                    } else {
                        Toast.makeText(
                            this@ForgetPassActivity,
                            task.exception?.message!!,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }
}
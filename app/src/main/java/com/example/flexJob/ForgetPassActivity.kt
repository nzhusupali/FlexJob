package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flexJob.databinding.ForgetPassActivityBinding
import com.google.firebase.auth.FirebaseAuth


class ForgetPassActivity : AppCompatActivity() {
    private lateinit var _binding: ForgetPassActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ForgetPassActivityBinding.inflate(layoutInflater)
        setContentView(R.layout.forget_pass_activity)

        val resetPassword = _binding.BTNResetPassForgetPassActivity
        val etForgetPassword = _binding.ETEmailForgetPassActivity
        val progressBar = _binding.progressbar

        resetPassword.setOnClickListener {
            val emailReq: String = resources.getString(R.string.email_req)
            val validEmailReq: String = resources.getString(R.string.valid_email_req)

            val email = etForgetPassword.text.toString().trim()
            if (email.isEmpty()) {
                etForgetPassword.error = emailReq
                etForgetPassword.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etForgetPassword.error = validEmailReq
                etForgetPassword.requestFocus()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ForgetPassActivity,
                            R.string.Check_email,
                            Toast.LENGTH_LONG

                        ).show()
                        startActivity(Intent(this@ForgetPassActivity, LoginActivity::class.java))
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        finish()
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
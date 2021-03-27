
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
            val emailReq : String = resources.getString(R.string.email_req)
            val validEmailReq : String = resources.getString(R.string.valid_email_req)

            val email = ET_email_forget_pass_activity.text.toString().trim()
            if (email.isEmpty()) {
                ET_email_forget_pass_activity.error = emailReq
                ET_email_forget_pass_activity.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ET_email_forget_pass_activity.error = validEmailReq
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
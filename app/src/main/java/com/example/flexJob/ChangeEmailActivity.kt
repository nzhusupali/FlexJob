package com.example.flexJob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.change_email_activity.*

class ChangeEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_email_activity)
        BTN_change_email_activity.setOnClickListener {
            when {
                TextUtils.isEmpty(
                    ET_oldLogin_change_email_activity.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.text_old_email,
                        Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(
                    ET_newEmail_change_email_activity.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.text_new_email,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    val user = Firebase.auth.currentUser
                    val newEmail: String =
                        ET_newEmail_change_email_activity.text.toString().trim { it <= ' ' }
                    user!!.updateEmail(newEmail)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    R.string.email_update,
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, SettingsActivity::class.java))
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                finish()
                            } else
                                Toast.makeText(
                                    this,
                                    R.string.Error_server,
                                    Toast.LENGTH_LONG
                                ).show()
                        }
                }
            }
        }
    }
}
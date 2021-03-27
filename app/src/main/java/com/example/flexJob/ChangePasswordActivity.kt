package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.change_password_activity.*

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password_activity)
        BTN_change_password_activity.setOnClickListener {
            when {
                TextUtils.isEmpty(
                    ET_oldPass_change_password_activity.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.text_old_password,
                        Toast.LENGTH_LONG
                    ).show()
                }
                TextUtils.isEmpty(
                    ET_newPass_change_password_activity.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        R.string.text_new_password,
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> {
                    val user = Firebase.auth.currentUser
                    val newPassword: String =
                        ET_newPass_change_password_activity.text.toString().trim { it <= ' ' }
                    user!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    R.string.pass_update,
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
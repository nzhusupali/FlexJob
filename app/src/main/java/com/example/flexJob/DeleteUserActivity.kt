package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.delete_user_activity.*

class DeleteUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.delete_user_activity)

        BTN_delete_account_activity.setOnClickListener {

            deleteUer()
        }
    }

    private fun deleteUer() {
        val tag = "delete_user_activity"
        val user = Firebase.auth.currentUser!!
        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "User account deleted.")
                    val intent =
                        Intent(this, MainActivity::class.java)
                    Toast.makeText(
                        this,
                        "Account deleted",
                        Toast.LENGTH_LONG
                    ).show()
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Not Work",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}

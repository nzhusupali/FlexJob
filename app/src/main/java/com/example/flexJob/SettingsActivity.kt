package com.example.flexJob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        /**
         * When entering settings, it first checks if you are logged in. If so, the settings are started;
         * if not, go to LoginActivity.kt
         */
        /**
         * Check Auth status START
         */
        val checkAuth = Firebase.auth
        val checkAuthSttus = checkAuth.currentUser
        if (checkAuthSttus != null){
            CNG_loginInf_settings_activity.text= FirebaseAuth.getInstance().currentUser!!.email
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        /**
         * Check Auth status END
         */

        circleImageView.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
        BTN_delete_account_settings_activity.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, DeleteUserActivity::class.java))
        }

        /**
         * Exit account start
         */
        lateinit var mGoogleSignInClient: GoogleSignInClient
        val auth by lazy {
            FirebaseAuth.getInstance()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)

//        BTN_exit_settings_activity.setOnClickListener {
//            mGoogleSignInClient.signOut().addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val intent = Intent(this@SettingsActivity, MainActivity::class.java)
//                    Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
//                    startActivity(intent)
//                    finish()
//                } else{
//                    FirebaseAuth.getInstance().signOut()
//                    startActivity(Intent(this@SettingsActivity, SettingsActivity::class.java))
//                    Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
        BTN_exit_settings_activity.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            mGoogleSignInClient.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        /**
         * Exit account end
         */
    }

}

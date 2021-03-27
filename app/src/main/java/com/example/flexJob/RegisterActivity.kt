package com.example.flexJob

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val Req_Code: Int = 123
    private val firebaseAuth = FirebaseAuth.getInstance()
    var callBackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        text_HaveAcc_activity_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            finish()
        }
        BTN_next_email_pass_register.setOnClickListener {
            val enterEmail: String = resources.getString(R.string.enter_email)
            val enterPassword: String = resources.getString(R.string.enter_password)
            val validEmail: String = resources.getString(R.string.valid_email_req)

            when {
                TextUtils.isEmpty(
                    ET_email_register_activity.text.toString().trim { it <= ' ' }) -> {
                    ET_email_register_activity.error = enterEmail
                    ET_email_register_activity.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(
                    ET_password_register_activity.text.toString().trim { it <= ' ' }) -> {
                    ET_password_register_activity.error = enterPassword
                    ET_password_register_activity.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    val email: String =
                        ET_email_register_activity.text.toString().trim { it <= ' ' }
                    val password: String =
                        ET_password_register_activity.text.toString().trim { it <= ' ' }

                    // Create an instance and create a register a user with email and password.
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            // If the registration is successfully done
                            if (task.isSuccessful) {

//                                FireBase register user
                                task.result!!.user!!

                                Toast.makeText(
                                    this@RegisterActivity,
                                    "You are register successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
//                                If all operation successfully, going to MainActivity
                                val intent =
                                    Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                }
            }
        }
        /**
         * There started Google auth in login
         */
        // Configure Google Sign In inside onCreate method
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // initialize the firebaseAuth variable
        FirebaseAuth.getInstance()
        Sign_Google_register_activity.setOnClickListener {
            signInGoogle()
        }
        /** There End Google auth ...*/

        /** There start Facebook auth firebase*/
        callBackManager = CallbackManager.Factory.create()
        Sign_facebook_register_activity.setReadPermissions(R.string.e_mail.toString())
        Sign_facebook_register_activity.setOnClickListener {
            signin()
        }
        /** There end Facebook auth firebase*/

    }

    /** There start Google auth in Firebase     ...*/
    // signInGoogle() function
    private fun signInGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    // onActivityResult() function : this is where we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Faceebook
        callBackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // UpdateUI() function - this is where we specify what UI updation are needed after google signin has taken place.
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                SavedPreference.setEmail(this, account.email.toString())
                SavedPreference.setUsername(this, account.displayName.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    /**
     * НЕ ЗАБУДЬ ВКЛЮЧИТЬ
     */
//    override fun onStart() {
//        super.onStart()
//        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//    } // не забудь вкключить
    /** There end Google auth in Firebase     ...*/

    /** Facebook Auth Firebase Started*/
    private fun signin() {
        Sign_facebook_register_activity.registerCallback(callBackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFaceBookAccessToken(result!!.accessToken)

            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }
        })
    }

    private fun handleFaceBookAccessToken(accessToken: AccessToken?) {
        // get the credentials
        val credentials = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth.signInWithCredential(credentials)
            .addOnSuccessListener { result ->
                // get the email
                val email = result.user?.email
                Toast.makeText(this, "You logged in with $email", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }

    }
    /** Facebook Auth Firebase Ended*/
}
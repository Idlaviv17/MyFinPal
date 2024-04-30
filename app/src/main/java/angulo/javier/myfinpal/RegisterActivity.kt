package angulo.javier.myfinpal

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import angulo.javier.myfinpal.domain.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var et_email : EditText
    lateinit var et_password1 : EditText
    lateinit var et_password2 : EditText
    lateinit var btn_register : Button
    lateinit var et_username: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) { }

        et_email = findViewById(R.id.et_email_register)
        et_password1 = findViewById(R.id.et_password1_register)
        et_password2 = findViewById(R.id.et_password2_register)
        btn_register = findViewById(R.id.btn_register_layout)
        et_username = findViewById(R.id.et_username_register)

        auth = Firebase.auth
        database = Firebase.database.reference

        btn_register.setOnClickListener {
            var email: String = et_email.text.toString()
            var password1: String = et_password1.text.toString()
            var password2: String = et_password2.text.toString()
            var username = et_username.text.toString()

            if (!email.isNullOrEmpty() && !password1.isNullOrEmpty() && !password2.isNullOrEmpty()) {

                if (password1.length < 6 || password2.length < 6) {
                    Toast.makeText(this, "Password needs to be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener;
                }

                if (!isEmailValid(email)) {
                    Toast.makeText(this, "Entered email is not a valid email.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener;
                }

                if (username.isNullOrEmpty()) {
                    Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener;
                }

                if (password1 == password2) {

                    auth.createUserWithEmailAndPassword(email, password1)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                var uid = user?.uid ?: "123"
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("success", "createUserWithEmail:success")
                                addUser(uid, email, username)
                                finish()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("error", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }

                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUser(uid: String, email: String, username: String) {
        val user = User(email, username)

        database.child("users").child(uid).setValue(user)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "$username has been registered",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "The user connot be saved",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$")
        return emailRegex.matches(email)
    }
}
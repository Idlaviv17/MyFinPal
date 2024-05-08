package angulo.javier.myfinpal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class PasswordRecoveryActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    lateinit var et_email: EditText
    lateinit var btn_recovery: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_recovery)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) { }

        et_email = findViewById(R.id.et_password_recovery)
        btn_recovery = findViewById(R.id.btn_password_recovery)

        auth = Firebase.auth

        btn_recovery.setOnClickListener {
            var email: String = et_email.text.toString()

            if(!email.isNullOrEmpty()) {

                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Verification email sent.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "An error has occurred.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Enter email.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package angulo.javier.myfinpal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    lateinit var btn_login: Button
    lateinit var tv_signup: TextView
    lateinit var tv_recovery: TextView
    lateinit var et_email: EditText
    lateinit var et_password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) { }

        btn_login = findViewById(R.id.btn_login)
        tv_signup = findViewById(R.id.tv_signup)
        tv_recovery = findViewById(R.id.tv_recovery)
        et_email = findViewById(R.id.et_email_login)
        et_password = findViewById(R.id.et_password_login)

        btn_login.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        tv_signup.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tv_recovery.setOnClickListener {
            var intent = Intent(this, PasswordRecoveryActivity::class.java)
            startActivity(intent)
        }
    }
}
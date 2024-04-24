package angulo.javier.myfinpal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    lateinit var et_email : EditText
    lateinit var et_password1 : EditText
    lateinit var et_password2 : EditText
    lateinit var btn_register : Button

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

        btn_register.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
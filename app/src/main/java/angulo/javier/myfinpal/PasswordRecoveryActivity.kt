package angulo.javier.myfinpal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PasswordRecoveryActivity : AppCompatActivity() {
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

        btn_recovery.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
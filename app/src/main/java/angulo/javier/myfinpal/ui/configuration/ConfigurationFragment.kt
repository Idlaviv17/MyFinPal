package angulo.javier.myfinpal.ui.configuration

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import angulo.javier.myfinpal.LoginActivity
import angulo.javier.myfinpal.databinding.FragmentConfigurationBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ConfigurationFragment : Fragment() {

    private var _binding: FragmentConfigurationBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    lateinit var tvUsername: TextView
    lateinit var btn_logout: Button
    lateinit var userId: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val budgetViewModel =
            ViewModelProvider(this).get(ConfigurationViewModel::class.java)

        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Retrieve the username
        database = Firebase.database.reference
        tvUsername = _binding!!.tvUsernameConfiguration
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        retrieveUsername()

        btn_logout = _binding!!.btnLogout

        auth = Firebase.auth

        btn_logout.setOnClickListener {
            auth.signOut()
            var intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveUsername() {
        val userRef = database.child("users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.child("username").value.toString()
                tvUsername.text = username
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseError", "Error retrieving username: ${databaseError.message}")
            }
        })
    }
}
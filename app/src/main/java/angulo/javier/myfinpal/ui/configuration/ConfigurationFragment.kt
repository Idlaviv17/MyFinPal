package angulo.javier.myfinpal.ui.configuration

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import angulo.javier.myfinpal.LoginActivity
import android.text.InputType
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.marginLeft
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.databinding.FragmentConfigurationBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
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

        // Show AlertDialog when "Change Password" TextView is clicked
        val tvChangePassword = _binding!!.tvChangePassword
        tvChangePassword.setOnClickListener {
            showChangePasswordDialog()
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

    private fun showChangePasswordDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val etCurrentPassword = EditText(requireContext())
        val etNewPassword = EditText(requireContext())

        etCurrentPassword.hint = "Current Password"
        etCurrentPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        etNewPassword.hint = "New Password"
        etNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        // Adjust margins for the EditTexts
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val margin = resources.getDimensionPixelSize(R.dimen.dialog_edit_text_margin)
        layoutParams.setMargins(margin, margin, margin, margin)
        etCurrentPassword.layoutParams = layoutParams
        etNewPassword.layoutParams = layoutParams

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(etCurrentPassword)
        layout.addView(etNewPassword)

        builder.setView(layout)
            .setTitle("Change Password")
            .setPositiveButton("Change") { dialog, _ ->
                val currentPassword = etCurrentPassword.text.toString().trim()
                val newPassword = etNewPassword.text.toString().trim()

                if (currentPassword.isNotEmpty() && newPassword.isNotEmpty() && newPassword.length >= 6) {
                    // Check the current password against the user's actual password
                    val user = FirebaseAuth.getInstance().currentUser
                    val credential = EmailAuthProvider.getCredential(user!!.email!!, currentPassword)
                    user.reauthenticate(credential)
                        .addOnCompleteListener { reAuthTask ->
                            if (reAuthTask.isSuccessful) {
                                changePassword(newPassword)
                            } else {
                                Toast.makeText(context, "Incorrect current password", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Please enter a valid current and new password", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()

        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_corners)

        alertDialog.show()

        //alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    private fun changePassword(newPassword: String) {

        if (newPassword.isNullOrEmpty() || newPassword.length < 6) {
            Toast.makeText(context, "Password needs to be at least 6 characters long.", Toast.LENGTH_SHORT).show()
            return
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ChangePassword", "Password updated successfully")
                    // Show confirmation message
                    Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ChangePassword", "Failed to update password: ${task.exception}")
                    // Show error message
                    Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
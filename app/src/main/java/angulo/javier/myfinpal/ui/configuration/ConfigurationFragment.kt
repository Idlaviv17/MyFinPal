package angulo.javier.myfinpal.ui.configuration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import angulo.javier.myfinpal.LoginActivity
import angulo.javier.myfinpal.databinding.FragmentConfigurationBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class ConfigurationFragment : Fragment() {

    private var _binding: FragmentConfigurationBinding? = null
    private lateinit var auth : FirebaseAuth
    lateinit var btn_logout: Button

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
}
package angulo.javier.myfinpal.ui.history

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import angulo.javier.myfinpal.Dao.PaymentDAO
import angulo.javier.myfinpal.databinding.FragmentHistoryBinding
import angulo.javier.myfinpal.domain.Payment
import angulo.javier.myfinpal.ui.adapter.PaymentAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val budgetViewModel =
            ViewModelProvider(this)[HistoryViewModel::class.java]

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = Firebase.database.reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        val paymentsRv = binding.paymentsRv
        paymentsRv.layoutManager = LinearLayoutManager(requireContext())

        PaymentDAO().getPayments(userId) { retrievedPayments ->
            val reversedPayments = retrievedPayments.reversed()
            val adapter = PaymentAdapter(reversedPayments) { payment ->
                val action = HistoryFragmentDirections.actionNavigationHistoryToNavigationHistoryDetail(payment)
                findNavController().navigate(action)
            }

            paymentsRv.adapter = adapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
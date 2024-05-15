package angulo.javier.myfinpal.ui.history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var paymentsRv: RecyclerView
    private var payments: List<Payment> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = Firebase.database.reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        paymentsRv = binding.paymentsRv
        paymentsRv.layoutManager = LinearLayoutManager(requireContext())

        PaymentDAO().getPayments(userId) { retrievedPayments ->
            val reversedPayments = retrievedPayments.reversed()
            payments = reversedPayments
            val adapter = PaymentAdapter(reversedPayments) { payment ->
                findNavController().navigate(
                    HistoryFragmentDirections.actionNavigationHistoryToNavigationHistoryDetail(payment)
                )
            }

            paymentsRv.adapter = adapter
        }

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(query: Editable?) { filterQuery(query.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return root
    }

    private fun filterQuery(query: String) {
        val filteredPayments = payments.filter { payment -> payment.title.contains(query, ignoreCase = true) }
        val adapter = PaymentAdapter(filteredPayments) { payment ->
            findNavController().navigate(
                HistoryFragmentDirections.actionNavigationHistoryToNavigationHistoryDetail(payment)
            )
        }
        paymentsRv.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        binding.searchEt.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
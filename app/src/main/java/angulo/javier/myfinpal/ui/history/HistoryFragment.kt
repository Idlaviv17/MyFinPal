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
import angulo.javier.myfinpal.util.Categories
import angulo.javier.myfinpal.domain.Payment
import angulo.javier.myfinpal.ui.adapter.PaymentAdapter
import angulo.javier.myfinpal.util.PaymentMethods
import angulo.javier.myfinpal.databinding.FragmentHistoryBinding
import java.time.LocalDate

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

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

        val payments = binding.payments
        payments.layoutManager = LinearLayoutManager(requireContext())

        val paymentsList = listOf(
            Payment("Membresía Netflix", "Netflix",
                PaymentMethods.CREDIT_CARD, Categories.MEMBERSHIPS, 29.90f, LocalDate.of(2024, 4, 2), "Description 1"),
            Payment("Groceries", "Walmart", PaymentMethods.DEBIT_CARD, Categories.FOOD, 50.0f, LocalDate.of(2023, 3, 20), "Description 2"),
            Payment("Shopping", "Liverpool", PaymentMethods.CASH, Categories.SHOPPING, 100.0f, LocalDate.of(2023, 4, 10), "Description 3")
        )

        // Create the PaymentAdapter with a lambda function to handle item clicks
        val adapter = PaymentAdapter(paymentsList) { payment ->
            // Handle click event, for example, navigate to payment details fragment
            val action = HistoryFragmentDirections.actionNavigationHistoryToNavigationHistoryDetail(payment)
            findNavController().navigate(action)
            }

        payments.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
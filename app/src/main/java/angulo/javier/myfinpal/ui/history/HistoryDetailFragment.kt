package angulo.javier.myfinpal.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.Dao.PaymentDAO
import angulo.javier.myfinpal.databinding.FragmentHistoryDetailBinding
import angulo.javier.myfinpal.ui.new_record.NewRecordFragmentDirections
import angulo.javier.myfinpal.util.IconHandler
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class HistoryDetailFragment : Fragment() {

    private var _binding: FragmentHistoryDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args = HistoryDetailFragmentArgs.fromBundle(requireArguments())
        val payment = args.payment

        database = Firebase.database.reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        binding.returnBtn.setOnClickListener {
            val action = HistoryDetailFragmentDirections.actionNavigationHistoryDetailToNavigationHistory()
            findNavController().navigate(action)
        }

        binding.deleteBtn.setOnClickListener {
            PaymentDAO().deletePayment(userId, payment.uid, ) { databaseError, _ ->
                if (databaseError == null) {
                    Toast.makeText(requireContext(), "Payment deleted successfully", Toast.LENGTH_SHORT).show()
                    val action = HistoryDetailFragmentDirections.actionNavigationHistoryDetailToNavigationHistory()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Failed to delete payment. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val formattedAmount = "$${payment.amount}"

        IconHandler.setIcon(payment.category, binding.iconBg, binding.icon)
        binding.paymentTitleTv.text = payment.title
        binding.paymentMethodTv.text = payment.method
        binding.categoryTv.text = payment.category
        binding.amountTv.text = formattedAmount
        binding.dateTv.text = payment.date.toString()
        binding.descriptionTv.text = payment.description

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
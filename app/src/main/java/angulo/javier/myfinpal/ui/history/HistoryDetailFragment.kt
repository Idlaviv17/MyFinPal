package angulo.javier.myfinpal.ui.history

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.Dao.BudgetDAO
import angulo.javier.myfinpal.Dao.PaymentDAO
import angulo.javier.myfinpal.databinding.FragmentHistoryDetailBinding
import angulo.javier.myfinpal.domain.Budget
import angulo.javier.myfinpal.domain.Payment
import angulo.javier.myfinpal.util.Categories
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

    private lateinit var payment: Payment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args = HistoryDetailFragmentArgs.fromBundle(requireArguments())
        payment = args.payment

        database = Firebase.database.reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        binding.returnBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.deleteBtn.setOnClickListener {
            deletePayment()
            updateBudget()
        }

        binding.updateBtn.setOnClickListener {
            val action = HistoryDetailFragmentDirections.actionNavigationHistoryDetailToNavigationUpdateRecord(payment)
            findNavController().navigate(action)
        }

        val formattedAmount = if (payment.amount % 1 == 0.0f) {
            "$" + payment.amount.toInt().toString()
        } else {
            "$" + payment.amount.toString()
        }

        IconHandler.setIcon(payment.category, binding.iconBg, binding.icon)
        binding.paymentTitleTv.text = payment.title
        binding.recipientTv.text = payment.title
        binding.paymentMethodTv.text = payment.method
        binding.categoryTv.text = payment.category
        binding.amountTv.text = formattedAmount
        binding.dateTv.text = payment.date
        binding.descriptionTv.text = payment.description

        return root
    }

    private fun deletePayment() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Delete Payment")
            setMessage("Are you sure you want to delete this payment?")
            setPositiveButton("Delete") { dialog, _ ->
                PaymentDAO().deletePayment(userId, payment.uid) { databaseError, _ ->
                    if (databaseError == null) {
                        Toast.makeText(requireContext(), "Payment deleted successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete payment. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun updateBudget() {
        val amount = payment.amount

        BudgetDAO().readUserBudget(userId) { dataSnapshot ->
            dataSnapshot.getValue(Budget::class.java)?.let { budget ->
                budget.spendBudget -= amount

                when (payment.category) {
                    Categories.ACTIVITIES.stringValue -> budget.activities -= amount
                    Categories.FOOD.stringValue -> budget.food -= amount
                    Categories.HEALTH.stringValue -> budget.health -= amount
                    Categories.MEMBERSHIPS.stringValue -> budget.memberships -= amount
                    Categories.SERVICES.stringValue -> budget.service -= amount
                    Categories.SHOPPING.stringValue -> budget.shopping -= amount
                }

                BudgetDAO().updateUserBudget(userId, budget)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
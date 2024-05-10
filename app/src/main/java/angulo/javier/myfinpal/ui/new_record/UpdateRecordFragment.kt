package angulo.javier.myfinpal.ui.new_record

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.Dao.BudgetDAO
import angulo.javier.myfinpal.Dao.PaymentDAO
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.databinding.FragmentUpdateRecordBinding
import angulo.javier.myfinpal.domain.Budget
import angulo.javier.myfinpal.domain.Payment
import angulo.javier.myfinpal.ui.history.HistoryDetailFragmentArgs
import angulo.javier.myfinpal.util.Categories
import angulo.javier.myfinpal.util.PaymentMethods
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Calendar.*
import java.util.Locale

class UpdateRecordFragment : Fragment() {

    private var _binding: FragmentUpdateRecordBinding? = null
    private val binding get() = _binding!!

    private lateinit var payment: Payment
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    private lateinit var recipient: String
    private lateinit var paymentMethod: String
    private lateinit var formCategory: String
    private var actualAmount: Float = 0.0f
    private var formAmount: Float = 0.0f
    private lateinit var dateString: String
    private lateinit var formDescription: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args = HistoryDetailFragmentArgs.fromBundle(requireArguments())
        payment = args.payment
        actualAmount = payment.amount

        database = Firebase.database.reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        setupSpinners()
        loadPayment()
        limitDecimalPlaces(binding.amountEt, 10, 2)

        binding.dateEt.setOnClickListener { showDatePicker() }
        binding.cancelBtn.setOnClickListener { findNavController().popBackStack() }
        binding.updateBtn.setOnClickListener {
            updatePayment()
            updateBudget()
        }

        return root
    }

    private fun setupSpinners() {
        setupSpinner(binding.paymentMethodsSpinner, R.array.payment_methods)
        setupSpinner(binding.categoriesSpinner, R.array.categories)
    }

    private fun setupSpinner(spinner: Spinner, itemsArrayResourceId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            itemsArrayResourceId,
            R.layout.spinner_item_text
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter.sort { item1, item2 -> item1.toString().compareTo(item2.toString()) }
        spinner.adapter = adapter
    }

    private fun loadPayment() {
        binding.apply {
            recipientEt.setText(payment.title)
            when(payment.method) {
                PaymentMethods.BANK_TRANSFER.stringValue -> { paymentMethodsSpinner.setSelection(0) }
                PaymentMethods.CASH.stringValue -> { paymentMethodsSpinner.setSelection(1) }
                PaymentMethods.CREDIT_CARD.stringValue -> { paymentMethodsSpinner.setSelection(2) }
                PaymentMethods.DEBIT_CARD.stringValue -> { paymentMethodsSpinner.setSelection(3) }
                PaymentMethods.PAYPAL.stringValue -> { paymentMethodsSpinner.setSelection(4) }
            }

            when(payment.category) {
                Categories.ACTIVITIES.stringValue -> { categoriesSpinner.setSelection(0) }
                Categories.FOOD.stringValue -> { categoriesSpinner.setSelection(1) }
                Categories.HEALTH.stringValue -> { categoriesSpinner.setSelection(2) }
                Categories.MEMBERSHIPS.stringValue -> { categoriesSpinner.setSelection(3) }
                Categories.RESTAURANTS.stringValue -> { categoriesSpinner.setSelection(4) }
                Categories.SHOPPING.stringValue -> { categoriesSpinner.setSelection(5) }
            }

            val formattedAmount = if (payment.amount % 1 == 0.0f) {
                payment.amount.toInt().toString()
            } else {
                payment.amount.toString()
            }
            amountEt.setText(formattedAmount)

            dateEt.setText(payment.date)
            descriptionEt.setText(payment.description)
        }
    }

    private fun updatePayment() {
        recipient = binding.recipientEt.text.toString()
        paymentMethod = binding.paymentMethodsSpinner.selectedItem.toString()
        formCategory = binding.categoriesSpinner.selectedItem.toString()
        formAmount = binding.amountEt.text.toString().toFloatOrNull() ?: 0f
        dateString = binding.dateEt.text.toString()
        formDescription = binding.descriptionEt.text.toString()

        if (hasChanges() && isValid()) {
            payment.apply {
                title = recipient
                method = paymentMethod
                payment.category = formCategory
                payment.amount = formAmount
                date = dateString
                payment.description = formDescription
            }

            PaymentDAO().updatePayment(userId, payment.uid, payment) { databaseError, _ ->
                if (databaseError == null) {
                    Toast.makeText(requireContext(), "Payment updated successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    clearForm()
                } else {
                    Toast.makeText(requireContext(), "Failed to update payment. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateBudget() {
        val amount = payment.amount

        BudgetDAO().readUserBudget(userId) { dataSnapshot ->
            dataSnapshot.getValue(Budget::class.java)?.let { budget ->
                val budgetChange = amount - actualAmount
                budget.spendBudget += budgetChange

                when (payment.category) {
                    Categories.ACTIVITIES.stringValue -> budget.activities += budgetChange
                    Categories.FOOD.stringValue -> budget.food += budgetChange
                    Categories.HEALTH.stringValue -> budget.health += budgetChange
                    Categories.MEMBERSHIPS.stringValue -> budget.memberships += budgetChange
                    Categories.RESTAURANTS.stringValue -> budget.restaurants += budgetChange
                    Categories.SHOPPING.stringValue -> budget.shopping += budgetChange
                }

                BudgetDAO().updateUserBudget(userId, budget)
            }
        }
    }

    private fun hasChanges(): Boolean {
        return recipient != payment.title ||
                paymentMethod != payment.method ||
                formCategory != payment.category ||
                formAmount != payment.amount ||
                dateString != payment.date ||
                formDescription != payment.description
    }

    private fun isValid(): Boolean {
        binding.apply {
            if (recipient.isEmpty()) {
                recipientEt.error = "Recipient cannot be empty"
                return false
            }
            if (paymentMethod.isEmpty()) {
                Toast.makeText(requireContext(), "Payment method cannot be empty", Toast.LENGTH_SHORT).show()
                return false
            }
            if (formCategory.isEmpty()) {
                Toast.makeText(requireContext(), "Category cannot be empty", Toast.LENGTH_SHORT).show()
                return false
            }
            if (formAmount <= 0f) {
                amountEt.error = "Invalid amount"
                return false
            }
            if (dateString.isEmpty()) {
                dateEt.error = "Date cannot be empty"
                return false
            }
        }
        return true
    }

    private fun clearForm() {
        binding.apply {
            recipientEt.setText("")
            paymentMethodsSpinner.setSelection(-1)
            categoriesSpinner.setSelection(-1)
            amountEt.setText("")
            dateEt.setText("")
            descriptionEt.setText("")
        }
    }

    private fun showDatePicker() {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val currentDate = getInstance()
        currentDate.time = dateFormat.parse(binding.dateEt.text.toString()) ?: currentDate.time

        val year = currentDate.get(YEAR)
        val month = currentDate.get(MONTH)
        val day = currentDate.get(DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                binding.dateEt.setText(dateFormat.format(selectedDate.time))
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun limitDecimalPlaces(editText: EditText, maxDigitsBeforeDecimalPoint: Int, maxDigitsAfterDecimalPoint: Int) {
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val builder = StringBuilder(dest)
            builder.replace(dstart, dend, source?.subSequence(start, end).toString())
            if (!builder.toString().matches(Regex("^\\d{0,$maxDigitsBeforeDecimalPoint}(\\.\\d{0,$maxDigitsAfterDecimalPoint})?$"))) {
                if (source!!.isEmpty()) dest?.subSequence(dstart, dend) else ""
            } else null
        }
        editText.filters = arrayOf(filter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package angulo.javier.myfinpal.ui.new_record

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.Dao.PaymentDAO
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.databinding.FragmentNewRecordBinding
import angulo.javier.myfinpal.domain.Payment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Calendar.*
import java.util.Locale

class NewRecordFragment : Fragment() {

    private var _binding: FragmentNewRecordBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    private lateinit var recipient: String
    private lateinit var paymentMethod: String
    private lateinit var category: String
    private var amount: Float = 0.0f
    private lateinit var dateString: String
    private lateinit var description: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = Firebase.database.reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        //(binding.establishmentsSpinner, R.array.establishments)
        setupSpinner(binding.paymentMethodsSpinner, R.array.payment_methods)
        setupSpinner(binding.categoriesSpinner, R.array.categories)

        binding.dateEt.setOnClickListener {
            showDatePicker()
        }

        binding.cancelBtn.setOnClickListener {
            val action = NewRecordFragmentDirections.actionNavigationNewRecordToNavigationHome()
            findNavController().navigate(action)
        }

        binding.acceptBtn.setOnClickListener {
            addPayment()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addPayment() {
        recipient = binding.recipientEt.text.toString()
        paymentMethod = binding.paymentMethodsSpinner.selectedItem.toString()
        category = binding.categoriesSpinner.selectedItem.toString()
        amount = binding.amountEt.text.toString().toFloatOrNull() ?: 0f
        dateString = binding.dateEt.text.toString()
        description = binding.descriptionEt.text.toString()

        if(validate()) {
            //val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            //val date: LocalDate = LocalDate.parse(dateString, dateFormat)

            val payment = Payment(
                title = recipient,
                method = paymentMethod,
                category = category,
                amount = amount,
                date = dateString,
                description = description
            )

            PaymentDAO().addPayment(userId, payment) { databaseError, _ ->
                if (databaseError == null) {
                    Toast.makeText(requireContext(), "Payment added successfully", Toast.LENGTH_SHORT).show()
                    clearForm()

                    val action = NewRecordFragmentDirections.actionNavigationNewRecordToNavigationHistory()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Failed to add payment. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (recipient.isEmpty()) {
            binding.recipientEt.error = "Recipient cannot be empty"
            return false
        }

        if (paymentMethod.isEmpty()) {
            Toast.makeText(requireContext(), "Payment method cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (category.isEmpty()) {
            Toast.makeText(requireContext(), "Category cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }

        if (amount.toString().isEmpty()) {
            binding.amountEt.error = "Amount cannot be empty"
            return false
        } else {
            if (amount <= 0f) {
                binding.amountEt.error = "Invalid amount"
                return false
            }
        }

        if (dateString.isEmpty()) {
            binding.dateEt.error = "Date cannot be empty"
            return false
        }

        return true
    }

    private fun clearForm() {
        binding.recipientEt.setText("")
        binding.paymentMethodsSpinner.setSelection(-1)
        binding.categoriesSpinner.setSelection(-1)
        binding.amountEt.setText("")
        binding.dateEt.setText("")
        binding.descriptionEt.setText("")
    }

    private fun setupSpinner(spinner: Spinner, itemsArrayResourceId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            itemsArrayResourceId,
            R.layout.spinner_item_text
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adapter.sort { item1, item2 ->
            item1.toString().compareTo(item2.toString())
        }

        spinner.adapter = adapter
    }

    private fun showDatePicker() {
        val currentDate = getInstance()
        val year = currentDate.get(YEAR)
        val month = currentDate.get(MONTH)
        val day = currentDate.get(DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                binding.dateEt.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
}
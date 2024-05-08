package angulo.javier.myfinpal.ui.budget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.Dao.BudgetDAO
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.databinding.FragmentBudgetBinding
import angulo.javier.myfinpal.databinding.FragmentBudgetEditBinding
import angulo.javier.myfinpal.domain.Budget
import angulo.javier.myfinpal.ui.home.HomeFragmentDirections
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BudgetEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentBudgetEditBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null

    private val budgetDao = BudgetDAO()
    private lateinit var database: DatabaseReference
    lateinit var userId: String

    private lateinit var textBudgetMenuFoodNumber: EditText
    private lateinit var textBudgetMenuShoppingNumber: EditText
    private lateinit var textBudgetMenuHealthNumber: EditText
    private lateinit var textBudgetMenuActivitiesNumber: EditText
    private lateinit var textBudgetMenuMembershipNumber: EditText
    private lateinit var textBudgetMenuRestaurantNumber: EditText

    private lateinit var textBudgetToSpend: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetEditBinding.inflate(inflater, container, false)
        val root: View = binding.root

        database = Firebase.database.reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Initialize EditText and TextView variables
        textBudgetMenuFoodNumber = binding.textBudgetMenuFoodNumber
        textBudgetMenuShoppingNumber = binding.textBudgetMenuShoppingNumber
        textBudgetMenuHealthNumber = binding.textBudgetMenuHealthNumber
        textBudgetMenuActivitiesNumber = binding.textBudgetMenuActivitiesNumber
        textBudgetMenuMembershipNumber = binding.textBudgetMenuMemberNumber
        textBudgetMenuRestaurantNumber = binding.textBudgetMenuRestaurantsNumber
        textBudgetToSpend = binding.textBudgetToSpend

        val budgetMenuButtonRestore = binding.budgetMenuButtonRestore
        val budgetMenuButtonAccept = binding.budgetMenuButtonAccept
        val budgetMenuButtonCancel = binding.budgetMenuButtonCancel

        // Set up TextWatcher
        textBudgetMenuFoodNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuShoppingNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuHealthNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuActivitiesNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuMembershipNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuRestaurantNumber.addTextChangedListener(TextWatcherListener())

        // Set up button click listeners
        val budgetMenuFoodButtonDown = binding.budgetMenuFoodButtonDown
        val budgetMenuFoodButtonUp = binding.budgetMenuFoodButtonUp
        val budgetMenuShoppingButtonDown = binding.budgetMenuShoppingButtonDown
        val budgetMenuShoppingButtonUp = binding.budgetMenuShoppingButtonUp
        val budgetMenuHealthButtonDown = binding.budgetMenuHealthButtonDown
        val budgetMenuHealthButtonUp = binding.budgetMenuHealthButtonUp
        val budgetMenuActivitiesButtonDown = binding.budgetMenuActivitiesButtonDown
        val budgetMenuActivitiesButtonUp = binding.budgetMenuActivitiesButtonUp
        val budgetMenuMembershipButtonDown = binding.budgetMenuMembershipButtonDown
        val budgetMenuMembershipButtonUp = binding.budgetMenuMembershipButtonUp
        val budgetMenuRestaurantButtonDown = binding.budgetMenuRestaurantsButtonDown
        val budgetMenuRestaurantButtonUp = binding.budgetMenuRestaurantsButtonUp


        budgetMenuFoodButtonDown.setOnClickListener { decreaseBudgetValue(textBudgetMenuFoodNumber) }
        budgetMenuFoodButtonUp.setOnClickListener { increaseBudgetValue(textBudgetMenuFoodNumber) }
        budgetMenuShoppingButtonDown.setOnClickListener { decreaseBudgetValue(textBudgetMenuShoppingNumber) }
        budgetMenuShoppingButtonUp.setOnClickListener { increaseBudgetValue(textBudgetMenuShoppingNumber) }
        budgetMenuHealthButtonDown.setOnClickListener { decreaseBudgetValue(textBudgetMenuHealthNumber) }
        budgetMenuHealthButtonUp.setOnClickListener { increaseBudgetValue(textBudgetMenuHealthNumber) }
        budgetMenuActivitiesButtonDown.setOnClickListener { decreaseBudgetValue(textBudgetMenuActivitiesNumber) }
        budgetMenuActivitiesButtonUp.setOnClickListener { increaseBudgetValue(textBudgetMenuActivitiesNumber) }
        budgetMenuMembershipButtonDown.setOnClickListener { decreaseBudgetValue(textBudgetMenuMembershipNumber) }
        budgetMenuMembershipButtonUp.setOnClickListener { increaseBudgetValue(textBudgetMenuMembershipNumber) }
        budgetMenuRestaurantButtonDown.setOnClickListener { decreaseBudgetValue(textBudgetMenuRestaurantNumber) }
        budgetMenuRestaurantButtonUp.setOnClickListener { increaseBudgetValue(textBudgetMenuRestaurantNumber) }

        budgetMenuButtonRestore.setOnClickListener {
            textBudgetMenuFoodNumber.setText("")
            textBudgetMenuShoppingNumber.setText("")
            textBudgetMenuHealthNumber.setText("")
            textBudgetMenuActivitiesNumber.setText("")
            textBudgetMenuMembershipNumber.setText("")
            textBudgetMenuRestaurantNumber.setText("")
        }

        budgetMenuButtonAccept.setOnClickListener{
            updateBudgetInfo()
            val action = BudgetEditFragmentDirections.actionNavigationBudgetEditToNavigationBudget()
            findNavController().navigate(action)
        }

        budgetMenuButtonCancel.setOnClickListener{
            val action = BudgetEditFragmentDirections.actionNavigationBudgetEditToNavigationBudget()
            findNavController().navigate(action)
        }

        return root
    }

    private fun decreaseBudgetValue(editText: EditText) {
        val text = editText.text.toString()

        if (text.toString().isNullOrEmpty()) {
            editText.setText("00")
            return
        }

        val currentValue = text.toDouble()

        val newValue: Double = currentValue - 10

        if (newValue <= 0){
            editText.setText("00")
            return
        }

        editText.setText(newValue.toString())
        cancelBudget()
    }

    private fun increaseBudgetValue(editText: EditText) {
        val text = editText.text.toString()

        if (text.toString().isNullOrEmpty()) {
            editText.setText("00")
            return
        }

        val currentValue = text.toDouble()
        val newValue = currentValue + 10

        editText.setText(newValue.toString())

        cancelBudget()
    }

    private fun cancelBudget() {
        val totalBudget = (textBudgetMenuFoodNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuShoppingNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuHealthNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuActivitiesNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuMembershipNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuRestaurantNumber.text.toString().toDoubleOrNull() ?: 0.0)

        textBudgetToSpend.text = totalBudget.toString()
    }

    private fun updateBudgetInfo(){
        val foodNumber = textBudgetMenuFoodNumber.text.toString().toDoubleOrNull() ?: 0.0
        val shoppingNumber = textBudgetMenuShoppingNumber.text.toString().toDoubleOrNull() ?: 0.0
        val healthNumber = textBudgetMenuHealthNumber.text.toString().toDoubleOrNull() ?: 0.0
        val activitiesNumber = textBudgetMenuActivitiesNumber.text.toString().toDoubleOrNull() ?: 0.0
        val membershipNumber = textBudgetMenuMembershipNumber.text.toString().toDoubleOrNull() ?: 0.0
        val restaurantNumber = textBudgetMenuRestaurantNumber.text.toString().toDoubleOrNull() ?: 0.0
        val budgetLimit = textBudgetToSpend.text.toString().toDoubleOrNull() ?: 0.0

        //val args = BudgetEditFragmentArgs.fromBundle(requireArguments())
        //val budget = args.budget
        val budget = arguments?.getSerializable("budget") as? Budget

        val budgetCopy = budget?.copy(
            budgetLimit = budgetLimit,
            foodLimit = foodNumber,
            shoppingLimit = shoppingNumber,
            healthLimit = healthNumber,
            activitiesLimit = activitiesNumber,
            membershipsLimit = membershipNumber,
            restaurantsLimit = restaurantNumber
        )
        if (budgetCopy != null) {
            budgetDao.updateUserBudget(userId, budgetCopy)
                .addOnSuccessListener {
                    Log.d("Firebase", "Data captured correctly.")
                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Error capturing data: ${exception.message}")
                }
        }
    }

    private inner  class TextWatcherListener : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.let { newText ->
                if (newText.startsWith("00") && newText.length > 1 && !newText.startsWith("0.")) {
                    s.delete(0, 1)
                }

                if (newText.contains(".") && newText.lastIndexOf(".") != newText.indexOf(".")) {
                    s?.delete(s.length - 1, s.length)
                }
                cancelBudget()
            }
        }
    }

}
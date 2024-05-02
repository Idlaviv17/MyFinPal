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
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BudgetEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BudgetEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


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
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_budget_edit, container, false)
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_budget_edit, container, false)

        database = Firebase.database.reference
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Initialize EditText and TextView variables
        textBudgetMenuFoodNumber = rootView.findViewById(R.id.textBudgetMenuFoodNumber)
        textBudgetMenuShoppingNumber = rootView.findViewById(R.id.textBudgetMenuShoppingNumber)
        textBudgetMenuHealthNumber = rootView.findViewById(R.id.textBudgetMenuHealthNumber)
        textBudgetMenuActivitiesNumber = rootView.findViewById(R.id.textBudgetMenuActivitiesNumber)
        textBudgetMenuMembershipNumber = rootView.findViewById(R.id.textBudgetMenuMemberNumber)
        textBudgetMenuRestaurantNumber = rootView.findViewById(R.id.textBudgetMenuRestaurantsNumber)
        textBudgetToSpend = rootView.findViewById(R.id.textBudgetToSpend)

        val budgetMenuButtonRestore = rootView.findViewById<ImageView>(R.id.budgetMenuButtonRestore)
        val budgetMenuButtonAccept = rootView.findViewById<ImageView>(R.id.budgetMenuButtonAccept)
        val budgetMenuButtonCancel = rootView.findViewById<ImageView>(R.id.budgetMenuButtonCancel)

        // Set up TextWatcher
        textBudgetMenuFoodNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuShoppingNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuHealthNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuActivitiesNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuMembershipNumber.addTextChangedListener(TextWatcherListener())
        textBudgetMenuRestaurantNumber.addTextChangedListener(TextWatcherListener())

        // Set up button click listeners
        val budgetMenuFoodButtonDown = rootView.findViewById<ImageView>(R.id.budgetMenuFoodButtonDown)
        val budgetMenuFoodButtonUp = rootView.findViewById<ImageView>(R.id.budgetMenuFoodButtonUp)
        val budgetMenuShoppingButtonDown = rootView.findViewById<ImageView>(R.id.budgetMenuShoppingButtonDown)
        val budgetMenuShoppingButtonUp = rootView.findViewById<ImageView>(R.id.budgetMenuShoppingButtonUp)
        val budgetMenuHealthButtonDown = rootView.findViewById<ImageView>(R.id.budgetMenuHealthButtonDown)
        val budgetMenuHealthButtonUp = rootView.findViewById<ImageView>(R.id.budgetMenuHealthButtonUp)
        val budgetMenuActivitiesButtonDown = rootView.findViewById<ImageView>(R.id.budgetMenuActivitiesButtonDown)
        val budgetMenuActivitiesButtonUp = rootView.findViewById<ImageView>(R.id.budgetMenuActivitiesButtonUp)
        val budgetMenuMembershipButtonDown = rootView.findViewById<ImageView>(R.id.budgetMenuMembershipButtonDown)
        val budgetMenuMembershipButtonUp = rootView.findViewById<ImageView>(R.id.budgetMenuMembershipButtonUp)
        val budgetMenuRestaurantButtonDown = rootView.findViewById<ImageView>(R.id.budgetMenuRestaurantsButtonDown)
        val budgetMenuRestaurantButtonUp = rootView.findViewById<ImageView>(R.id.budgetMenuRestaurantsButtonUp)


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
            val navController = findNavController()
            updateBudgetInfo()
            navController.navigate(R.id.navigation_budget)
        }

        budgetMenuButtonCancel.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.navigation_budget)
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BudgetEdit.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BudgetEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
        updateBudgetToSpend()
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

        updateBudgetToSpend()
    }

    private fun updateBudgetToSpend() {
        val totalBudget = (textBudgetMenuFoodNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuShoppingNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuHealthNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuActivitiesNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuMembershipNumber.text.toString().toDoubleOrNull() ?: 0.0) +
                (textBudgetMenuRestaurantNumber.text.toString().toDoubleOrNull() ?: 0.0)

        textBudgetToSpend.text = totalBudget.toString()
    }

    private fun updateBudgetInfo(){
        val userRef = database.child("users").child(userId)

        val foodNumber = textBudgetMenuFoodNumber.text.toString().toDoubleOrNull() ?: 0.0
        val shoppingNumber = textBudgetMenuShoppingNumber.text.toString().toDoubleOrNull() ?: 0.0
        val healthNumber = textBudgetMenuHealthNumber.text.toString().toDoubleOrNull() ?: 0.0
        val activitiesNumber = textBudgetMenuActivitiesNumber.text.toString().toDoubleOrNull() ?: 0.0
        val membershipNumber = textBudgetMenuMembershipNumber.text.toString().toDoubleOrNull() ?: 0.0
        val restaurantNumber = textBudgetMenuRestaurantNumber.text.toString().toDoubleOrNull() ?: 0.0
        val budgetToSpend = textBudgetToSpend.text.toString().toDoubleOrNull() ?: 0.0

        val newDataMap = HashMap<String, Any>()
        newDataMap["food"] = foodNumber
        newDataMap["shopping"] = shoppingNumber
        newDataMap["health"] = healthNumber
        newDataMap["activities"] = activitiesNumber
        newDataMap["membership"] = membershipNumber
        newDataMap["restaurants"] = restaurantNumber
        newDataMap["budgetLimit"] = budgetToSpend

        userRef.child("budget").updateChildren(newDataMap)
            .addOnSuccessListener {
                // Manejar el éxito de la actualización
                Log.d("Firebase", "Data captured correctly.")
            }
            .addOnFailureListener { exception ->
                // Manejar el fallo de la actualización
                Log.e("Firebase", "Error capturing data: ${exception.message}")
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
                updateBudgetToSpend()
            }
        }
    }

}
package angulo.javier.myfinpal.ui.budget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import angulo.javier.myfinpal.R

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

        val textBudgetMenuFoodNumber = rootView.findViewById<EditText>(R.id.textBudgetMenuFoodNumber)
        val textBudgetMenuShoppingNumber = rootView.findViewById<EditText>(R.id.textBudgetMenuShoppingNumber)
        val textBudgetMenuHealthNumber = rootView.findViewById<EditText>(R.id.textBudgetMenuHealthNumber)
        val textBudgetMenuActivitiesNumber = rootView.findViewById<EditText>(R.id.textBudgetMenuActivitiesNumber)
        val textBudgetMenuMembershipNumber = rootView.findViewById<EditText>(R.id.textBudgetMenuMemberNumber)
        val textBudgetMenuRestaurantNumber = rootView.findViewById<EditText>(R.id.textBudgetMenuRestaurantsNumber)

        val budgetMenuButtonRestore = rootView.findViewById<ImageView>(R.id.budgetMenuButtonRestore)

        textBudgetMenuFoodNumber.addTextChangedListener(RemoveLeadingZerosWatcher())
        textBudgetMenuShoppingNumber.addTextChangedListener(RemoveLeadingZerosWatcher())
        textBudgetMenuHealthNumber.addTextChangedListener(RemoveLeadingZerosWatcher())
        textBudgetMenuActivitiesNumber.addTextChangedListener(RemoveLeadingZerosWatcher())
        textBudgetMenuMembershipNumber.addTextChangedListener(RemoveLeadingZerosWatcher())
        textBudgetMenuRestaurantNumber.addTextChangedListener(RemoveLeadingZerosWatcher())

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
            // Establecer el texto de cada EditText en una cadena vacía
            textBudgetMenuFoodNumber.setText("")
            textBudgetMenuShoppingNumber.setText("")
            textBudgetMenuHealthNumber.setText("")
            textBudgetMenuActivitiesNumber.setText("")
            textBudgetMenuMembershipNumber.setText("")
            textBudgetMenuRestaurantNumber.setText("")
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
        // Get the current text from the TextView
        val text = editText.text.toString()

        if (text.toString().isNullOrEmpty()) {
            editText.setText("00")
            return
        }

        val currentValue = text.toDouble()

        // Decrease the value (for example, by 10)
        val newValue: Double = currentValue - 10

        if (newValue <= 0){
            editText.setText("00")
            return
        }
        // Update the TextView with the new value
        editText.setText(newValue.toString())
    }

    private fun increaseBudgetValue(editText: EditText) {
        // Get the current text from the TextView
        val text = editText.text.toString()

        if (text.toString().isNullOrEmpty()) {
            editText.setText("00")
            return
        }

        val currentValue = text.toDouble()
        // Increase the value (for example, by 10)
        val newValue = currentValue + 10

        // Update the TextView with the new value
        editText.setText(newValue.toString())
    }

    private class RemoveLeadingZerosWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No se necesita hacer nada antes de que el texto cambie.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // No se necesita hacer nada mientras el texto cambia. ...
        }

        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.let { newText ->
                if (newText.startsWith("00") && newText.length > 1 && !newText.startsWith("0.")) {
                    s.delete(0, 1)
                }

                if (newText.contains(".") && newText.lastIndexOf(".") != newText.indexOf(".")) {
                    s?.delete(s.length - 1, s.length)
                }
            }
        }
    }

}
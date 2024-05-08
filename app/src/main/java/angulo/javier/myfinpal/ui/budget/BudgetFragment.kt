package angulo.javier.myfinpal.ui.budget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.Dao.BudgetDAO
import angulo.javier.myfinpal.databinding.FragmentBudgetBinding
import angulo.javier.myfinpal.domain.Budget
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class BudgetFragment : Fragment() {

    private var _binding: FragmentBudgetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val budgetDao = BudgetDAO()

    private lateinit var database: DatabaseReference
    lateinit var userId: String

    lateinit var budgetToSpend: TextView
    lateinit var budgetLimit: TextView

    lateinit var foodBudget: TextView
    lateinit var shoppingBudget: TextView
    lateinit var healthBudget: TextView
    lateinit var activitiesBudget: TextView
    lateinit var membershipsBudget: TextView
    lateinit var restaurantsBudget: TextView

    lateinit var foodBudgetLimit: TextView
    lateinit var shoppingBudgetLimit: TextView
    lateinit var healthBudgetLimit: TextView
    lateinit var activitiesBudgetLimit: TextView
    lateinit var membershipsBudgetLimit: TextView
    lateinit var restaurantsBudgetLimit: TextView

    lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val budgetViewModel =
            ViewModelProvider(this)[BudgetViewModel::class.java]
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnEditBudget.setOnClickListener {
            navigateToEditBudget()
        }

        database = Firebase.database.reference

        budgetLimit = _binding!!.textBudgetLimit
        budgetToSpend = _binding!!.textBudgetToSpend

        foodBudget = _binding!!.textBudgetMenuFoodNumber
        shoppingBudget = _binding!!.textBudgetMenuShoppingNumber
        healthBudget = _binding!!.textBudgetMenuHealthNumber
        activitiesBudget = _binding!!.textBudgetMenuActivitieshNumber
        membershipsBudget = _binding!!.textBudgetMenuMembershipNumber
        restaurantsBudget = _binding!!.textBudgetMenuRestaurantsNumber

        foodBudgetLimit = _binding!!.textBudgetMenuFoodLimitNumber
        shoppingBudgetLimit  = _binding!!.textBudgetMenuShoppingLimitNumber
        healthBudgetLimit  = _binding!!.textBudgetMenuHealthLimitNumber
        activitiesBudgetLimit  = _binding!!.textBudgetMenuActivitiesLimitNumber
        membershipsBudgetLimit  = _binding!!.textBudgetMenuMembershipLimitNumber
        restaurantsBudgetLimit  = _binding!!.textBudgetMenuRestaurantsLimitNumber

        progressBar = _binding!!.progressBar

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        showBudget()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToEditBudget() {
        // Navegación utilizando NavController
        //var spendBudget = budgetToSpend.text.toString()
        budgetDao.readUserBudget(userId) { dataSnapshot ->
            val budget = dataSnapshot.getValue(Budget::class.java)
            //spendBudget = spendBudget.replace(Regex("[^\\d.]"), "")
            budget?.let {
                val action = BudgetFragmentDirections.actionNavigationBudgetToNavigationBudgetEdit(it)
                findNavController().navigate(action)
            }
        }

    }

    private fun showBudget() {
        budgetDao.readUserBudget(userId) { dataSnapshot ->
            val budget = dataSnapshot.getValue(Budget::class.java)
            Log.d("BudgetFragment", "Budget data: $budget")

            budgetLimit.text = textToDoubleFormat(budget?.budgetLimit.toString())
            budgetToSpend.text = textToDoubleFormat(budget?.spendBudget.toString())

            foodBudgetLimit.text = textToDoubleFormat(budget?.foodLimit.toString())
            shoppingBudgetLimit.text = textToDoubleFormat(budget?.shoppingLimit.toString())
            healthBudgetLimit.text = textToDoubleFormat(budget?.healthLimit.toString())
            activitiesBudgetLimit.text = textToDoubleFormat(budget?.activitiesLimit.toString())
            membershipsBudgetLimit.text = textToDoubleFormat(budget?.membershipsLimit.toString())
            restaurantsBudgetLimit.text = textToDoubleFormat(budget?.restaurantsLimit.toString())

            foodBudget.text = textToDoubleFormat(budget?.food.toString())
            shoppingBudget.text = textToDoubleFormat(budget?.shopping.toString())
            healthBudget.text = textToDoubleFormat(budget?.healthLimit.toString())
            activitiesBudget.text = textToDoubleFormat(budget?.activities.toString())
            membershipsBudget.text = textToDoubleFormat(budget?.memberships.toString())
            restaurantsBudget.text = textToDoubleFormat(budget?.restaurants.toString())

            val budgetLimitValue = budget?.budgetLimit?.toString()?.toDoubleOrNull() ?: 0.0
            val spendBudgetValue = budget?.spendBudget?.toString()?.toDoubleOrNull() ?: 0.0

            budgetLimit.text = textToDoubleFormat(budgetLimitValue.toString())
            budgetToSpend.text = textToDoubleFormat(spendBudgetValue.toString())

            val progress = ((spendBudgetValue / budgetLimitValue) * 100)
            Log.d("BudgetFragment", "progress: $progress")
            progressBar.max = 100
            progressBar.progress = progress.toInt()
        }
    }


    private fun textToDoubleFormat(text: String): String {
        val valor: Double = text.toDoubleOrNull() ?: return ""
        return "${'$'}${String.format("%.2f", valor)}"
    }

}
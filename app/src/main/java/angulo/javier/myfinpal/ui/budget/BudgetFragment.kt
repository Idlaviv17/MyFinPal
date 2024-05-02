package angulo.javier.myfinpal.ui.budget

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.databinding.FragmentBudgetBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class BudgetFragment : Fragment() {

    private var _binding: FragmentBudgetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val budgetViewModel =
            ViewModelProvider(this).get(BudgetViewModel::class.java)

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
        val navController = findNavController()
        navController.navigate(R.id.action_navigation_budget_to_navigation_budget_edit)
    }

    private fun showBudget() {
        val userRef = database.child("users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val budgetL = dataSnapshot.child("budget").child("budgetLimit").value.toString()
                val budgetTS = dataSnapshot.child("budget").child("budgetToSpend").value.toString()
                val foodB = dataSnapshot.child("budget").child("food").value.toString()
                val shoppingB = dataSnapshot.child("budget").child("shopping").value.toString()
                val healthB = dataSnapshot.child("budget").child("health").value.toString()
                val activitiesB = dataSnapshot.child("budget").child("activities").value.toString()
                val membershipsB = dataSnapshot.child("budget").child("memberships").value.toString()
                val restaurantsB = dataSnapshot.child("budget").child("restaurants").value.toString()

                budgetLimit.text = textToDoubleFormat(budgetL)
                budgetToSpend.text = textToDoubleFormat(budgetTS)
                foodBudget.text = textToDoubleFormat(foodB)
                shoppingBudget.text = textToDoubleFormat(shoppingB)
                healthBudget.text = textToDoubleFormat(healthB)
                activitiesBudget.text = textToDoubleFormat(activitiesB)
                membershipsBudget.text = textToDoubleFormat(membershipsB)
                restaurantsBudget.text = textToDoubleFormat(restaurantsB)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseError", "Error when displaying information: ${databaseError.message}")
            }
        })
    }

    private fun textToDoubleFormat(text: String): String {
        val valor: Double = text.toDoubleOrNull() ?: return ""
        return "${'$'}${String.format("%.2f", valor)}"
    }

}
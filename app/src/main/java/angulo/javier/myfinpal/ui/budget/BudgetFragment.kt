package angulo.javier.myfinpal.ui.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.databinding.FragmentBudgetBinding

class BudgetFragment : Fragment() {

    private var _binding: FragmentBudgetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val budgetViewModel =
            ViewModelProvider(this).get(BudgetViewModel::class.java)

        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // Configura el OnClickListener para el botón
        binding.btnEditBudget.setOnClickListener {
            // Navega a otro fragmento (ejemplo: a un fragmento llamado EditBudgetFragment)
            navigateToEditBudget()
        }


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
}
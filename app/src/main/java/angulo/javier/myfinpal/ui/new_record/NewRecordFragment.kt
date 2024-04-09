package angulo.javier.myfinpal.ui.new_record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.databinding.FragmentNewRecordBinding

class NewRecordFragment : Fragment() {

    private var _binding: FragmentNewRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val newRecordViewModel =
            ViewModelProvider(this).get(NewRecordViewModel::class.java)

        _binding = FragmentNewRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupSpinner(binding.establishmentsSpinner, R.array.establishments)
        setupSpinner(binding.paymentMethodsSpinner, R.array.payment_methods)
        setupSpinner(binding.categoriesSpinner, R.array.categories)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}
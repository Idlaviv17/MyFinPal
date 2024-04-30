package angulo.javier.myfinpal.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.databinding.FragmentHistoryDetailBinding
import angulo.javier.myfinpal.util.Categories
import angulo.javier.myfinpal.util.IconHandler

class HistoryDetailFragment : Fragment() {

    private var _binding: FragmentHistoryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args = HistoryDetailFragmentArgs.fromBundle(requireArguments())
        val payment = args.payment
        val formattedCost = "$${payment.cost}"

        IconHandler.setIcon(payment.category, binding.iconBg, binding.icon)
        binding.paymentTitleTv.text = payment.title
        binding.establishmentTv.text = payment.establishment
        binding.paymentMethodTv.text = payment.method.stringValue
        binding.categoryTv.text = payment.category.stringValue
        binding.costTv.text = formattedCost
        binding.dateTv.text = payment.date.toString()
        binding.descriptionTv.text = payment.description

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
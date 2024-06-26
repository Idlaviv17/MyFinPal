package angulo.javier.myfinpal.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import angulo.javier.myfinpal.Dao.PaymentDAO
import angulo.javier.myfinpal.databinding.FragmentHomeBinding
import angulo.javier.myfinpal.ui.adapter.PaymentAdapter
import angulo.javier.myfinpal.ui.history.HistoryFragmentDirections
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private var _binding: FragmentHomeBinding? = null
    lateinit var rvList : RecyclerView
    lateinit var tvHomeText: TextView
    lateinit var userId: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Retrieve the username to show a welcome message
        database = Firebase.database.reference
        tvHomeText = _binding!!.tvHomeText
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        retrieveUsername()

        binding.paymentsHistoryBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToNavigationHistory()
            findNavController().navigate(action)
        }

        val data: MutableList<CardDataObject> = ArrayList()
        data.add(CardDataObject("Debit", "$2,300.00", "1234 5678 9012 3456"))
        data.add(CardDataObject("Credit", "$15,000.00", "9876 5432 1098 7654"))
        data.add(CardDataObject("Savings", "$36,752.00", "2468 0135 7924 6801"))

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adapter = CustomCardAdapter(data)

        rvList = _binding!!.rvList

        rvList.layoutManager = layoutManager
        rvList.setHasFixedSize(true)
        rvList.adapter = adapter

        val paymentsRv = binding.paymentsRv
        paymentsRv.layoutManager = LinearLayoutManager(requireContext())

        PaymentDAO().getPayments(userId) { retrievedPayments ->
            val recentPayments = retrievedPayments.takeLast(5)
            val reversedPayments = recentPayments.reversed()
            val paymentAdapter = PaymentAdapter(reversedPayments) { payment ->
                val navigateToHistoryAction = HomeFragmentDirections.actionNavigationHomeToNavigationHistory()
                findNavController().navigate(navigateToHistoryAction)

                val navigateToHistoryDetailAction = HistoryFragmentDirections.actionNavigationHistoryToNavigationHistoryDetail(payment)
                findNavController().navigate(navigateToHistoryDetailAction)
            }

            paymentsRv.adapter = paymentAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retrieveUsername() {
        val userRef = database.child("users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.child("username").value.toString()
                tvHomeText.text = "Welcome, $username!"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseError", "Error retrieving username: ${databaseError.message}")
            }
        })
    }
}
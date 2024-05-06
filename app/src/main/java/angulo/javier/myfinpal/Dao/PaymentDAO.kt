package angulo.javier.myfinpal.Dao

import angulo.javier.myfinpal.domain.Payment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PaymentDAO {
    private val users: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun addPayment(userId: String, payment: Payment, callback: DatabaseReference.CompletionListener) {
        val userPaymentsRef = users.child(userId).child("payments").push()
        userPaymentsRef.setValue(payment, callback)
    }

    fun getPayments(userId: String, callback: (List<Payment>) -> Unit) {
        val userPaymentsRef = users.child(userId).child("payments")
        userPaymentsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val payments = mutableListOf<Payment>()
                for (childSnapshot in dataSnapshot.children) {
                    val payment = childSnapshot.getValue(Payment::class.java)
                    if (payment != null) {
                        payment.uid = childSnapshot.key.toString()
                    }
                    payment?.let {
                        payments.add(it)
                    }
                }
                callback(payments)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error reading data: ${databaseError.message}")
            }
        })
    }

    fun updatePayment(userId: String, paymentId: String, updatedPayment: Payment, callback: DatabaseReference.CompletionListener) {
        val userPaymentRef = users.child(userId).child("payments").child(paymentId)
        userPaymentRef.setValue(updatedPayment, callback)
    }

    fun deletePayment(userId: String, paymentId: String, callback: DatabaseReference.CompletionListener) {
        val userPaymentRef = users.child(userId).child("payments").child(paymentId)
        userPaymentRef.removeValue(callback)
    }
}
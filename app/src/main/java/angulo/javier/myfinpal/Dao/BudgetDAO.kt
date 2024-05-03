package angulo.javier.myfinpal.Dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import angulo.javier.myfinpal.domain.Budget
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*

class BudgetDAO {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun createUserBudget(userId: String, budget: Budget) {
        val userBudgetRef = database.child(userId).child("budget")
        userBudgetRef.setValue(budget)
    }

    fun readUserBudget(userId: String, onDataChange: (DataSnapshot) -> Unit) {
        val userBudgetRef = database.child(userId).child("budget")
        userBudgetRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                onDataChange(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error al leer datos: ${databaseError.message}")
            }
        })
    }

    fun updateUserBudget(userId: String, newBudget: Budget) : Task<Void> {
        val userBudgetRef = database.child(userId).child("budget")
        return userBudgetRef.setValue(newBudget)
    }

    fun deleteUserBudget(userId: String) {
        val userRef = database.child(userId)
        userRef.removeValue()
    }
}
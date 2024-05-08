package angulo.javier.myfinpal.domain

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.io.Serializable
//@Parcelize
data class Budget(
    var budgetLimit: Double = 0.0,
    var spendBudget: Double = 0.0,
    var food: Double = 0.0,
    var shopping: Double = 0.0,
    var health: Double = 0.0,
    var activities: Double = 0.0,
    var memberships: Double = 0.0,
    var restaurants: Double = 0.0,
    var foodLimit: Double = 0.0,
    var shoppingLimit: Double = 0.0,
    var healthLimit: Double = 0.0,
    var activitiesLimit: Double = 0.0,
    var membershipsLimit: Double = 0.0,
    var restaurantsLimit: Double = 0.0,
): Serializable

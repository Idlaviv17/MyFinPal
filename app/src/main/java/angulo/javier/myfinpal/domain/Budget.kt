package angulo.javier.myfinpal.domain

import java.io.Serializable
data class Budget(
    var budgetLimit: Double = 0.0,
    var spendBudget: Double = 0.0,
    var food: Double = 0.0,
    var shopping: Double = 0.0,
    var health: Double = 0.0,
    var activities: Double = 0.0,
    var memberships: Double = 0.0,
    var restaurants: Double = 0.0
): Serializable

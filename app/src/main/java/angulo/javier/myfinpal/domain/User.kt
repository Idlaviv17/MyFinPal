package angulo.javier.myfinpal.domain

data class User(var email: String, var username: String ,var budget: Budget = Budget())

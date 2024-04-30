package angulo.javier.myfinpal.entity

import android.os.Parcelable
import angulo.javier.myfinpal.util.Categories
import angulo.javier.myfinpal.util.PaymentMethods
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Payment(
    var title: String,
    var establishment: String,
    var method: PaymentMethods,
    var category: Categories,
    var cost: Float,
    var date: LocalDate,
    var description: String
) : Parcelable

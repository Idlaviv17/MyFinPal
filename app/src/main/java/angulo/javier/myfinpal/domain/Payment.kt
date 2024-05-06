package angulo.javier.myfinpal.domain

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
    var title: String = "",
    var method: String = "",
    var category: String = "",
    var amount: Float = 0f,
    var date: String = "",
    var description: String = "",
    @Exclude
    var uid: String = ""
) : Parcelable
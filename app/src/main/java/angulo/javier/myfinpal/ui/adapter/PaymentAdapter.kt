package angulo.javier.myfinpal.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import angulo.javier.myfinpal.domain.Payment
import angulo.javier.myfinpal.R
import angulo.javier.myfinpal.util.IconHandler
import java.time.format.DateTimeFormatter

class PaymentAdapter(
    private val payments: List<Payment>,
    private val onItemClick: (payment: Payment) -> Unit
) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payment_item, parent, false)
        return PaymentViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = payments[position]
        holder.bind(payment)
        holder.itemView.setOnClickListener {
            onItemClick(payment)
        }
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    inner class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconBgImageView: ImageView = itemView.findViewById(R.id.icon_bg)
        private val iconImageView: ImageView = itemView.findViewById(R.id.icon)
        private val titleTextView: TextView = itemView.findViewById(R.id.title_tv)
        private val costTextView: TextView = itemView.findViewById(R.id.cost_tv)
        private val dateTextView: TextView = itemView.findViewById(R.id.date_tv)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(payment: Payment) {
            IconHandler.setIcon(payment.category, iconBgImageView, iconImageView)
            titleTextView.text = payment.title
            costTextView.text = costTextView.text
            dateTextView.text = payment.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }
}
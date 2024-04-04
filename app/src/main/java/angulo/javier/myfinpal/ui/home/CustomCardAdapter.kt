package angulo.javier.myfinpal.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import angulo.javier.myfinpal.R

class CustomCardAdapter (
    private val data: List<CardDataObject>
) : RecyclerView.Adapter<CustomCardAdapter.ViewHolder>() {

    private val items: MutableList<CardView>

    init {
        this.items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvType.text = data[position].type
        holder.tvAmount.text = data[position].amount
        holder.tvCardNumber.text = data[position].cardNumber

        items.add(holder.card)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder
    internal constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvCardNumber: TextView = itemView.findViewById(R.id.tvCardNumber)
        val card: CardView = itemView.findViewById(R.id.card)
    }
}
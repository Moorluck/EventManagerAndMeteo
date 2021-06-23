package be.bxl.eventsmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.bxl.eventsmanager.R
import be.bxl.eventsmanager.models.Event
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventAdapter(val onDeleteClickListener : (Int) -> Unit,
                   val onEditClickListener : (Int) -> Unit) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    // Data

    var events : MutableList<Event> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName : TextView = itemView.findViewById(R.id.tv_event_item)
        val tvDescription : TextView = itemView.findViewById(R.id.tv_description_item)
        val tvTime : TextView = itemView.findViewById(R.id.tv_time_item)
        val btnEdit : ImageView = itemView.findViewById(R.id.btn_edit_item)
        val btnDelete : ImageView = itemView.findViewById(R.id.btn_delete_item)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventView = layoutInflater.inflate(R.layout.item_event, parent, false)

        return ViewHolder(eventView)
    }

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {

        val event = events[position]

        holder.tvName.text = event.name
        holder.tvDescription.text = event.description
        holder.tvTime.text = event.time.format(DateTimeFormatter.ofPattern("HH:mm"))

        holder.btnDelete.setOnClickListener {
            onDeleteClickListener.invoke(event.id)
        }

        holder.btnEdit.setOnClickListener {
            onEditClickListener.invoke(event.id)
        }

    }

    override fun getItemCount(): Int {
        return events.size
    }
}
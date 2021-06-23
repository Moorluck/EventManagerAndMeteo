package be.bxl.eventsmanager.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.bxl.eventsmanager.R
import be.bxl.eventsmanager.models.Event
import java.time.format.DateTimeFormatter

class EventOfDayAdapter(val context : Context?, val onDeleteClickListener : (Int) -> Unit,
                        val onEditClickListener : (Int) -> Unit) : RecyclerView.Adapter<EventOfDayAdapter.ViewHolder>() {

    var eventsOfDay : MutableList<MutableList<Event>> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvDay : TextView = itemView.findViewById(R.id.tv_day_item_event_of_day)
        val rvEvent : RecyclerView = itemView.findViewById(R.id.rv_event_of_day)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventOfDayAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val eventOfDayView = layoutInflater.inflate(R.layout.item_event_of_day, parent, false)

        return ViewHolder(eventOfDayView)
    }

    override fun onBindViewHolder(holder: EventOfDayAdapter.ViewHolder, position: Int) {

        val listOfEvent : MutableList<Event> = eventsOfDay[position]

        holder.tvDay.text = listOfEvent[0].date.format(DateTimeFormatter.ofPattern("EEE dd MMM"))

        var adapter = EventAdapter(
            onDeleteClickListener = {
                onDeleteClickListener.invoke(it)
        },
            onEditClickListener = {
                onEditClickListener.invoke(it)
        })



        adapter.events = listOfEvent
        holder.rvEvent.adapter = adapter
        holder.rvEvent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    }

    override fun getItemCount(): Int {
        return eventsOfDay.size
    }
}
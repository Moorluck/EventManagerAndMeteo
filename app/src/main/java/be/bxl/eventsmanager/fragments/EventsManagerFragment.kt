package be.bxl.eventsmanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.bxl.eventsmanager.EventRepository
import be.bxl.eventsmanager.R
import be.bxl.eventsmanager.adapters.EventOfDayAdapter
import be.bxl.eventsmanager.models.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

class EventsManagerFragment() : Fragment() {

    //Views

    private lateinit var rvEvents : RecyclerView
    private lateinit var btnAdd : FloatingActionButton

    // Data

    var listOfListOfEvent = mutableListOf<MutableList<Event>>()

    lateinit var adapter : EventOfDayAdapter
    lateinit var repository : EventRepository



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_events_manager, container, false)

        rvEvents = v.findViewById(R.id.rv_event_of_day)
        btnAdd = v.findViewById(R.id.floating_btn_add_event_manager)

        // Get data from DB
        repository = EventRepository.newInstance(requireContext())

        repository.getListOfEventsOfDay {
            listOfListOfEvent = it
        }

        adapter = EventOfDayAdapter(context,
            onDeleteClickListener = {
                onDeleteBtnClickListener?.invoke(it)
            },
            onEditClickListener = {
                onEditBtnClickListener?.invoke(it)
            })

        adapter.eventsOfDay = listOfListOfEvent

        rvEvents.adapter = adapter
        rvEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        btnAdd.setOnClickListener(onAddEventClickListener)

        return v
    }

    fun updateList() {
        repository.getListOfEventsOfDay {
            listOfListOfEvent = it
        }

        adapter.eventsOfDay = listOfListOfEvent
    }

    private var onDeleteBtnClickListener : ((Int) -> Unit)? = null

    fun setOnDeleteBtnClickListener (lambda: (Int) -> Unit) {
        onDeleteBtnClickListener = lambda
    }

    private var onEditBtnClickListener : ((Int) -> Unit)? = null

    fun setOnEditBtnClickListener (lambda: (Int) -> Unit) {
        onEditBtnClickListener = lambda
    }

    private var onAddEventClickListener : ((View) -> Unit)? = null

    fun setOnAddEventClickListener (lambda : (View) -> Unit) {
        onAddEventClickListener = lambda
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventsManagerFragment()
    }
}
package be.bxl.eventsmanager

import android.content.Context
import be.bxl.eventsmanager.db.EventDAO
import be.bxl.eventsmanager.models.Event
import java.time.LocalDate

class EventRepository() {

    var context: Context? = null
    lateinit var db : EventDAO

    var events = mutableListOf<Event>()

    fun updateEvents() {
        events = db.openReadable().getAllEvent()
        db.close()
    }

    fun getListOfTodayEvent(listener : (MutableList<Event>) -> Unit) {

        updateEvents()

        val eventOfToday = mutableListOf<Event>()

        for (event in events) {
            if (event.date == LocalDate.now()) {
                eventOfToday.add(event)
            }
        }

        listener.invoke(eventOfToday)
    }

    fun getListOfTomorrowEvent(listener : (MutableList<Event>) -> Unit) {

        updateEvents()

        val eventOfTomorrow = mutableListOf<Event>()

        val localDateTomorrow : LocalDate = LocalDate.of(LocalDate.now().year,
            LocalDate.now().monthValue, LocalDate.now().dayOfMonth + 1)

        for (event in events) {
            if (event.date == localDateTomorrow) {
                eventOfTomorrow.add(event)
            }
        }

        listener.invoke(eventOfTomorrow)
    }

    fun getListOfEventsOfDay(listener : (MutableList<MutableList<Event>>) -> Unit) {

        updateEvents()

        val listOfEventsOfDay = mutableListOf<MutableList<Event>>()
        val listOfDifferentDay = mutableListOf<LocalDate>()

        for (event in events) {
            if (!listOfDifferentDay.contains(event.date)) {
                listOfDifferentDay.add(event.date)
            }
        }

        for (day in listOfDifferentDay) {

            val listOfEventOfTheDay = mutableListOf<Event>()

            for (event in events) {
                if (event.date == day) {
                    listOfEventOfTheDay.add(event)
                }
            }

            listOfEventsOfDay.add(listOfEventOfTheDay)
        }

        listOfEventsOfDay.sortBy {
            it[0].date
        }

        return listener.invoke(listOfEventsOfDay)
    }

    fun getEventById(id : Int) : Event? {
        val result = db.openReadable().getEventById(id)
        db.close()
        return result
    }

    fun deleteEventById(id : Int) {
        db.openWritable().delete(id)
        updateEvents()
        db.close()
    }

    fun insertEvent(event : Event) {
        db.openWritable().insert(event)
        updateEvents()
        db.close()
    }

    fun updateOneEvent(event: Event) {
        db.openWritable().update(event.id, event)
        updateEvents()
        db.close()
    }

    companion object {
        fun newInstance(context: Context) = EventRepository().apply {
            this.context = context
            this.db = EventDAO(context)
        }
    }
}
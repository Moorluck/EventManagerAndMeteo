package be.bxl.eventsmanager.models

import java.time.LocalDate
import java.time.LocalTime

data class Event(
    var name : String,
    var description : String,
    var date : LocalDate,
    var time : LocalTime,

    val id : Int = 0
)

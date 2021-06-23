package be.bxl.eventsmanager.models

import java.time.LocalDate

data class WeatherObject(
    val city : String,
    val lon : Double,
    val lat : Double,
    val temp : Int,
    val weather : String,
    val iconID : String,
    val date : LocalDate = LocalDate.now()
)
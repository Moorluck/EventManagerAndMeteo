package be.bxl.eventsmanager.api

import android.content.Context
import android.content.res.Resources
import be.bxl.eventsmanager.R

class URLHelper() {

    companion object {

        fun getApiKey(context: Context) : String {
            return context.getString(R.string.api_key_weather_app)
        }
        fun getApiKeyGoogle(context: Context) : String {
            return context.getString(R.string.api_key_google)
        }

        val URLCurrentWeather = "https://api.openweathermap.org/data/2.5/weather?q=__city__&appid=__apiKey__&units=metric"
        val URLNextWeather = "https://api.openweathermap.org/data/2.5/forecast?q=__city__&appid=__apiKey__&units=metric"
        val URLLocation = "https://maps.googleapis.com/maps/api/geocode/json?latlng=__lat__,__long__&key=__apiKeyGoogle__"
        val URLIcon = "https://openweathermap.org/img/w/__iconID__.png"

    }
}
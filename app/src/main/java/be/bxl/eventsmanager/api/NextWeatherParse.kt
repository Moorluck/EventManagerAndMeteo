package be.bxl.eventsmanager.api

import android.util.Log
import be.bxl.eventsmanager.models.WeatherObject
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime

class NextWeatherParse {
    companion object {
        fun parseJson(json : String) : MutableList<WeatherObject> {
            val jsonObject = JSONObject(json)
            val listOfWeather = jsonObject.getJSONArray("list")

            val cityObject = jsonObject.getJSONObject("city")
            val cityName = cityObject.getString("name")
            val coord = cityObject.getJSONObject("coord")
            val lat = coord.getDouble("lat")
            val lon = coord.getDouble("lon")

            val weathers = mutableListOf<WeatherObject>()
            for (i in 0 until listOfWeather.length() step(8)) {
                val weatherObject = listOfWeather.getJSONObject(i)

                val main = weatherObject.getJSONObject("main")
                val temp = main.getDouble("temp").toInt()

                val weatherInfo = weatherObject.getJSONArray("weather")
                val weatherInfoObject = weatherInfo.getJSONObject(0)
                val weather = weatherInfoObject.getString("main")

                val iconID = weatherInfoObject.getString("icon")

                val date = weatherObject.getString("dt_txt")
                    .substring(0, 10)

                val newWeather = WeatherObject(cityName, lon, lat, temp, weather, iconID, LocalDate.parse(date))

                Log.d("Time", weatherObject.getString("dt_txt"))

                weathers.add(newWeather)
            }

            return weathers
        }
    }
}
package be.bxl.eventsmanager.api

import be.bxl.eventsmanager.models.WeatherObject
import org.json.JSONObject

class CurentWeatherParse {

    companion object {
        fun parseJson(json: String): WeatherObject {
            val jsonObject = JSONObject(json)

            val coord = jsonObject.getJSONObject("coord")
            val lon: Double = coord.getDouble("lon")
            val lat: Double = coord.getDouble("lat")

            val cityName: String = jsonObject.getString("name")

            val main = jsonObject.getJSONObject("main")
            val temp = main.getDouble("temp").toInt()

            val weatherArray = jsonObject.getJSONArray("weather")
            val weatherObject = weatherArray.getJSONObject(0)
            val weather = weatherObject.getString("main")

            val iconID = weatherObject.getString("icon")

            return WeatherObject(cityName, lon, lat, temp, weather, iconID)
        }
    }
}
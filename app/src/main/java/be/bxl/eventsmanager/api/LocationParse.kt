package be.bxl.eventsmanager.api

import org.json.JSONObject

class LocationParse {
    companion object {
        fun parseJson(json : String) : String {
            val jsonObject = JSONObject(json)

            val result = jsonObject.getJSONArray("results").getJSONObject(0)

            val addressComponents = result.getJSONArray("address_components")

            lateinit var cityName : String

            for (i in 0 until addressComponents.length()) {
                val element = addressComponents.getJSONObject(i)
                val types = element.getJSONArray("types")

                val typesList = mutableListOf<String>()
                for (i in 0 until types.length()) {
                    typesList.add(types.getString(i))
                }

                if (typesList.contains("administrative_area_level_1")) {
                    cityName = element.getString("long_name")
                }
            }

            return cityName
        }
    }
}
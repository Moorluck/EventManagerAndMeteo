package be.bxl.eventsmanager


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.bxl.eventsmanager.adapters.MeteoAdapter
import be.bxl.eventsmanager.api.HttpRequest
import be.bxl.eventsmanager.api.LocationParse
import be.bxl.eventsmanager.api.NextWeatherParse
import be.bxl.eventsmanager.api.URLHelper
import be.bxl.eventsmanager.models.WeatherObject
import be.bxl.formation.demo_android_kotlin_gps.helpers.LocationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MeteoActivity : AppCompatActivity() {

    lateinit var etCityName : EditText
    lateinit var tvCityName : TextView
    lateinit var btnSet : Button
    lateinit var rvMeteo : RecyclerView
    lateinit var imgLocalisation : ImageView

    // Date

    lateinit var weathers : MutableList<WeatherObject>

    // Pref

    lateinit var sharedPref : SharedPreferences

    // Adapter

    lateinit var adapter: MeteoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meteo)

        etCityName = findViewById(R.id.et_meteo)
        tvCityName = findViewById(R.id.tv_city_name_meteo)
        btnSet = findViewById(R.id.btn_set_meteo)
        imgLocalisation = findViewById(R.id.img_localisation_meteo)

        rvMeteo = findViewById(R.id.rv_meteo)
        rvMeteo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MeteoAdapter()
        rvMeteo.adapter = adapter

        sharedPref = getSharedPreferences(getString(R.string.city_name), Context.MODE_PRIVATE)

        if (sharedPref.getString(getString(R.string.city_name), null) == null) {
            val editor = sharedPref.edit()
            editor.putString(getString(R.string.city_name), "Bruxelles")
            editor.apply()
        }

        btnSet.setOnClickListener {
            val cityName = etCityName.text.toString()
            val editor = sharedPref.edit()
            editor.putString(getString(R.string.city_name), cityName)
            editor.apply()
            launchThreadToUpdateUI()
        }

        imgLocalisation.setOnClickListener {
            val locationRequest = LocationHelper(this@MeteoActivity) {
                lifecycleScope.launch {
                    launchThreadToGetLocation(it)
                }
            }
            locationRequest.getLastLocation()
        }

        launchThreadToUpdateUI()

    }

    private suspend fun launchThreadToGetLocation(it: LocationHelper.Coordinate) {
        withContext(Dispatchers.IO) {
            val url = URLHelper.URLLocation
                .replace("__lat__", it.lat.toString())
                .replace("__long__", it.lon.toString())
                .replace("__apiKeyGoogle__", URLHelper.getApiKeyGoogle(this@MeteoActivity))

            val request = HttpRequest.getJsonFromRequest(url)
            if (request != null) {
                val cityName = LocationParse.parseJson(request)
                val editor = sharedPref.edit()
                editor.putString(getString(R.string.city_name), cityName)
                editor.apply()

                launchThreadToUpdateUI()
            }
        }
    }

    private fun launchThreadToUpdateUI() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val cityName = sharedPref.getString(getString(R.string.city_name), "") ?: ""
                val url = URLHelper.URLNextWeather
                    .replace("__city__", cityName)
                    .replace("__apiKey__", URLHelper.getApiKey(this@MeteoActivity))
                val request = HttpRequest.getJsonFromRequest(url)
                if (request != null) {
                    weathers = NextWeatherParse.parseJson(request)
                    updateUI()
                }
            }
        }
    }

    private suspend fun updateUI() {
        withContext(Dispatchers.Main) {
            tvCityName.text = sharedPref.getString(getString(R.string.city_name), "")
            adapter.weatherObjects = weathers
        }
    }


}
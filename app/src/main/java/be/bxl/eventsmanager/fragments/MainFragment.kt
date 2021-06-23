package be.bxl.eventsmanager.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.bxl.eventsmanager.EventRepository
import be.bxl.eventsmanager.MeteoActivity
import be.bxl.eventsmanager.R
import be.bxl.eventsmanager.adapters.EventAdapter
import be.bxl.eventsmanager.api.CurentWeatherParse
import be.bxl.eventsmanager.api.HttpRequest
import be.bxl.eventsmanager.api.URLHelper
import be.bxl.eventsmanager.models.WeatherObject
import be.bxl.eventsmanager.models.Event
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class MainFragment : Fragment() {

    // Views

    private lateinit var tvDate : TextView
    private lateinit var tvTemperature : TextView
    private lateinit var tvCity : TextView
    private lateinit var tvNoEventToday : TextView
    private lateinit var tvNoEventTomorrow : TextView

    private lateinit var rvToday : RecyclerView
    private lateinit var rvTomorrow : RecyclerView

    private lateinit var imgIconWeather : ImageView

    // Data

    private lateinit var repository : EventRepository

    private lateinit var todayEvents : MutableList<Event>
    private lateinit var tomorrowEvents : MutableList<Event>

    private lateinit var weatherObject : WeatherObject

    // Adapter

    private lateinit var todayAdapter : EventAdapter
    private lateinit var tomorrowAdapter : EventAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_main, container, false)

        tvDate = v.findViewById(R.id.tv_date_main)
        tvTemperature = v.findViewById(R.id.tv_temperature_main)
        tvCity = v.findViewById(R.id.tv_city_main)
        tvNoEventToday = v.findViewById(R.id.tv_no_event_today_main)
        tvNoEventTomorrow = v.findViewById(R.id.tv_no_event_tomorrow_main)

        rvToday = v.findViewById(R.id.rv_today_main)
        rvTomorrow = v.findViewById(R.id.rv_tomorrow_main)

        imgIconWeather = v.findViewById(R.id.img_icon_weather_main)

        // Set the date
        val localDateString = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE dd MMM"))
        val date = localDateString.substring(0, 1).uppercase() + localDateString.substring(1)
        tvDate.text = date.replace(".", "")


        // Get Event from database
        repository = EventRepository.newInstance(requireActivity().applicationContext)

        repository.getListOfTodayEvent {
            todayEvents = it
        }

        repository.getListOfTomorrowEvent {
            tomorrowEvents = it
        }

        // Setup rv

        // Today Adapter

        todayAdapter = EventAdapter (
            onDeleteClickListener = {
                onDeleteBtnClickListener?.invoke(it)
        },
            onEditClickListener = {
                onEditBtnClickListener?.invoke(it)
            })
        todayAdapter.events = this.todayEvents

        rvToday.adapter = todayAdapter
        rvToday.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        // Tomorrow Adapter

        tomorrowAdapter = EventAdapter (
            onDeleteClickListener = {
                onDeleteBtnClickListener?.invoke(it)
        },
            onEditClickListener = {
                onEditBtnClickListener?.invoke(it)
            })

        tomorrowAdapter.events = this.tomorrowEvents

        rvTomorrow.adapter = tomorrowAdapter
        rvTomorrow.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        updateTvNoEvent()

        // Click to acces meteo activity

        imgIconWeather.setOnClickListener {
            val intent = Intent(requireContext(), MeteoActivity::class.java)
            startActivity(intent)
        }

        return v

    }

    override fun onResume() {
        super.onResume()

        val sharedPref = activity?.getSharedPreferences(getString(R.string.city_name), Context.MODE_PRIVATE)

        if (sharedPref?.getString(getString(R.string.city_name), null) == null) {
            val editor = sharedPref?.edit()
            editor?.putString(getString(R.string.city_name), "Bruxelles")
            editor?.apply()
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val cityName = sharedPref?.getString(getString(R.string.city_name), "Bruxelles") ?: "Bruxelles"
                val url = URLHelper.URLCurrentWeather
                    .replace("__city__", cityName)
                    .replace("__apiKey__", URLHelper.getApiKey(requireContext()))
                val request = HttpRequest.getJsonFromRequest(url)
                if (request != null) {
                    weatherObject = CurentWeatherParse.parseJson(request)
                    updateWeatherUI(weatherObject)
                }
            }
        }
    }

    suspend fun updateWeatherUI(weatherObject: WeatherObject) {

        withContext(Dispatchers.Main) {
            val temperatureText = weatherObject.temp.toString() + "°C"
            tvTemperature.text = temperatureText
            tvCity.text = weatherObject.city

            Picasso.get()
                .load(URLHelper.URLIcon.replace("__iconID__", weatherObject.iconID))
                .into(imgIconWeather)
        }

    }

    fun updateList() {
        repository.getListOfTodayEvent {
            todayEvents = it
        }
        repository.getListOfTomorrowEvent {
            tomorrowEvents = it
        }

        todayAdapter.events = todayEvents
        tomorrowAdapter.events = tomorrowEvents

        updateTvNoEvent()
    }

    private fun updateTvNoEvent() {
        if (tomorrowEvents.isEmpty()) {
            tvNoEventTomorrow.visibility = View.VISIBLE
        } else {
            tvNoEventTomorrow.visibility = View.INVISIBLE
        }

        if (todayEvents.isEmpty()) {
            tvNoEventToday.visibility = View.VISIBLE
        } else {
            tvNoEventToday.visibility = View.INVISIBLE
        }
    }

    // Listeneer

    private var onDeleteBtnClickListener : ((Int) -> Unit)? = null

    fun setOnDeleteBtnClickListener (lambda: (Int) -> Unit) {
        onDeleteBtnClickListener = lambda
    }

    private var onEditBtnClickListener : ((Int) -> Unit)? = null

    fun setOnEditBtnClickListener (lambda: (Int) -> Unit) {
        onEditBtnClickListener = lambda
    }

    companion object {
        @JvmStatic
        public fun newInstance() = MainFragment()
    }
}
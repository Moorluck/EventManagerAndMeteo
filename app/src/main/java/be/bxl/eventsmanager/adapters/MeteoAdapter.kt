package be.bxl.eventsmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.bxl.eventsmanager.R
import be.bxl.eventsmanager.api.URLHelper
import be.bxl.eventsmanager.models.WeatherObject
import com.squareup.picasso.Picasso
import java.time.format.DateTimeFormatter

class MeteoAdapter : RecyclerView.Adapter<MeteoAdapter.ViewHolder>() {

    var weatherObjects : MutableList<WeatherObject> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvDate : TextView = itemView.findViewById(R.id.tv_date_item_meteo)
        val tvTemp : TextView = itemView.findViewById(R.id.tv_temp_item_meteo)
        val imgIconWeather : ImageView = itemView.findViewById(R.id.img_meteo_icon_item_meteo)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeteoAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val meteoView = layoutInflater.inflate(R.layout.item_meteo, parent, false)

        return ViewHolder(meteoView)
    }

    override fun onBindViewHolder(holder: MeteoAdapter.ViewHolder, position: Int) {
        val weather = weatherObjects[position]

        holder.tvDate.text = weather.date.format(DateTimeFormatter.ofPattern("EEE dd MMM"))
        val tempString = weather.temp.toString() + "°C"
        holder.tvTemp.text = tempString

        Picasso.get()
            .load(URLHelper.URLIcon.replace("__iconID__", weather.iconID))
            .into(holder.imgIconWeather)
    }

    override fun getItemCount(): Int {
        return weatherObjects.size
    }
}
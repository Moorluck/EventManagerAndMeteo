package be.bxl.eventsmanager.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import be.bxl.eventsmanager.EventRepository
import be.bxl.eventsmanager.R
import be.bxl.eventsmanager.models.Event
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewEventFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Views

    private lateinit var etName : EditText
    private lateinit var etDescription : EditText
    private lateinit var etDate : EditText
    private lateinit var etTime : EditText

    private lateinit var btnAdd : Button

    // Data to Add in DB

    var eventToEdit : Event? = null

    private lateinit var name : String
    private lateinit var description : String
    private var date : LocalDate = LocalDate.now()
    private var time : LocalTime = LocalTime.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_new_event, container, false)

        etName = v.findViewById(R.id.et_name_new_fragment)
        etDescription = v.findViewById(R.id.et_description_new_event)
        etDate = v.findViewById(R.id.et_date_new_event)
        etTime = v.findViewById(R.id.et_time_new_event)

        btnAdd = v.findViewById(R.id.btn_validate_new_event)


        etDate.setOnClickListener {
            showDatePickerDialog()
        }

        etTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnAdd.setOnClickListener {
            if (eventToEdit == null) {
                addDataToDB()
            }
            else {
                updateDataInDB()
            }

            activity?.supportFragmentManager?.popBackStack()
        }

        if (eventToEdit != null) {
            etName.setText(eventToEdit?.name)
            etDescription.setText(eventToEdit?.description)
            etDate.setText(eventToEdit?.date.toString())
            etTime.setText(eventToEdit?.time.toString())
            btnAdd.text = "Update"

            date = eventToEdit!!.date
            time = eventToEdit!!.time
        }
        else {
            etName.setText("")
            etDescription.setText("")
            etDate.setText("")
            etTime.setText("")
            btnAdd.text = "Add new event"
        }

        return v
    }




    private fun showDatePickerDialog() {
        val datePickerDialog : DatePickerDialog? = context?.let {
            DatePickerDialog(
                it,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
        }
        datePickerDialog?.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog : TimePickerDialog? = context?.let {
            TimePickerDialog(
                it,
                this,
                12,
                0,
                true
            )
        }
        timePickerDialog?.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateText = "$year-$month-$dayOfMonth"
        etDate.setText(dateText)

        date = LocalDate.of(year, month + 1, dayOfMonth)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val timeText = "$hourOfDay:$minute"
        etTime.setText(timeText)

        time = LocalTime.of(hourOfDay, minute)
    }

    private fun addDataToDB() {
        name = etName.text.toString()
        description = etDescription.text.toString()

        val newEvent = Event(name, description, date, time)
        EventRepository.newInstance(requireContext()).insertEvent(newEvent)
    }

    private fun updateDataInDB() {
        name = etName.text.toString()
        description = etDescription.text.toString()

        val newEvent = Event(name, description, date, time, eventToEdit!!.id)
        EventRepository.newInstance(requireContext()).updateOneEvent(newEvent)

        eventToEdit = null
    }


    companion object {
        @JvmStatic
        fun newInstance() = NewEventFragment()
    }
}
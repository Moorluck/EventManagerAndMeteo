package be.bxl.eventsmanager.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import be.bxl.eventsmanager.models.Event
import java.lang.RuntimeException
import java.time.LocalDate
import java.time.LocalTime

class EventDAO(val context: Context) {

    private var dbHelper : DBHelper? = null
    private var _db : SQLiteDatabase? = null

    val db : SQLiteDatabase
        get() = _db ?: throw RuntimeException("Utilisez la méthode openWritable ou openReadable")

    fun openWritable() : EventDAO {
        dbHelper = DBHelper(context)
        _db = dbHelper!!.writableDatabase
        return this
    }

    fun openReadable() : EventDAO {
        dbHelper = DBHelper(context)
        _db = dbHelper!!.readableDatabase
        return this
    }

    fun close() {
        _db?.close()
        dbHelper?.close()
        _db = null
        dbHelper = null
    }

    private fun cursorToEvent(c : Cursor) : Event {
        val name = c.getString(c.getColumnIndex(EventTable.COLUMN_NAME))
        val description = c.getString(c.getColumnIndex(EventTable.COLUMN_DESCRIPTION))

        val dateString = c.getString(c.getColumnIndex(EventTable.COLUMN_DATE))
        val date = LocalDate.parse(dateString)

        val timeString = c.getString(c.getColumnIndex(EventTable.COLUMN_TIME))
        val time = LocalTime.parse(timeString)

        val id = c.getInt(c.getColumnIndex(EventTable.COLUMN_ID))

        return Event(name, description, date, time, id)
    }

    private fun eventToContentValues(event : Event) : ContentValues {
        return ContentValues().apply {
            put(EventTable.COLUMN_NAME, event.name)
            put(EventTable.COLUMN_DESCRIPTION, event.description)
            put(EventTable.COLUMN_DATE, event.date.toString())
            put(EventTable.COLUMN_TIME, event.time.toString())
        }
    }

    //CRUD

    fun insert(event : Event) : Long {
        val cv : ContentValues = eventToContentValues(event)
        return db.insert(EventTable.TABLE_NAME, null, cv)
    }

    fun getEventById(id : Int) : Event? {
        val c : Cursor = db.query(EventTable.TABLE_NAME, null, "${EventTable.COLUMN_ID} = ?",
            arrayOf(id.toString()), null, null, null)

        if (c.count == 0) {
            return null
        }

        c.moveToFirst()
        return cursorToEvent(c)
    }

    fun getAllEvent() : MutableList<Event> {
        val c : Cursor = db.query(EventTable.TABLE_NAME, null, null,
            null, null, null, null)

        if(c.count == 0) return mutableListOf()

        val result : MutableList<Event> = mutableListOf()

        c.moveToFirst()
        while(!c.isAfterLast) {
            val event : Event = cursorToEvent(c)
            result.add(event)

            c.moveToNext()
        }

        return result
    }

    fun update(id : Int, event : Event) : Boolean {
        val cv : ContentValues = eventToContentValues(event)

        val nbRow : Int = db.update(EventTable.TABLE_NAME, cv, "${EventTable.COLUMN_ID} = ?",
            arrayOf(id.toString()))

        return nbRow == 1
    }

    fun delete(id: Int) : Boolean {
        val nbRow : Int = db.delete(EventTable.TABLE_NAME, "${EventTable.COLUMN_ID} = ?",
            arrayOf(id.toString()))
        return nbRow == 1
    }

    fun deleteAll() : Boolean {
        val nbRow : Int = db.delete(EventTable.TABLE_NAME, null, null)
        return nbRow > 0
    }
}
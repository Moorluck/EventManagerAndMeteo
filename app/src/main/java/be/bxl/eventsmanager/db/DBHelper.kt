package be.bxl.eventsmanager.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(val context: Context)
    : SQLiteOpenHelper(context, DBInfo.NAME, null, DBInfo.VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(EventTable.REQUEST_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(EventTable.REQUEST_DROP)

        onCreate(db)
    }
}
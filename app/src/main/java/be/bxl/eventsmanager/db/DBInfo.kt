package be.bxl.eventsmanager.db

class DBInfo {
    companion object {
        val NAME : String = "MyDB"
        val VERSION : Int = 1
    }
}

class EventTable {
    companion object {
        val TABLE_NAME = "event"

        val COLUMN_ID = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_DESCRIPTION = "description"
        val COLUMN_DATE = "date"
        val COLUMN_TIME = "time"

        val REQUEST_CREATE : String = """
            CREATE TABLE $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT,
            $COLUMN_DESCRIPTION TEXT,
            $COLUMN_DATE TEXT,
            $COLUMN_TIME TEXT
            );
        """.trimIndent()

        val REQUEST_DROP : String = """
            DROP TABLE $TABLE_NAME;
        """.trimIndent()
    }
}
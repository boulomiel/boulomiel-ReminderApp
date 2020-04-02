package com.rubenmimoun.reminderapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import com.rubenmimoun.reminderapp.Notif

@RequiresApi(Build.VERSION_CODES.P)
class Database(context: Context?) : SQLiteOpenHelper(context,"Database", null, 1) {


    private  val createENTRIES =
        "CREATE TABLE ${DatabaseContract.Details.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${DatabaseContract.Details.COLUMN_NAME_DATE} TEXT," +
                "${DatabaseContract.Details.COLUMN_NAME_HOUR} TEXT ," +
                "${DatabaseContract.Details.COLUMN_NAME_TODO} TEXT )"

    private  val sqlDeleteEntries = "DROP TABLE IF EXISTS ${DatabaseContract.Details.TABLE_NAME}"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(sqlDeleteEntries)
        onCreate(db)
    }

    fun insertData(date : String , hour : String , todo : String) : Long?{
        // Gets the data repository in write mode
        val db = this.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(DatabaseContract.Details.COLUMN_NAME_DATE, date)
            put(DatabaseContract.Details.COLUMN_NAME_HOUR, hour)
            put(DatabaseContract.Details.COLUMN_NAME_TODO, todo)
        }

        // Insert the new row, returning the primary key value of the new row
        db.insert(DatabaseContract.Details.TABLE_NAME, null, values)

        val newRowId = db?.insert(DatabaseContract.Details.TABLE_NAME, null, values)
        db.close()
        return newRowId
        }

    fun deleteRow(date : String) : Int{

        val db = this.writableDatabase

        // Define 'where' part of query.
        val selection = "${DatabaseContract.Details.COLUMN_NAME_DATE} LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(date)
        // Issue SQL statement.
        val deletedRows = db.delete(DatabaseContract.Details.TABLE_NAME, selection, selectionArgs)
        db.close()
        // number of rows deleted
        return deletedRows
    }

    fun readDb() : ArrayList<Notif>{
        val db = this.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(BaseColumns._ID,
            DatabaseContract.Details.COLUMN_NAME_DATE,
            DatabaseContract.Details.COLUMN_NAME_HOUR,
            DatabaseContract.Details.COLUMN_NAME_TODO)

        // Filter results WHERE "title" = 'My Title'
//        val selection = "${DatabaseContract.Details.COLUMN_NAME_DATE} = ?"
//        val selectionArgs = arrayOf("Date")

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${DatabaseContract.Details.COLUMN_NAME_DATE} DESC"

        val cursor = db.query(
            DatabaseContract.Details.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
             null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

        val set = HashSet<Notif>()
        val notifs  = arrayListOf<Notif>()
        with(cursor) {
            while (moveToNext()) {

                val notif = Notif(
                    getString(getColumnIndexOrThrow(DatabaseContract.Details.COLUMN_NAME_DATE)),
                    getString(getColumnIndexOrThrow(DatabaseContract.Details.COLUMN_NAME_HOUR)),
                    getString(getColumnIndexOrThrow(DatabaseContract.Details.COLUMN_NAME_TODO))
                )

                set.add(notif)
            }
        }

        for (element in set){
            notifs.add(element)
        }

        return notifs
    }


    fun tableExists(): Boolean {
        val db = this.readableDatabase
            if (db == null || !db.isOpen) return false
            val cursor = db.query(
                DatabaseContract.Details.TABLE_NAME,
                null,
                null,
                null,
                null,
                null, null,null)

            if (!cursor.moveToFirst()) {
                cursor.close()
                return false
            }
            val count = cursor.getInt(0)
            cursor.close()
            return count > 0

    }

}
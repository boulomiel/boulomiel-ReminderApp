package com.rubenmimoun.reminderapp.database

import android.provider.BaseColumns

class DatabaseContract {

    /* Inner class that defines the table contents */
    object Details : BaseColumns {
        const val TABLE_NAME = "Notifications"
        const val COLUMN_NAME_DATE = "Date"
        const val COLUMN_NAME_HOUR = "Hour"
        const val COLUMN_NAME_TODO = "todo"
    }
}
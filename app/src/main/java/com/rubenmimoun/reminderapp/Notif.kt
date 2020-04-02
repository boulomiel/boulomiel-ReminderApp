package com.rubenmimoun.reminderapp

import java.util.*

data class Notif(val date : String , val time : String , val todo : String) : Comparable<String>{

    override fun compareTo(other: String): Int {
        TODO("Not yet implemented")
    }

}
package com.rubenmimoun.reminderapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.rubenmimoun.reminderapp.adapter.NotificationAdapter
import com.rubenmimoun.reminderapp.database.Database
import kotlinx.android.synthetic.main.activity_notification_list.*


@RequiresApi(Build.VERSION_CODES.P)
class NotificationListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var list : ArrayList<Notif>
    private lateinit var  editDate : EditText
    private lateinit var  editHour : EditText
    private lateinit var  editTodo : EditText
    private lateinit var database : Database
    private lateinit var adapterNotif : NotificationAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        setSupportActionBar(toolbar)


        database = Database(this)
        list = setListOnStart()
        adapterNotif =  NotificationAdapter(list)
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterNotif

        val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener(this)
        adapterNotif.notifyDataSetChanged()

        setRecyclerViewItemTouchListener()

    }

    override fun onClick(v: View?) {
        if(v?.id == fab.id){
            createItem()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)

        return true

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                createItem()
                true
            }else -> false
        }
    }

    private fun setListOnStart() : ArrayList<Notif>{

        var list = ArrayList<Notif>()

        if(!database.tableExists()){
            return list
        }

        list = database.readDb()


        return list

    }

    private fun createItem(){

        val builderDialog = AlertDialog.Builder(this)
        builderDialog.setTitle("Add a new stuff to do : ")

        builderDialog.setView(alertDialogViews())
        builderDialog.setPositiveButton("Done"){ _, _ ->

            val date =editDate.text.toString()
            val hour = editHour.text.toString()
            val todo = editTodo.text.toString()

            if(checkParams(date,hour,todo)){
                database.insertData(date,hour,todo)
                createAlarm(date,hour,todo)
            }

        }

        builderDialog.setNeutralButton("Cancel"){ _, _ ->  }

        val dialog = builderDialog.create()
        dialog.show()


    }

    private fun alertDialogViews() : View {

        val linearLayout = LinearLayout(this)


        editDate = EditText(this)
        editHour = EditText(this)
        editTodo = EditText(this)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = lp


        editDate.width = 200
        editDate.hint = "Date : dd / mm / yyyy"
        editDate.inputType = InputType.TYPE_CLASS_DATETIME

        editHour.width = 200
        editHour.hint = "Exact time of the reminder: hh : mm"
        editHour.inputType = InputType.TYPE_CLASS_DATETIME
        editTodo.width = 2000
        editTodo.hint = "To do :"
        editTodo.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE

        linearLayout.addView(editDate)
        linearLayout.addView(editHour)
        linearLayout.addView(editTodo)

        return linearLayout

    }

    private fun checkParams(date : String , hour : String, todo : String) : Boolean {

        if(date == "" || hour == "" || todo == ""){
            Snackbar.make(window.decorView,"At least one field is empty", Snackbar.LENGTH_SHORT).show()
            return false
        }

        if(!isDateValidFormat(date)){
            Snackbar.make(window.decorView,"The date format is incorrect", Snackbar.LENGTH_SHORT).show()
            return false
        }
        if(!isHourValidFormat(hour)){
            Snackbar.make(window.decorView,"The hour format is incorrect", Snackbar.LENGTH_SHORT).show()
            return false
        }


        list.add(Notif(date,hour,todo))

        return true
    }


    private fun setRecyclerViewItemTouchListener() {


        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder): Boolean {

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                val position = viewHolder.adapterPosition
                database.deleteRow(list[position].date)
                list.removeAt(position)
                adapterNotif.notifyItemRemoved(position)
                Snackbar.make(window.decorView,"Notification removed", Snackbar.LENGTH_SHORT).show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    fun createAlarm(date :String , hour: String, todo: String){

        val alarm = PendingIntent.getBroadcast(
            this,
            0,
            Intent("ALARM"),
            PendingIntent.FLAG_NO_CREATE
        ) == null

        if (alarm) {
            val itAlarm = Intent("ALARM")
            itAlarm.putExtra("date",date)
            itAlarm.putExtra("hour",hour)
            itAlarm.putExtra("todo",todo)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, itAlarm, 0)
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.SECOND, 3)
            val alarme =
                getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarme.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                60000,
                pendingIntent
            )
        }

    }



    override fun onDestroy() {
        database.close()
        super.onDestroy()


    }
}

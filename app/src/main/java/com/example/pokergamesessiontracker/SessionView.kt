package com.example.pokergamesessiontracker

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// This class is used when a user clicks on a specific session in the home page
// This class will gather and display all of the session information to allow users to go back through their recorded sessions
// This class also gives the user the option to delete the session with the button at the bottom of the page
class SessionView : AppCompatActivity() {

    private lateinit var buttonDeleteSession: Button
    private lateinit var databaseSession: DatabaseReference


    private lateinit var datePlayed: TextView
    private lateinit var locationPlayed: TextView
    private lateinit var gameTypePlayed: TextView
    private lateinit var hoursPlayed: TextView
    private lateinit var smallBlindAmount: TextView
    private lateinit var bigBlindAmount: TextView
    private lateinit var buyInAmount: TextView
    private lateinit var cashOutAmount: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_view)

        buttonDeleteSession = findViewById<Button>(R.id.buttonDeleteSession) as Button

        datePlayed = findViewById<TextView>(R.id.datePlayed)
        locationPlayed = findViewById<TextView>(R.id.locationPlayed)
        gameTypePlayed = findViewById<TextView>(R.id.gameTypePlayed)
        hoursPlayed = findViewById<TextView>(R.id.hoursPlayed)
        smallBlindAmount = findViewById<TextView>(R.id.smallBlindAmount)
        bigBlindAmount = findViewById<TextView>(R.id.bigBlindAmount)
        buyInAmount = findViewById<TextView>(R.id.buyInAmount)
        cashOutAmount = findViewById<TextView>(R.id.cashOutAmount)

        val bundle = intent.extras
        val uid = bundle?.get("uid").toString()
        val id = bundle?.get("id").toString()
        val date = bundle?.get("date").toString()
        val location = bundle?.get("location").toString()
        val gameType = bundle?.get("gameType").toString()
        val hours = bundle?.get("hoursPlayed").toString()
        val smallBlind = bundle?.get("smallBlind").toString()
        val bigBlind = bundle?.get("bigBlind").toString()
        val buyIn = bundle?.get("buyInAmount").toString()
        val cashOut = bundle?.get("cashOutAmount").toString()
        var sessionsList: ArrayList<Session> = bundle?.getParcelableArrayList("sessionsList")!!

        datePlayed.text = date
        locationPlayed.text = location
        gameTypePlayed.text = gameType
        hoursPlayed.text = hours
        smallBlindAmount.text = smallBlind
        bigBlindAmount.text = bigBlind
        buyInAmount.text = buyIn
        cashOutAmount.text = cashOut

        databaseSession = FirebaseDatabase.getInstance().getReference().child(uid).child(id)

        buttonDeleteSession.setOnClickListener {
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        // This removes the current session from the database
                        databaseSession.removeValue()
                        Toast.makeText(this, "Session Deleted", Toast.LENGTH_SHORT).show()
                        finish()
                        return@OnClickListener
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                }
            }
            val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            ab.setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
        }
    }
}
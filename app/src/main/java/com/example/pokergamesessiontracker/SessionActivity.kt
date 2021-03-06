package com.example.pokergamesessiontracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlin.collections.ArrayList
import android.util.Log

// This class handles the logistics of entering user data and submitting a session to the database

class SessionActivity : AppCompatActivity() {

    private lateinit var buttonSubmitSession: Button
    private lateinit var sessionDate: DatePicker
    private lateinit var sessionLocation: EditText
    private lateinit var sessionRadioGroup: RadioGroup
    private lateinit var sessionRadioButtonTexasHoldem: RadioButton
    private lateinit var sessionRadioButtonPotLimitOmaha: RadioButton
    private lateinit var smallBlindEntry: EditText
    private lateinit var bigBlindEntry: EditText
    private lateinit var buyInAmount: EditText
    private lateinit var cashOutAmount: EditText
    private lateinit var hoursPlayed: EditText

    private lateinit var databaseSession: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_session)

        buttonSubmitSession = findViewById<View>(R.id.buttonSubmitSession) as Button
        sessionDate = findViewById<DatePicker>(R.id.datePicker) as DatePicker
        sessionLocation = findViewById<EditText>(R.id.locationEntry) as EditText
        sessionRadioGroup = findViewById<RadioGroup>(R.id.radioGroup) as RadioGroup
        sessionRadioButtonTexasHoldem = findViewById<RadioButton>(R.id.texasHoldemSelection) as RadioButton
        sessionRadioButtonPotLimitOmaha = findViewById<RadioButton>(R.id.PLOSelection) as RadioButton
        smallBlindEntry = findViewById<EditText>(R.id.smallBlindEntry) as EditText
        bigBlindEntry = findViewById<EditText>(R.id.bigBlindEntry) as EditText
        buyInAmount = findViewById<EditText>(R.id.buyInEntry) as EditText
        cashOutAmount = findViewById<EditText>(R.id.cashOutEntry) as EditText
        hoursPlayed = findViewById<EditText>(R.id.hoursPlayedEntry)

        databaseSession = FirebaseDatabase.getInstance().getReference()

        val bundle = intent.extras
        var count = bundle?.getInt("count")
        var uid = bundle?.getString("uid")
        var sessions = ArrayList<Session>()

        // When the user hits the submit session button, this code will go through the radio group, date selector, and edit text fields to gather the information, store it in a Session class object, then finally add it to the database
        buttonSubmitSession.setOnClickListener {
            var date = sessionDate.year.toString() + "-" + sessionDate.month.toString() + "-" + sessionDate.dayOfMonth.toString()
            var sessionType = ""
            if (sessionRadioButtonTexasHoldem.isChecked()) {
                sessionType = "Texas Hold'Em"
            } else if (sessionRadioButtonPotLimitOmaha.isChecked()) {
                sessionType = "Pot Limit Omaha"
            }

            Log.i("sessionType", sessionType)

            var location = sessionLocation.text.toString()
            var smallBlind = smallBlindEntry.text.toString().toInt()
            var bigBlind = bigBlindEntry.text.toString().toInt()
            var buyIn = buyInAmount.text.toString().toInt()
            var cashOut = cashOutAmount.text.toString().toInt()
            var hours = hoursPlayed.text.toString().toInt()
            var id = databaseSession.push().key

            var sessionId = "Session: " + id

            var addThis = Session(id, sessionId, date, sessionType, location, smallBlind, bigBlind, buyIn, cashOut, hours)
            sessions.add(addThis)

            Log.i("Session Activity", "UID is " + uid.toString())


            // Adding the session to the database under the current user account
            if (uid != null) {
                Log.i("Session Activity", "adding to database ")
                //databaseSession.child(uid).setValue(sessionId)
                databaseSession.child(uid).child(id!!).setValue(addThis)
            }


            // After adding the session, the app will return to the home paage
            var returnIntent = Intent()
            returnIntent.putParcelableArrayListExtra("result", sessions)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}

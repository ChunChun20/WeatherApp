package com.example.courseproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeChangeReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val isAirplaneModeEnabled = intent?.getBooleanExtra("state",false) ?: return
        if(isAirplaneModeEnabled) {
            Toast.makeText(context,"Broadcast Receiver - Airplane Mode enabled", Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(context,"Broadcast Receiver - Airplane Mode disabled", Toast.LENGTH_LONG).show()

        }
    }
}
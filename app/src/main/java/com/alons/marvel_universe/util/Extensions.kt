package com.alons.marvel_universe.util

import android.app.Activity
import android.widget.Toast

object Extensions {
    object Extensions {
        fun Activity.toast(msg: String){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}
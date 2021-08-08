package me.happyclick.activitybutton

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.happyclick.activitybutton.ui.ButtonActionFragment
import me.happyclick.activitybutton.ui.contacts.ContactsFragment

class Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ButtonActionFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}
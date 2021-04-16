package com.example.sampleapp

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapp.ui.gallery.GalleryFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.util.ExtraConstants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_scrolling.*
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {

    private  var Checkfor: Boolean=false
    private lateinit var appBarConfiguration: AppBarConfiguration
  lateinit var   sharedPreference:SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {

sharedPreference= SharedPreference(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(FirebaseAuth.getInstance().currentUser==null)
        {
            Log.i("TAG", "Sending Sigin Request")
            val intent=Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else {
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home, R.id.nav_contact, R.id.nav_message
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val item = menu.findItem(R.id.app_bar_switch)
        item.setActionView(R.layout.switch_item)

        val mySwitch = item.actionView.findViewById<SwitchCompat>(R.id.switchForActionBar)
        mySwitch.isChecked=sharedPreference.getValueBoolien("AwayMode",false)
        mySwitch.setOnCheckedChangeListener { _, isChecked ->
             Checkfor = if (isChecked) true else false
            if(isChecked) {


                sharedPreference.save("AwayMode",true)

                Toast.makeText(
                    this@MainActivity, "Away Mode On",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                sharedPreference.removeValue("AwayMode")
                sharedPreference.save("AwayMode",false)
                Toast.makeText(
                    this@MainActivity, "Away Mode Off",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }


        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e("Switch","ID :${item.itemId}")
        return when (item.itemId) {
            R.id.SignOut -> {
                AuthUI.getInstance()
                    .signOut(this).addOnCompleteListener {
                        val intent = Intent(
                            this,
                            LoginActivity::class.java
                        )
                        startActivity(intent)
                    }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
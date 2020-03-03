package com.shanya.leaningassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val drawerNavView: NavigationView = findViewById(R.id.drawer_navigation_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.drawerMenuAbout,R.id.bottomMenuTodo,R.id.bottomMenuQuestion,R.id.bottomMenuWord), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)

        drawerNavView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.drawerMenuTodo -> {
                    Toast.makeText(this,"todo",Toast.LENGTH_SHORT).show()
                }
                R.id.drawerMenuAbout -> {
                    Toast.makeText(this,"about",Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.drawerMenuTodo){
            Toast.makeText(this,"about",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}

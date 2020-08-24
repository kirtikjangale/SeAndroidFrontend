package com.example.seandroidproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.example.seandroidproject.R
import com.example.seandroidproject.fragment.*
import com.google.android.material.navigation.NavigationView


class HomePageActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    lateinit var frameLayout : FrameLayout
    lateinit var coordinatorLayout : CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var navigationView : NavigationView

    var previousMenuItem : MenuItem? = null
    var isLoggedIn : Boolean =false

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        frameLayout = findViewById(R.id.frame)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)


        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)


        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)

        val navUsername = headerView.findViewById<View>(R.id.txtName) as TextView
        val navMobileNumber = headerView.findViewById<View>(R.id.txtNumber) as TextView

        setUpToolbar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomePageActivity, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.syncState()

        openHome()


        val menu: Menu = navigationView.menu


        if(isLoggedIn){
            menu.findItem(R.id.login).isVisible = false
        }else{
            menu.findItem(R.id.logout).isVisible = false
            menu.findItem(R.id.wishList).isVisible = false
            menu.findItem(R.id.sell).isVisible = false
            menu.findItem(R.id.listings).isVisible = false
            menu.findItem(R.id.profile).isVisible = false

            navUsername.visibility=View.GONE
            navMobileNumber.visibility = View.GONE
        }

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId){
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        AllItemsFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "All Items"
                }
                R.id.wishList -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        WishlistFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Wishlist"
                }
                R.id.sell -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        SellItemsFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Post Item"
                }

                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        ProfileFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "My Profile"
                }

                R.id.listings -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        MyListingsFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "My Listings"
                }

                R.id.logout -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frame,
                        LogoutFragment()
                    ).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Logout"
                }

                R.id.login->{
                    val intent = Intent(this@HomePageActivity,LoginActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawers()
                    finish()
                }
            }

            return@setNavigationItemSelectedListener true
        }

    }

    private fun openHome(){
        supportFragmentManager.beginTransaction().replace(
            R.id.frame,
            AllItemsFragment()
        ).addToBackStack("Home").commit()
        drawerLayout.closeDrawers()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }

    private fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "SeAndroidApp"
        supportActionBar?.setHomeButtonEnabled(true) //enable home button
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //display home button

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {

        when(supportFragmentManager.findFragmentById(R.id.frame)){
            !is AllItemsFragment -> openHome()
            else->super.onBackPressed()
        }

    }

}
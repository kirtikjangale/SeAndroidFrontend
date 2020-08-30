package com.example.seandroidproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
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

    // just for testing ==> to be connected with backend
    var isLoggedIn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_home_page)

            frameLayout = findViewById(R.id.frame)
            coordinatorLayout = findViewById(R.id.coordinatorLayout)
            toolbar = findViewById(R.id.toolbar)
            navigationView = findViewById(R.id.navigationView)
            drawerLayout = findViewById(R.id.drawerLayout)

            val navigationView = findViewById<NavigationView>(R.id.navigationView)
            val headerView = navigationView.getHeaderView(0)
//        val navUsername = headerView.findViewById<View>(R.id.txtName) as TextView
//        val navMobileNumber = headerView.findViewById<View>(R.id.txtNumber) as TextView

            setUpToolbar()

            val actionBarDrawerToggle = ActionBarDrawerToggle(this@HomePageActivity,drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
            )

            drawerLayout.addDrawerListener(actionBarDrawerToggle)

            actionBarDrawerToggle.syncState()

            openHome()

            navigationView.setNavigationItemSelectedListener {

                if(previousMenuItem != null){
                    previousMenuItem?.isChecked = false
                }
                it.isCheckable = true
                it.isChecked = true
                previousMenuItem = it

                when(it.itemId){
                    R.id.home ->{
//                        supportFragmentManager.beginTransaction().replace(
//                            R.id.frame,
//                            // hardcoded defaults
//                            AllItemsFragment("517619", "ewaste")
//                        ).commit()
//                        drawerLayout.closeDrawers()
//                        supportActionBar?.title = "All Items"
                        openHome()
                    }
                    R.id.wishList ->{
                        supportFragmentManager.beginTransaction().replace(
                            R.id.frame,
                            WishlistFragment()
                        ).commit()
                        drawerLayout.closeDrawers()
                        supportActionBar?.title = "Wishlist"
                    }
                    R.id.sell ->{
                        supportFragmentManager.beginTransaction().replace(
                            R.id.frame,
                            SellItemsFragment()
                        ).commit()
                        drawerLayout.closeDrawers()
                        supportActionBar?.title = "Post Item"
                    }

                    R.id.profile ->{
                        supportFragmentManager.beginTransaction().replace(
                            R.id.frame,
                            ProfileFragment()
                        ).commit()
                        drawerLayout.closeDrawers()
                        supportActionBar?.title = "My Profile"
                    }

                    R.id.listings ->{
                        supportFragmentManager.beginTransaction().replace(
                            R.id.frame,
                            MyListingsFragment()
                        ).commit()
                        drawerLayout.closeDrawers()
                        supportActionBar?.title = "My Listings"
                    }

//                R.id.drawer_logout ->{
//                    supportFragmentManager.beginTransaction().replace(
//                        R.id.frame,
//                        LogoutFragment()
//                    ).commit()
//                    drawerLayout.closeDrawers()
//                    supportActionBar?.title = "Logout"
//                }
                }

                return@setNavigationItemSelectedListener true
            }

    }

    private fun openHome(){
        supportFragmentManager.beginTransaction().replace(
            R.id.frame,
            // hardcoded defaults
            AllItemsFragment("517619", "ewaste")
        ).addToBackStack("Home").commit()
        drawerLayout.closeDrawers()
        supportActionBar?.title = "All Items"
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
            !is AllItemsFragment ->openHome()
            else->super.onBackPressed()
        }

    }

}
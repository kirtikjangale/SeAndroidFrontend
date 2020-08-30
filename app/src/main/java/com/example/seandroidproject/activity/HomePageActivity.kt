package com.example.seandroidproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
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

    lateinit var sharedPreferences: SharedPreferences

    lateinit var btnLogin : Button
    lateinit var btnSignUp : Button
    lateinit var btnLogout : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)

        if (isLoggedIn) {
            setContentView(R.layout.activity_home_page)

            frameLayout = findViewById(R.id.frame)
            coordinatorLayout = findViewById(R.id.coordinatorLayout)
            toolbar = findViewById(R.id.toolbar)
            navigationView = findViewById(R.id.navigationView)
            drawerLayout = findViewById(R.id.drawerLayout)

            val navigationView = findViewById<NavigationView>(R.id.navigationView)
            val headerView = navigationView.getHeaderView(0)


            val navUsername = headerView.findViewById<View>(R.id.txtName) as TextView
            val navMobileNumber = headerView.findViewById<View>(R.id.txtNumber) as TextView

            navUsername.text = sharedPreferences.getString("userName","Name")
            navMobileNumber.text = sharedPreferences.getString("userPhone","Mobile Number")

            btnLogout = headerView.findViewById<View>(R.id.drawer_logout) as Button

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
                       val intent = Intent(this@HomePageActivity,SellActivity::class.java)
                        startActivity(intent)
                        finish()
                       // drawerLayout.closeDrawers()
                        //supportActionBar?.title = "Post Item"
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

            btnLogout.setOnClickListener {
                sharedPreferences.edit().clear().commit()
                val intent = Intent(this@HomePageActivity,HomePageActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
        else {
            setContentView(R.layout.activity_home_page_nouser)

            frameLayout = findViewById(R.id.frame)
            coordinatorLayout = findViewById(R.id.coordinatorLayout)
            toolbar = findViewById(R.id.toolbar)
            navigationView = findViewById(R.id.navigationView)
            drawerLayout = findViewById(R.id.drawerLayout)

            val navigationView = findViewById<NavigationView>(R.id.navigationView)
            val headerView = navigationView.getHeaderView(0)
//            val navUsername = headerView.findViewById<View>(R.id.txtName) as TextView
//            val navMobileNumber = headerView.findViewById<View>(R.id.txtNumber) as TextView

            btnLogin = headerView.findViewById<View>(R.id.drawer_login) as Button
            btnSignUp = headerView.findViewById<View>(R.id.drawer_signup) as Button



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



                return@setNavigationItemSelectedListener true
            }


            btnLogin.setOnClickListener {
                val intent = Intent(this@HomePageActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            btnSignUp.setOnClickListener {
                val intent = Intent(this@HomePageActivity,RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }


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
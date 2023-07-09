package com.dev.android.meetme.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.dev.android.meetme.R
import com.dev.android.meetme.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding

    var actionBarDrawerToggle:ActionBarDrawerToggle?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Start Bottom Navigation
        val navHost=supportFragmentManager.findFragmentById(R.id.fragmentContainerView)!!.findNavController()

        val popupMenu=PopupMenu(this,null)
        popupMenu.inflate(R.menu.bottom_menu)
        binding.bottomBar.setupWithNavController(popupMenu.menu,navHost)

        navHost.addOnDestinationChangedListener(object :NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                title=when(destination.id){
                    R.id.homeFragment2->"Home"
                    R.id.mesageFragment->"Chat"
                    R.id.profileFragment2->"Account"

                    else ->"Home"
                }
            }
        })
        //End Bottom Navigation

        //Start Drawable layout
        actionBarDrawerToggle= ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        //Start Drawable layout

    }
   //Drawable item click events
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.fav ->{

            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  if(actionBarDrawerToggle!!.onOptionsItemSelected(item)) {
             true
        }else{
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.close()
        }else{
//            super.onBackPressed()
          onBackPressedDispatcher.onBackPressed()
        }
    }
}
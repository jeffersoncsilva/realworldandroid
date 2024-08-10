package com.jefferson.apps.real_world.android.real_worldandroidapp.common

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.jefferson.apps.real_world.android.real_worldandroidapp.R
import com.jefferson.apps.real_world.android.real_worldandroidapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private val appBarConfiguration by lazy { AppBarConfiguration(topLevelDestinationIds = setOf(R.id.animalsNearYou, R.id.search)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Base_Theme_RealWorldAndroidApp)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.findNavController()
        setupActionBar()
        setupBottomNav()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupBottomNav(){
        binding.bottomNavigation.setupWithNavController(navController)
    }
}
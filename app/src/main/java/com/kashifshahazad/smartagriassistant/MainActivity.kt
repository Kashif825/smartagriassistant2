package com.kashifshahazad.smartagriassistant



import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    companion object {
        var adminUid = "YIIQGVn8hoVVcJ7eiZaH21ADEyz1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val toolbar: Toolbar =findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val imageView = findViewById<ImageView>(R.id.drawer_icon)
        imageView.setOnClickListener { view: View? ->
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val bottomNavigationView = findViewById<BottomNavigationView>(
            R.id.bottomNavigation)
        bottomNavigationView.setupWithNavController(navHostFragment.navController)

        findViewById<FloatingActionButton>(R.id.addNew).setOnClickListener(){
             startActivity(Intent(this, add_crop_issue::class.java))
        }

        val viewModel = MainViewModel()

        lifecycleScope.launch {
            viewModel.currentUser.collect {
                if (it==null){
                    startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                    finish()
                }
            }
        }

        askNotificationPermission()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_logout) {
            val viewModel = AuthViewModel()
            viewModel.logout()
            startActivity(Intent(this, LoginActivity::class.java))


        } else if (item.itemId == R.id.item_about_us) {
          startActivity(Intent(this, About_us::class.java))
        }
        return true
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED && !shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            Log.d("FCM", "Fetching FCM registration")

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                Log.d("FCM", "Fetching FCM registration token failed ${task.result!!}")


            })
        }
    }

}
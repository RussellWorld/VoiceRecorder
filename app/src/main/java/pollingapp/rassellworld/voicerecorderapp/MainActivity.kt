package pollingapp.rassellworld.voicerecorderapp

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomappbar.BottomAppBar
import pollingapp.rassellworld.voicerecorderapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
lateinit var navController: NavController
lateinit var binding: ActivityMainBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)


        navController = Navigation.findNavController(this, R.id.navHostFragmentContainer)
        NavigationUI.setupWithNavController(
            binding.navBottom, navController
        )
    }

    @Suppress("DEPRECATION")
    fun isServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("pollingapp.rassellworld.voicerecorderapp.record.RecordService" == service.service.className) {
                return true
            }
        }
        return false
    }

}
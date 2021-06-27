package pollingapp.rassellworld.voicerecorderapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
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
}
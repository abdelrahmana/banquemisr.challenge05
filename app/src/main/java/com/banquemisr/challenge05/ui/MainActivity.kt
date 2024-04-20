package com.banquemisr.challenge05.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.banquemisr.challenge05.R
import com.banquemisr.challenge05.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
         setContent {
        setContentView(binding.root)
         }
         handleOnBackPressed()
        }

    private fun handleOnBackPressed() {
        onBackPressedDispatcher.addCallback(this@MainActivity, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController(R.id.nav_host_fragment).navigateUp()
            }
        })
    }


}


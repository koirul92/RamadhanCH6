package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityCoroutinesBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class CoroutinesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoroutinesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHello.setOnClickListener {
            GlobalScope.async {
                kenalan()
            }
        }

    }
    suspend fun kenalan(){
        delay(1000)
        Log.d("Kenalan!", "Halo namaku Anwar!")
    }
}
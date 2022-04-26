package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding:ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            btnHandler.setOnClickListener {
                startActivity(
                    Intent(this@MainActivity2, MainActivity::class.java)
                )
            }
            btnAsyncTask.setOnClickListener {
                startActivity(
                    Intent(this@MainActivity2, AsyncTaskActivity::class.java)
                )
            }
            btnCoroutines.setOnClickListener {
                startActivity(
                    Intent(this@MainActivity2, CoroutinesActivity::class.java)
                )
            }
        }
    }
}
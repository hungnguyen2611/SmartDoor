package com.example.smartdoor_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartdoor_app.databinding.ActivityUserItemBinding


class UserItemActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
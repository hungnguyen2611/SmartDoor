package com.example.smartdoor_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.smartdoor_app.databinding.ActivityProfileBinding
import com.example.smartdoor_app.databinding.ActivityProfileViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ProfileView : AppCompatActivity() {
    private lateinit var binding: ActivityProfileViewBinding

    private lateinit var actionBar: ActionBar

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Profile View"

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        binding.HomeBtn.setOnClickListener{
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        binding.editBtn.setOnClickListener{
            startActivity(Intent(this, EditActivity::class.java))
            finish()
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
        super.onBackPressed()
    }


    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        database = FirebaseDatabase.getInstance().getReference("Users")
        if(firebaseUser != null){
            val email = firebaseUser.email
            if (email != null) {
                val emailwithcomma = email.replace('.', ',')
                database.child(emailwithcomma).get().addOnSuccessListener {
                    if (it.exists()){
                        val avt_url = it.child("avatar").value as String
                        binding.emailTv.text = email
                        binding.username.text = it.child("username").value as String
                        binding.email.text = email
                        binding.fullname.text = it.child("fullname").value as String
                        val role = it.child("role").value as String
                        binding.role.text = role
                        binding.admin.text = "hung2001"
                        Picasso.get().load(avt_url).into(binding.imageView)
                    }
                    else{
                        Toast.makeText(this, "User doesn't exist", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }

            }
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


}
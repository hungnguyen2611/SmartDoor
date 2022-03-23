package com.example.smartdoor_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.smartdoor_app.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private lateinit var actionBar: ActionBar

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var database : DatabaseReference

    private var admin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        binding.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }

        binding.profileBtn.setOnClickListener{
            startActivity(Intent(this, ProfileView::class.java))
            finish()
        }

        binding.historyBtn.setOnClickListener{
            if(admin) {
                startActivity(Intent(this, HistoryActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(this, "This feature is available to Admin only", Toast.LENGTH_SHORT).show()
            }
        }

        binding.memberlistBtn.setOnClickListener{
            if(admin) {
                startActivity(Intent(this, MemberListActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(this, "This feature is available to Admin only", Toast.LENGTH_SHORT).show()
            }
        }

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
                        val fullname = it.child("fullname").value
                        val role = it.child("role").value
                        if (role == "admin")
                            admin = true
                        binding.emailTv.text = email
                        binding.welcomeTv.text = "Welcome "+ fullname
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
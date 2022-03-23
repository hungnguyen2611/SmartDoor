package com.example.smartdoor_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_member_list.*
import kotlinx.android.synthetic.main.user_popup.view.*

class ListActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference

    private lateinit var recordRecyclerview : RecyclerView

    private lateinit var recordArrayList : ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recordRecyclerview = findViewById(R.id.member_list)
        recordRecyclerview.layoutManager = LinearLayoutManager(this)
        recordRecyclerview.setHasFixedSize(true)

        recordArrayList = arrayListOf<User>()
        getMemberData()

        backButton.setOnClickListener {
            startActivity(Intent(this, MemberListActivity::class.java))
            finish()
        }

    }
    override fun onBackPressed() {
        startActivity(Intent(this, MemberListActivity::class.java))
        finish()
        super.onBackPressed()
    }

    private fun getMemberData() {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val mem = userSnapshot.getValue(User::class.java)
                        recordArrayList.add(mem!!)
                    }
                    var adapter = MyMemAdapter(recordArrayList)
                    recordRecyclerview.adapter = adapter
                    adapter.setOnItemClickListener(object : MyMemAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val mDialogView = LayoutInflater.from(this@ListActivity).inflate(R.layout.user_popup, null)
                            val mBuilder = AlertDialog.Builder(this@ListActivity).setView(mDialogView).setTitle("Image")
                            val mAlertDialog = mBuilder.show()

                            Picasso.get().load(recordArrayList[position].avatar).resize(800,800).into(mDialogView.imageView)

                            mDialogView.addtolistBtn.setOnClickListener{
                                var emailwithcomma = recordArrayList[position].email.toString()
                                emailwithcomma = emailwithcomma.replace('.',',')
                                database.child(emailwithcomma).child("role").setValue("member")
                                Toast.makeText(this@ListActivity, "Add member successful", Toast.LENGTH_SHORT).show()
                                mAlertDialog.dismiss()
                                onBackPressed()
                            }
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
}
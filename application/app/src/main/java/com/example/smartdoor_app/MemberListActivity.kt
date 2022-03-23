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
import kotlinx.android.synthetic.main.activity_pop_up_window.view.*
import kotlinx.android.synthetic.main.activity_pop_up_window.view.imageView
import kotlinx.android.synthetic.main.activity_pop_up_window.view.okbutton
import kotlinx.android.synthetic.main.member_pop_up.view.*

class MemberListActivity : AppCompatActivity() {
    private lateinit var database : DatabaseReference

    private lateinit var recordRecyclerview : RecyclerView

    private lateinit var recordArrayList : ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_list)

        recordRecyclerview = findViewById(R.id.member_list)
        recordRecyclerview.layoutManager = LinearLayoutManager(this)
        recordRecyclerview.setHasFixedSize(true)

        recordArrayList = arrayListOf<User>()
        getMemberData()

        backButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        addBtn.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
            finish()
        }

    }
    override fun onBackPressed() {
        startActivity(Intent(this, ProfileActivity::class.java))
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
                        if (mem != null) {
                            if (mem.role == "member") {
                                recordArrayList.add(mem)
                            }
                        }
                    }
                    var adapter = MyMemAdapter(recordArrayList)
                    recordRecyclerview.adapter = adapter
                    adapter.setOnItemClickListener(object : MyMemAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val mDialogView = LayoutInflater.from(this@MemberListActivity).inflate(R.layout.member_pop_up, null)
                            val mBuilder = AlertDialog.Builder(this@MemberListActivity).setView(mDialogView).setTitle("Image")
                            val mAlertDialog = mBuilder.show()

                            Picasso.get().load(recordArrayList[position].avatar).resize(800,800).into(mDialogView.imageView)

                            mDialogView.okbutton.setOnClickListener{
                                mAlertDialog.dismiss()
                            }
                            mDialogView.deletebutton.setOnClickListener {
                                var emailwithcomma = recordArrayList[position].email.toString()
                                emailwithcomma = emailwithcomma.replace('.',',')
                                database.child(emailwithcomma).child("role").setValue("user")
                                Toast.makeText(this@MemberListActivity, "Add member successful", Toast.LENGTH_SHORT).show()
                                mAlertDialog.dismiss()
                                onBackPressed()
                            }
                            Toast.makeText(this@MemberListActivity, "you clicked on item no. $position", Toast.LENGTH_SHORT).show()
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
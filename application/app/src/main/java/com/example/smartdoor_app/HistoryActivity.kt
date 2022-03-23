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
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_pop_up_window.view.*


class HistoryActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference

    private lateinit var recordRecyclerview : RecyclerView

    private lateinit var recordArrayList : ArrayList<record>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recordRecyclerview = findViewById(R.id.record_list)
        recordRecyclerview.layoutManager = LinearLayoutManager(this)
        recordRecyclerview.setHasFixedSize(true)

        recordArrayList = arrayListOf<record>()
        getHistoryData()

        backBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
        super.onBackPressed()
    }

    private fun getHistoryData() {
        database = FirebaseDatabase.getInstance().getReference("History")
        database.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val record = userSnapshot.getValue(record::class.java)
                        recordArrayList.add(record!!)
                    }
                    var adapter = MyAdapter(recordArrayList)
                    recordRecyclerview.adapter = adapter
                    adapter.setOnItemClickListener(object : MyAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val mDialogView = LayoutInflater.from(this@HistoryActivity).inflate(R.layout.activity_pop_up_window, null)
                            val mBuilder = AlertDialog.Builder(this@HistoryActivity).setView(mDialogView).setTitle("Image")
                            val mAlertDialog = mBuilder.show()

                            Picasso.get().load(recordArrayList[position].url).resize(800,800).into(mDialogView.imageView)

                            mDialogView.okbutton.setOnClickListener{
                                mAlertDialog.dismiss()
                            }
                            Toast.makeText(this@HistoryActivity, "you clicked on item no. $position", Toast.LENGTH_SHORT).show()
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
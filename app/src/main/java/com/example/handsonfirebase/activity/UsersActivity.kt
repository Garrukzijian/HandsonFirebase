package com.example.handsonfirebase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.handsonfirebase.R
import com.example.handsonfirebase.adapter.UserAdapter
import com.example.handsonfirebase.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_users.*
import java.util.ArrayList

class UsersActivity : AppCompatActivity() {
    var userlist = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        val sendId= intent.getStringExtra("sendId")
//        FirebaseService.sharePref =getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener{
//            FirebaseService.token=it.token
//
//        }
        userRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        var userAdapter = UserAdapter(this, userlist)
        userRecyclerView.adapter = userAdapter
        imageBack.setOnClickListener{
            onBackPressed()
        }
//        imgProfile.setOnClickListener{
//            val intent = Intent(this@UsersActivity, ProfileActivity::class.java)
//            startActivity(intent)
//        }
        getUserList(sendId!!)
    }

    fun getUserList(sendId:String) {
        val firebase = FirebaseAuth.getInstance().currentUser!!
        var databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                val currentUser = snapshot.getValue(User::class.java)
                if (currentUser!!.profileImage == "") {
                    imgProfile.setImageResource(R.drawable.ic_back)
                } else {
                    Glide.with(this@UsersActivity).load(currentUser.profileImage).into(imgProfile)
                }
                for (dataSnapShot:DataSnapshot in snapshot.children) {
                    val user=dataSnapShot.getValue(User::class.java)
                    if (user!!.userId.equals(sendId)) {
                        userlist.add(user)
                    }
                }
                val userAdapter = UserAdapter(this@UsersActivity, userlist)
                userRecyclerView.adapter = userAdapter

            }

        })
    }
}
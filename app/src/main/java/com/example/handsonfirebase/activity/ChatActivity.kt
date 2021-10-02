package com.example.handsonfirebase.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.handsonfirebase.R
import com.example.handsonfirebase.adapter.ChatAdapter
import com.example.handsonfirebase.model.Chat
import com.example.handsonfirebase.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_users.*

import java.util.ArrayList

class ChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var refernce: DatabaseReference? = null
    var chatlist = ArrayList<Chat>()
//    var topic =""
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)

        val intent = getIntent()
        val userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refernce = FirebaseDatabase.getInstance().getReference("Users")
        imageBack_chat.setOnClickListener {
            onBackPressed()
        }
        refernce!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.userName
                if (user.profileImage == "") {
                    imgProfile_chat.setImageResource(R.drawable.ic_back)
                } else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(imgProfile)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        btnSendMessage.setOnClickListener {
            var message: String = etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId!!, message)
                etMessage.setText("")
//                topic="/topics/$userId"
//                PushNotification(
//                    NotificationData(userName!!,message)
//                    ,topic).also {
//                    sendNotification(it)
//
//                }
                }
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Chat")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatlist.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    chatlist.add(chat!!)
//                    if (chat!!.senderId.equals(firebaseUser!!.uid) && chat!!.reciverId.equals(userId) ||
//                        chat!!.senderId.equals(userId) && chat!!.reciverId.equals(firebaseUser!!.uid)
//                    ) {
//                        chatlist.add(chat)
//                    }
                }
                val chatAdapter = ChatAdapter(this@ChatActivity, chatlist)
                chatRecyclerView.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    btn_finish_order.setOnClickListener {
        delete(userId!!)

    }
}
    private fun delete(sendId: String) {
        var refernce: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
        val refUser = refernce!!.child("Take_Order")

        refUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val DeleteId = childSnapshot.child("TakerId").value
                    if (sendId.equals(DeleteId)) {
                        refUser.child("Take_Order").child(sendId).removeValue()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var refernce: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("sendId", senderId)
        hashMap.put("reciverId", receiverId)
        hashMap.put("message",  message)
        refernce!!.child("Chat").push().setValue(hashMap)
    }
    private fun transport_data(
        TakerId: String,
        Product: String,
        Date: String,
        Price: String,
        Location: String
    ) {
        var refernce: DatabaseReference? = FirebaseDatabase.getInstance().getReference()
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.put("TakerId", TakerId)
        hashMap.put("Product", Product)
        hashMap.put("Date", Date)
        hashMap.put("Location", Location)
        hashMap.put("Price", Price)
        refernce!!.child("History Order").push().setValue(hashMap)
    }

//    private fun sendNotification(notification: PushNotification)= CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val response = Retrofitlnstance.api.postNotification(notification)
//            if(response.isSuccessful){
//                Toast.makeText(this@ChatActivity,"Response ${Gson().toJson(response)}", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(this@ChatActivity,response.errorBody().toString(), Toast.LENGTH_SHORT).show()
//            }
//        }catch (e: Exception){
//            Toast.makeText(this@ChatActivity,e.message, Toast.LENGTH_SHORT).show()
//
//        }
//    }
}
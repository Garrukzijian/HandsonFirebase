package com.example.handsonfirebase.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.handsonfirebase.R
import com.example.handsonfirebase.activity.ChatActivity
import com.example.handsonfirebase.model.User
import com.makeramen.roundedimageview.RoundedImageView

class UserAdapter(private val context: Context, private val userlist:List<User>):
     RecyclerView.Adapter<UserAdapter.ViewHolder>(){
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val view=LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
         return ViewHolder(view)
     }
     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val user=userlist[position]
         holder.txtUserName.text=user.userName
         Glide.with(context).load(user.profileImage).placeholder(R.drawable.ic_back).into(holder.imgUser)

         holder.layoutUser.setOnClickListener {
             val intent=Intent(context,ChatActivity::class.java)
             intent.putExtra("userId",userlist[holder.adapterPosition].userId)
             intent.putExtra("userName",userlist[holder.adapterPosition].userName)
             context.startActivity(intent)
         }
     }

     override fun getItemCount(): Int {
         return userlist.size
     }
     class ViewHolder(view:View):RecyclerView.ViewHolder(view){

         val txtUserName:TextView=view.findViewById(R.id.userName)
         val txtTemp:TextView=view.findViewById(R.id.temp)
         val imgUser:RoundedImageView=view.findViewById(R.id.userImage)
         val layoutUser:LinearLayout=view.findViewById(R.id.layotUser)
     }
 }
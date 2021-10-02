package com.example.handsonfirebase.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.handsonfirebase.R
import com.example.handsonfirebase.model.TakeOrder
import com.example.handsonfirebase.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_buy.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_take_order.*
import kotlinx.android.synthetic.main.item_take_order.*
import kotlinx.android.synthetic.main.item_take_order.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TakeOrderActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    val take_orderlist = ArrayList<TakeOrder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_order)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        image_take_order.setOnClickListener {
            onBackPressed()
        }
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Order")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                take_orderlist.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    tvUserName_take_order.text = user!!.userName
                    val takeOrder = dataSnapShot.getValue(TakeOrder::class.java)
                    var sendId=takeOrder!!.sendId
                    if (firebaseUser!!.uid.equals(sendId)) {
                    }else{
                        take_orderlist.add(takeOrder!!)
                        take_Order_recyclerView.layoutManager =
                            LinearLayoutManager(this@TakeOrderActivity)
                        take_Order_recyclerView.adapter = MyAdapter_takeOrder(take_orderlist)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        take_Order_recyclerView.layoutManager = LinearLayoutManager(this)
        take_Order_recyclerView.adapter = MyAdapter_takeOrder(take_orderlist)
    }

    inner class MyAdapter_takeOrder(val takeorderlist: List<TakeOrder>) :
        RecyclerView.Adapter<MyAdapter_takeOrder.MyviewHolder_takeOrder>() {
        inner class MyviewHolder_takeOrder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var product: TextView = itemView.Product_take_order
            var price: TextView = itemView.Price_take_order
            var date: TextView = itemView.Date_take_order
            var loccation: TextView = itemView.location_take_order

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder_takeOrder {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_take_order, parent, false)
            return MyviewHolder_takeOrder(itemView)
        }

        override fun onBindViewHolder(holder: MyviewHolder_takeOrder, position: Int) {
            val takeorder = takeorderlist[position]
            holder.product.text = takeorder.Product
            holder.price.text = takeorder.Price
            holder.date.text = takeorder.Date
            holder.loccation.text = takeorder.Location
            holder.itemView.setOnClickListener {
                val Product = take_orderlist[holder.adapterPosition].Product
                val Location =take_orderlist[holder.adapterPosition].Location
                val Price = take_orderlist[holder.adapterPosition].Price
                var time = get_takeorder_Now()
                transport_data(firebaseUser!!.uid, Product, time, Price, Location)
                val intent = Intent(this@TakeOrderActivity,UsersActivity ::class.java)
                intent.putExtra("Location", take_orderlist[holder.adapterPosition].Location)
                intent.putExtra("Product", take_orderlist[holder.adapterPosition].Product)
                intent.putExtra("Price", take_orderlist[holder.adapterPosition].Price)
                intent.putExtra("time", time)
                intent.putExtra("sendId", take_orderlist[holder.adapterPosition].sendId)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return takeorderlist.size
        }
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
        refernce!!.child("Take_Order").push().setValue(hashMap)
    }

    fun get_takeorder_Now(): String {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Date())
        } else {
            var tms = Calendar.getInstance()
            return tms.get(Calendar.YEAR).toString() + "-" + tms.get(Calendar.MONTH)
                .toString() + "-" + tms.get(
                Calendar.DAY_OF_MONTH
            ).toString() + " " + tms.get(Calendar.HOUR_OF_DAY).toString() + ":" + tms.get(
                Calendar.MINUTE
            ).toString() + ":" + tms.get(Calendar.SECOND).toString() + "." + tms.get(
                Calendar.MILLISECOND
            ).toString()
        }
    }
}


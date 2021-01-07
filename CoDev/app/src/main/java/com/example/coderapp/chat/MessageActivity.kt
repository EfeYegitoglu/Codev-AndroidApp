package com.example.coderapp.chat

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coderapp.R
import com.example.coderapp.profile.ProfileResponse
import com.example.coderapp.retrofit.ApiUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageActivity : AppCompatActivity() {

    private lateinit var dataBase: FirebaseFirestore
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageRecyclerView: RecyclerView
    private var userIdList: ArrayList<String> = ArrayList()
    private var messageList: ArrayList<String> =ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        toolbarMessage.title = ""
        setSupportActionBar(toolbarMessage)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        val sharedPreferences = this.getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )

        val userId = sharedPreferences?.getInt("user_id", 0) as Int

        val otherProfileId = intent.getIntExtra("other_user_id_for_message", 0)

        getProfileById(otherProfileId)
        dataBase = FirebaseFirestore.getInstance()



        messageRecyclerView = findViewById(R.id.messageRecycler)
        messageRecyclerView.setHasFixedSize(true)
        messageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        messageAdapter = MessageAdapter(this,userIdList,messageList,userId)
        messageRecyclerView.adapter = messageAdapter



        btnSendMessage.setOnClickListener {
            if (editTextMessage.text.isNotEmpty()) {
                insertMessage(userId, otherProfileId)
            } else {
                Toast.makeText(this, "Mesaj Giriniz", Toast.LENGTH_SHORT).show()
            }

        }


        getMessages(userId.toString(),otherProfileId.toString())


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return super.onSupportNavigateUp()
    }

    private fun getProfileById(userId: Int) {
        val pdi = ApiUtils.profileDaoInterface()
        pdi.profileQuery(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>?
            ) {
                if (response != null) {
                    val success = response.body()!!.success
                    if (success == 1) {
                        val profile = response.body()!!.profiles

                        for (p in profile) {
                            otherProfileMessageName.text = p.profile_name
                            if (p.profile_picture.isNotEmpty()) {
                                Picasso.get()
                                    .load("http://codevsoftware.tk/coderapp/profileimages/" + p.profile_picture + ".jpg")
                                    .into(otherProfileMessagePicture)
                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

            }
        })
    }

    private fun insertMessage(userId: Int, otherUserId: Int) {


        val messageMap = hashMapOf<String, String>()
        messageMap["userId"] = userId.toString()
        messageMap["otherUserId"] = otherUserId.toString()
        messageMap["message"] = editTextMessage.text.toString()
        messageMap["time"] = com.google.firebase.Timestamp.now().toString()



        dataBase.collection("Messages").document(userId.toString()).collection(otherUserId.toString()).add(messageMap).addOnCompleteListener { task ->

            if (task.isComplete && task.isSuccessful) {

                dataBase.collection("Messages").document(otherUserId.toString()).collection(userId.toString()).add(messageMap).addOnCompleteListener{task ->
                    if (task.isComplete && task.isSuccessful){
                        editTextMessage.setText("")
                    }
                }
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Mesajınız Gönderilemedi", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getMessages(user: String,other:String) {
        dataBase.collection("Messages").document(user.toString()).collection(other.toString()).orderBy("time",Query.Direction.ASCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {

                        messageList.clear()
                        userIdList.clear()

                        val documents = snapshot.documents

                        for (document in documents){
                            val userId = document.get("userId") as String
                            val message = document.get("message") as String

                            userIdList.add(userId.toString())
                            messageList.add(message)


                            messageAdapter.notifyDataSetChanged()
                        }


                    }
                }
            }
        }
    }




}
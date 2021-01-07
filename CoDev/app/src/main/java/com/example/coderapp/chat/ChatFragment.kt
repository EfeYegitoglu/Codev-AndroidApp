package com.example.coderapp.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coderapp.R
import com.google.firebase.firestore.FirebaseFirestore


class ChatFragment : Fragment() {

    private val database:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val sharedPreferences = context?.getSharedPreferences(
        "account_info",
        Context.MODE_PRIVATE
    )

    private var userId:Int?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_chat, container, false)

        userId = sharedPreferences?.getInt("user_id", 0)

        


        return view
    }




}
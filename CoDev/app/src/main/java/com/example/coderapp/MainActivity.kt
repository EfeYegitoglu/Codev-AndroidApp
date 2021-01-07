package com.example.coderapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.coderapp.account.LogInActivity
import com.example.coderapp.account.UserResponse
import com.example.coderapp.account.VerificationActivity
import com.example.coderapp.category.CategoryFragment
import com.example.coderapp.chat.ChatFragment
import com.example.coderapp.post.AddPostActivity
import com.example.coderapp.profile.CreateProfileActivity
import com.example.coderapp.profile.ProfileFragment
import com.example.coderapp.profile.ProfileResponse
import com.example.coderapp.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var fragment: Fragment
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        sharedPreferences = getSharedPreferences("account_info", Context.MODE_PRIVATE)



        if (sharedPreferences.getInt("user_id", 0) == 0 && sharedPreferences.getString(
                "user_mail",
                null
            ) == null
        ) {
            startActivity(Intent(this@MainActivity, LogInActivity::class.java))
            finish()
        }else{
            checkVC()

        }








        toolbar.title = "CoDev Software"
        toolbar.subtitle = "For Future"
        setSupportActionBar(toolbar)
        supportFragmentManager.beginTransaction().add(
            R.id.main_frame,
            CategoryFragment()
        ).commit()

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> {
                    fragment = CategoryFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, fragment)
                        .commit()
                }
                R.id.action_chat ->{
                    fragment = ChatFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, fragment)
                        .commit()
                }
                R.id.action_add -> {
                    val intent = Intent(applicationContext, AddPostActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_profile -> {
                    fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, fragment)
                        .commit()

                }
            }

            true
        }


    }


    private fun checkVC(){
        val userMail = sharedPreferences.getString("user_mail", null)
        val udi = ApiUtils.addUserDaoInterface()

        udi.findVCode(userMail!!).enqueue(object :Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val vc = response.body()!!.users

                for (vList in vc){
                    Log.e("vList",vList.account_state)
                    if (vList.account_state=="0"){
                        startActivity(Intent(this@MainActivity,VerificationActivity::class.java))
                        finish()
                    }else{
                        checkProfile()
                    }
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }
        })
    }

    private fun checkProfile(){
        val userId = sharedPreferences.getInt("user_id", 0)

        val pdi = ApiUtils.profileDaoInterface()

        pdi.profileQuery(userId)
            .enqueue(
                object : Callback<ProfileResponse> {
                    override fun onResponse(
                        call: Call<ProfileResponse>?,
                        response: Response<ProfileResponse>?
                    ) {
                        if (response != null) {
                            Log.e(
                                "profileSuccess",
                                response.body()?.success.toString()
                            )

                            if (response.body()?.success == 2) {

                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        CreateProfileActivity::class.java
                                    )
                                )
                                finish()
                            }

                        }


                    }

                    override fun onFailure(
                        call: Call<ProfileResponse>,
                        t: Throwable
                    ) {

                    }
                })
    }


}

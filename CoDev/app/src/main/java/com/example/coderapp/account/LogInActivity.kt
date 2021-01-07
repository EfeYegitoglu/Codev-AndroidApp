package com.example.coderapp.account

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.coderapp.MainActivity
import com.example.coderapp.R
import com.example.coderapp.profile.CreateProfileActivity
import com.example.coderapp.profile.ProfileResponse
import com.example.coderapp.retrofit.ApiUtils
import com.example.coderapp.retrofit.CRUDResponse
import kotlinx.android.synthetic.main.activity_log_in.*
import maes.tech.intentanim.CustomIntent
import maes.tech.intentanim.CustomIntent.customType
import maes.tech.intentanim.CustomIntent.customType

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LogInActivity : AppCompatActivity() {

    var control = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        goSignUpText.setOnClickListener {



            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            CustomIntent.customType(
               this,
                "left-to-right"
            );
        }





        loginButton.setOnClickListener {



            if (mailValidate(loginMail.text.toString().trim())){
                logIn()
            }else{

                Toast.makeText(
                    this@LogInActivity,
                    "Geçerli formatta mail adresi giriniz",
                    Toast.LENGTH_SHORT
                ).show()
            }


        }
    }

    private fun logIn() {

        val progressDialog = ProgressDialog(this@LogInActivity)
        progressDialog.setTitle("Giriş Yapılıyor")
        progressDialog.setMessage("Lütfen Bekleyin...")
        progressDialog.show()

        val loginMail = loginMail.text.toString().trim()
        val loginPassword = loginPassword.text.toString().trim()

        if (loginMail.isNotEmpty() && loginPassword.isNotEmpty()) {
            val udi = ApiUtils.addUserDaoInterface()
            udi.logIn(loginMail, loginPassword).enqueue(object : Callback<CRUDResponse> {
                override fun onResponse(
                    call: Call<CRUDResponse>,
                    response: Response<CRUDResponse>
                ) {
                    Log.e("loginSuccess", response.body()!!.success.toString())
                    Log.e("loginMessage", response.body()!!.message)

                    if (response.body()!!.success == 2) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@LogInActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    } else {
                        progressDialog.dismiss()
                        udi.findVCode(loginMail).enqueue(object : Callback<UserResponse> {
                            override fun onResponse(
                                call: Call<UserResponse>,
                                response: Response<UserResponse>
                            ) {
                                Log.e("loginUsers", response.body()!!.users.toString())
                                val user = response.body()!!.users
                                for (ul in user) {
                                    if (ul.account_state == "0") {
                                        Log.e("loginState", ul.account_state)

                                        val sharedPreferences = getSharedPreferences(
                                            "account_info",
                                            Context.MODE_PRIVATE
                                        )
                                        val spEditor = sharedPreferences.edit()
                                        spEditor.putString("user_mail", loginMail)
                                        spEditor.putInt("user_id", ul.user_id)
                                        spEditor.apply()

                                        startActivity(
                                            Intent(
                                                this@LogInActivity,
                                                VerificationActivity::class.java
                                            )
                                        )

                                    } else {
                                        Log.e("loginState", ul.account_state)

                                        val sharedPreferences = getSharedPreferences(
                                            "account_info",
                                            Context.MODE_PRIVATE
                                        )
                                        val spEditor = sharedPreferences.edit()
                                        spEditor.putString("user_mail", loginMail)
                                        spEditor.putInt("user_id", ul.user_id)
                                        spEditor.apply()

                                        control = true
                                        Log.e("control",control.toString())
                                        if (control){
                                            getProfile()
                                        }


                                    }
                                }
                            }

                            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                            }
                        })
                    }
                    Toast.makeText(
                        this@LogInActivity,
                        response.body()!!.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

                }
            })
        } else {
            Toast.makeText(this@LogInActivity, "Gerekli Alanları Doldurunuz", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getProfile(){
        val sharedPreferences = getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )
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
                                        this@LogInActivity,
                                        CreateProfileActivity::class.java
                                    )
                                )
                                finish()


                            } else if (response.body()?.success == 1) {

                                startActivity(
                                    Intent(
                                        this@LogInActivity,
                                        MainActivity::class.java
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

    private fun mailValidate(text: String?): Boolean {
        var p = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
        )
        var m = p.matcher(text)
        return m.matches()
    }


}

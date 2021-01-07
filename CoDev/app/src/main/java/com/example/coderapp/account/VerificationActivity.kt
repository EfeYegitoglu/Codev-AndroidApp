package com.example.coderapp.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coderapp.R
import com.example.coderapp.profile.CreateProfileActivity
import com.example.coderapp.retrofit.ApiUtils
import com.example.coderapp.retrofit.CRUDResponse
import kotlinx.android.synthetic.main.activity_verification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationActivity : AppCompatActivity() {
    private lateinit var userMail: String
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        sharedPreferences = getSharedPreferences("account_info", Context.MODE_PRIVATE)
        userMail = sharedPreferences.getString("user_mail", "Mail Gelmedi")!!
        Log.e("mailAddress", userMail)

        verificationText.text =
            userMail + "\n\n" + "Hesabına gönderilen onay kodunu aşağıdaki gerekli alana yapıştırınız ve onaylaya tıklayınız."


        confirmVerification.setOnClickListener {
            setVCode()
        }

        turnSignUpButton.setOnClickListener {
            val udi = ApiUtils.addUserDaoInterface()
            udi.deleteUser(userMail).enqueue(object : Callback<CRUDResponse>{
                override fun onResponse(call: Call<CRUDResponse>?, response: Response<CRUDResponse>?) {
                    if (response != null){
                        Log.e("başarı", response.body()?.success.toString())
                        Log.e("mailAddress", "Hesap Silindi")
                        Toast.makeText(
                            this@VerificationActivity,
                            "Mail adresiniz silindi. Tekrardan kullanabilirsiniz",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@VerificationActivity,SignUpActivity::class.java))
                        finish()
                    }

                }
                override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

                }

            })

        }


    }

    private fun setVCode() {

        val udi = ApiUtils.addUserDaoInterface()
        udi.findVCode(userMail).enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {}

            override fun onResponse(call: Call<UserResponse>?, response: Response<UserResponse>?) {
                if (response != null) {
                    val verificationList = response.body()!!.users
                    for (vl in verificationList) {
                        Log.e("mailAddress", vl.toString())

                        if (verificationCode.text.toString().isNotEmpty()) {
                            if (verificationCode.text.toString() == vl.verification_code) {
                                updateVerificationCode()
                                Toast.makeText(
                                    this@VerificationActivity,
                                    "Hesabınız Doğrulandı",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val edit = sharedPreferences.edit()
                                edit.putInt("user_id", vl.user_id)
                                edit.apply()

                                startActivity(Intent(this@VerificationActivity,
                                    CreateProfileActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@VerificationActivity,
                                    "Hatalı Doğrulama Kodu",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                this@VerificationActivity,
                                "Doğrulama Kodunu Giriniz",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }


                    }
                }
            }
        })
    }


    private fun updateVerificationCode() {


        val udi = ApiUtils.addUserDaoInterface()

        udi.updateVerification(userMail).enqueue(object : Callback<CRUDResponse> {
            override fun onResponse(call: Call<CRUDResponse>?, response: Response<CRUDResponse>?) {
                if (response != null) {
                    Log.e("verificationCode", "account_state güncellendi")
                }
            }

            override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

            }
        })

    }


}

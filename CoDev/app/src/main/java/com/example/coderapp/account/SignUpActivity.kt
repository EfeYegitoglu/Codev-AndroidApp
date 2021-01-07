package com.example.coderapp.account

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.coderapp.R
import com.example.coderapp.retrofit.ApiUtils
import com.example.coderapp.retrofit.CRUDResponse
import kotlinx.android.synthetic.main.activity_sign_up.*
import maes.tech.intentanim.CustomIntent
import maes.tech.intentanim.CustomIntent.customType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val sharedPreferences = getSharedPreferences("account_info", Context.MODE_PRIVATE)

        val spEditor = sharedPreferences.edit()





        goLoginText.setOnClickListener {
            startActivity(Intent(applicationContext, LogInActivity::class.java))
            customType(this, "right-to-left");
        }

        signUpButton.setOnClickListener {


            val signUpMailEditText = signUpMail.text.toString().trim()
            val signUpPasswordEditText = signUpPassword.text.toString().trim()
            val signUpPasswordAgainEditText = signUpPasswordAgain.text.toString().trim()

            if (signUpMailEditText.isNotEmpty() && signUpPasswordEditText.isNotEmpty() && signUpPasswordAgainEditText.isNotEmpty()) {
                if (signUpPasswordEditText == signUpPasswordAgainEditText) {

                    val alertDialog = AlertDialog.Builder(this@SignUpActivity)

                    alertDialog.setTitle(signUpMailEditText)
                    alertDialog.setMessage("Mail adresi ile kayıt oluyorsunuz. Hatalı mail girmediğinizi onaylayınız.")

                    alertDialog.setPositiveButton("Onayla") { dialogInterface, i ->


                        if (mailValidate(signUpMailEditText)) {

                            if (signUpPasswordEditText.length >= 6 && signUpPasswordAgainEditText.length >= 6){
                                signUp(signUpMailEditText, signUpMailEditText, signUpPasswordEditText)

                                spEditor.putString("user_mail", signUpMailEditText)
                                spEditor.apply()
                                spEditor.commit()

                                val progressDialog = ProgressDialog(this@SignUpActivity)
                                progressDialog.setTitle("Kayıt Olunuyor")
                                progressDialog.setMessage("Lütfen Bekleyin...")
                                progressDialog.show()


                                startActivity(
                                    Intent(
                                        applicationContext,
                                        VerificationActivity::class.java
                                    )
                                )
                                finish()
                            }else{
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Şifre uzunluğu en az 6 karakter olmalıdır",
                                    Toast.LENGTH_LONG
                                ).show()
                            }



                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Geçerli formatta mail adresi giriniz",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                    alertDialog.setNegativeButton("İptal") { dialogInterface, i ->


                    }

                    alertDialog.create().show()


                } else {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Şifrenizi Kontrol Ediniz",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(this@SignUpActivity, "Boş Alan Bırakmayınız", Toast.LENGTH_LONG)
                    .show()
            }

        }


    }

    private fun signUp(mailTo: String, user_mail: String, user_password: String) {

        val udi = ApiUtils.addUserDaoInterface()
        udi.signUp(mailTo, user_mail, user_password)
            .enqueue(object : Callback<CRUDResponse> {
                override fun onResponse(
                    call: Call<CRUDResponse>,
                    response: Response<CRUDResponse>
                ) {

                }

                override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

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

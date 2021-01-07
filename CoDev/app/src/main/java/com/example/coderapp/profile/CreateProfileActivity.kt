package com.example.coderapp.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.coderapp.MainActivity
import com.example.coderapp.R
import com.example.coderapp.retrofit.ApiUtils
import com.example.coderapp.retrofit.CRUDResponse
import kotlinx.android.synthetic.main.activity_create_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private var selectedImage: Uri? = null
    private var bitmapProfilePicture:Bitmap?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_profile)

        createProfileToolbar.title = "Profilini Oluştur"
        setSupportActionBar(createProfileToolbar)

        sharedPreferences = getSharedPreferences("account_info", Context.MODE_PRIVATE)

        createMailText.text = sharedPreferences.getString("user_mail", "Mail Gelmedi")

        getProfile()

        create_profile_image.setOnClickListener {
            checkPermission()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_save -> {
                insertProfile()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            selectedImage = data.data

            try {
                if (selectedImage != null) {

                    if (Build.VERSION.SDK_INT >= 28) {

                        val source = ImageDecoder.createSource(contentResolver, selectedImage!!)
                        bitmapProfilePicture = ImageDecoder.decodeBitmap(source)
                        create_profile_image.setImageBitmap(bitmapProfilePicture)
                        Log.i("bitmap", encodeImage())

                    } else {
                        bitmapProfilePicture =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                        create_profile_image.setImageBitmap(bitmapProfilePicture)
                        Log.i("bitmap", encodeImage())
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)

        }
    }

    private fun encodeImage(): String {
        val baos = ByteArrayOutputStream()
        bitmapProfilePicture?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)

    }


    private fun insertProfile() {
        val profilePicture = encodeImage()
        val createName=createName.text.toString()
        val createInfo=createInfo.text.toString()
        val createWebSite=createWebSite.text.toString()
        val userId=sharedPreferences.getInt("user_id", 0)


        if (createName.isNotEmpty() && createInfo.isNotEmpty()){
            val pdi = ApiUtils.profileDaoInterface()
            pdi.insertProfile(profilePicture,createName,createInfo,createWebSite.trim(),userId).enqueue(object :Callback<CRUDResponse>{
                override fun onResponse(
                    call: Call<CRUDResponse>,
                    response: Response<CRUDResponse>?
                ) {
                    if (response != null){
                        if (response.body()!!.success == 1){
                            Toast.makeText(
                                this@CreateProfileActivity,
                                "Bilgileriniz Kaydedildi",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            startActivity(Intent(this@CreateProfileActivity,MainActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(
                                this@CreateProfileActivity,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }



                    }

                }

                override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {

                }
            })
        }else{
            Toast.makeText(
                this@CreateProfileActivity,
                "Zorunlu Alanları Doldurunuz",
                Toast.LENGTH_SHORT
            )
                .show()
        }




    }

    private fun getProfile(){
        val userId=sharedPreferences.getInt("user_id", 0)
        val pdi=ApiUtils.profileDaoInterface()
        pdi.profileQuery(userId).enqueue(object :Callback<ProfileResponse>{
            override fun onResponse(
                call: Call<ProfileResponse>?,
                response: Response<ProfileResponse>?
            ) {
                if (response !=null){
                    Log.e("createProfileSuccess",response.body()!!.success.toString())
                    if (response.body()!!.success == 1){
                        val profile = response.body()!!.profiles
                        for (p in profile){
                            createName.setText(p.profile_name)
                            createInfo.setText(p.profile_info)
                            createWebSite.setText(p.profile_website)
                        }
                    }
                }



            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

            }
        })
    }
}

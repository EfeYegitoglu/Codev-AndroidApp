package com.example.coderapp.post

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.coderapp.MainActivity
import com.example.coderapp.R
import com.example.coderapp.category.CategoryResponse
import com.example.coderapp.retrofit.ApiUtils
import com.example.coderapp.retrofit.CRUDResponse
import kotlinx.android.synthetic.main.activity_add_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class AddPostActivity : AppCompatActivity() {

    private val categoryList = ArrayList<String>()
    private var category: String? = null
    private lateinit var categoryAdapter: ArrayAdapter<String>
    var selectedPicture: Uri? = null
    private var bitmap_one: Bitmap? = null
    private var bitmap_two: Bitmap? = null
    private var bitmap_three: Bitmap? = null









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        toolbarAddPost.title = "Yeni Gönderi"
        setSupportActionBar(toolbarAddPost)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        getCategoryForSpinner()

        addPostImage1.setOnClickListener {
            checkPermissionFor1()
        }

        addPostImage2.setOnClickListener {
            checkPermissionFor2()
        }


        addPostImage3.setOnClickListener {
            checkPermissionFor3()
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

            selectedPicture = data.data

            try {
                if (selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {

                        val source = ImageDecoder.createSource(contentResolver, selectedPicture!!)
                        bitmap_one = ImageDecoder.decodeBitmap(source)
                        addPostImage1.setImageBitmap(bitmap_one)
                        Log.i("bitmap", encodeImageOne())

                    } else {
                        bitmap_one =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        addPostImage1.setImageBitmap(bitmap_one)
                        Log.i("bitmap", encodeImageOne())
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        if (requestCode == 3 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPicture = data.data

            try {
                if (selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {

                        val source = ImageDecoder.createSource(contentResolver, selectedPicture!!)
                        bitmap_two = ImageDecoder.decodeBitmap(source)
                        addPostImage2.setImageBitmap(bitmap_two)
                        Log.i("bitmap", encodeImageTwo())

                    } else {
                        bitmap_two =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        addPostImage2.setImageBitmap(bitmap_two)
                        Log.i("bitmap1", encodeImageTwo())
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        if (requestCode == 4 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPicture = data.data

            try {
                if (selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {

                        val source = ImageDecoder.createSource(contentResolver, selectedPicture!!)
                        bitmap_three = ImageDecoder.decodeBitmap(source)
                        addPostImage3.setImageBitmap(bitmap_three)
                        Log.i("bitmap2", encodeImageThree())

                    } else {
                        bitmap_three =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        addPostImage3.setImageBitmap(bitmap_three)
                        Log.i("bitmap2", encodeImageThree())
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkPermissionFor1() {
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

    private fun checkPermissionFor2() {
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
            startActivityForResult(intent, 3)

        }
    }

    private fun checkPermissionFor3() {
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
            startActivityForResult(intent, 4)

        }
    }

    private fun uploadPost() {

        val sharedPreferences = application.getSharedPreferences(
            "account_info",
            Context.MODE_PRIVATE
        )

        val userId = sharedPreferences?.getInt("user_id", 0)


        val postTitle = addTitle.text.toString()
        val postExplanation = addExplanation.text.toString()
        val postCategory = categoryList[add_post_spinner.selectedItemPosition]

        val pictureOne = encodeImageOne()
        val pictureTwo = encodeImageTwo()
        val pictureThree = encodeImageThree()









        val pdi = ApiUtils.postDaoInterface()
        pdi.uploadPost(
            userId!!,
            postTitle,
            postExplanation,
            postCategory,
            pictureOne,
            pictureTwo,
            pictureThree
        ).enqueue(object : Callback<CRUDResponse> {
            override fun onResponse(call: Call<CRUDResponse>, response: Response<CRUDResponse>?) {
                if (response != null){

                    if (response.body()!!.success == 1){

                        Toast.makeText(applicationContext, "Gönderiniz Paylaşıldı", Toast.LENGTH_SHORT)
                            .show()
                    }else{

                        Toast.makeText(applicationContext, "Gönderiniz Paylaşılamadı", Toast.LENGTH_SHORT)
                            .show()
                    }

                }else{
                    Toast.makeText(applicationContext, "Beklenmeyen Hata", Toast.LENGTH_SHORT)
                        .show()
                }

            }

            override fun onFailure(call: Call<CRUDResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Başarısız", Toast.LENGTH_SHORT).show()
            }
        })


    }


    private fun encodeImageOne(): String {
        val baos = ByteArrayOutputStream()
        bitmap_one?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)

    }

    private fun encodeImageTwo(): String {
        val baos = ByteArrayOutputStream()
        bitmap_two?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)

    }

    private fun encodeImageThree(): String {
        val baos = ByteArrayOutputStream()
        bitmap_three?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_save) {

            val progressDialog = ProgressDialog(this@AddPostActivity)
            progressDialog.setTitle("Gönderiniz Paylaşılıyor")
            progressDialog.setMessage("Lütfen Bekleyin...")
            progressDialog.show()

            uploadPost()



            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun getCategoryForSpinner() {
        val cdi = ApiUtils.getCategoryDaoInterface()

        cdi.allCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>?
            ) {
                if (response != null) {
                    val category = response.body()!!.categories

                    for (c in category) {
                        categoryList.add(c.category_name.toUpperCase())
                        categoryAdapter = ArrayAdapter(
                            this@AddPostActivity,
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            categoryList
                        )
                        add_post_spinner.adapter = categoryAdapter

                        add_post_spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(p0: AdapterView<*>?) {

                                }

                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    //Toast.makeText(applicationContext,"Seçilen Kategori: ${categoryList[p2]}",Toast.LENGTH_SHORT).show()


                                }

                            }
                    }

                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {

            }
        })
    }


}

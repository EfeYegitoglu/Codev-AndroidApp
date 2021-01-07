package com.example.coderapp.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.coderapp.R
import com.example.coderapp.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.fragment_category.swipeToRefreshCategory as swipeToRefreshCategory


class CategoryFragment : Fragment(){

    private lateinit var adapter: CategoryAdapter
    private lateinit var categoryRv: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)



        categoryRv = view.findViewById(R.id.categoryRv)
        categoryRv.setHasFixedSize(true)
        categoryRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        allCategories()

        

        return view
    }

    private fun refreshPage(){

        swipeToRefreshCategory.setOnRefreshListener{
            activity?.finish()
            activity?.overridePendingTransition(0, 0)
            activity?.startActivity(activity!!.intent)
            activity?.overridePendingTransition(0, 0)
            swipeToRefresh.isRefreshing=false
        }
    }

    fun allCategories() {

        val cdi = ApiUtils.getCategoryDaoInterface()

        cdi.allCategories().enqueue(object : Callback<CategoryResponse> {

            override fun onResponse(call: Call<CategoryResponse>?, response: Response<CategoryResponse>?
            ) {
                if (response !=null) {
                    Log.e("category", response.body()?.categories.toString())

                    val categoryList = response.body()!!.categories

                    adapter = CategoryAdapter(view!!.context, categoryList)


                    categoryRv.adapter = adapter
                }

            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }





}

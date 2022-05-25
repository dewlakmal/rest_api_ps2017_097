package com.example.restapips_2017_097

import Adapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restapips_2017_097.API.Post
import com.example.restapips_2017_097.API.RetrofitAPI
import com.example.restapips_2017_097.databinding.FragmentFirstBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting the recyclerview by its id
        val recyclerview = binding.recyclerview

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this.context)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<Post>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
          data.add(Post(i,i, "Item " + i,""))
        }



        val apiInterface = RetrofitAPI.create().getPosts()

        //apiInterface.enqueue( Callback<List<Movie>>())
        apiInterface.enqueue( object : Callback<List<Post>> {
            @SuppressLint("CheckResult")
            override fun onResponse(call: Call<List<Post>>?, response: Response<List<Post>>?) {

                if(response?.body() != null){

                    // This will pass the ArrayList to our Adapter
                    val adapter = Adapter(response.body()!!)

                    // Setting the Adapter with the recyclerview
                    recyclerview.adapter = adapter

                    adapter.clickEvent.subscribe {
                        val clickedPostId = it.toInt()
                       // Toast.makeText(parentFragment?.context  ,"clicked $clickedPostId",Toast.LENGTH_SHORT).show()
                        response.body()!!.forEach(){
                            if (clickedPostId == it.id) {
                                setFragmentResult("requestKey",bundleOf("postid" to it.id,
                                                                                        "userid" to it.userId,
                                                                                            "title" to it.title,
                                                                                            "body" to it.body
                                                                                                            ))
                                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

                            }
                        }

                    }

                }
                 //   recyclerAdapter.setMovieListItems(response.body()!!)
            }

            override fun onFailure(call: Call<List<Post>>?, t: Throwable?) {
                Log.i("FirstFragment","Fail To Retrieve")

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
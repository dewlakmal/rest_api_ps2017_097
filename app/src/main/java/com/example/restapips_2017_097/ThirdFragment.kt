package com.example.restapips_2017_097

import Adapter2
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restapips_2017_097.API.Comment
import com.example.restapips_2017_097.API.Post
import com.example.restapips_2017_097.API.RetrofitAPI
import com.example.restapips_2017_097.databinding.FragmentThirdBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("requestKey1"){_,bundle->
            val selectedpostid = bundle.getInt("postid")
            //Toast.makeText(parentFragment?.context  ,"clicked $selectedpostid", Toast.LENGTH_SHORT).show()

            // getting the recyclerview by its id
            val recyclerview = binding.recyclerview1


            // this creates a vertical layout Manager
            recyclerview.layoutManager = LinearLayoutManager(this.context)

//            // ArrayList of class ItemsViewModel
//            val data = ArrayList<Post>()
//
//            // This loop will create 20 Views containing
//            // the image with the count of view
//            for (i in 1..20) {
//                data.add(Post(i,i, "Item " + i,""))
//            }



            val apiInterface = RetrofitAPI.create().getComments()

            //apiInterface.enqueue( Callback<List<Movie>>())
            apiInterface.enqueue( object : Callback<List<Comment>> {
                @SuppressLint("CheckResult")

                override fun onResponse(call: Call<List<Comment>>?, response: Response<List<Comment>>?) {
                    if (response?.body() != null) {
                        val data=ArrayList<Comment>()
                            response.body()!!.forEach(){
                                if(selectedpostid==it.postId){
                                    data.add(it)
                                }
                            }
                        // This will pass the ArrayList to our Adapter
                        val adapter = Adapter2(data)
                        // Setting the Adapter with the recyclerview
                        recyclerview.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                    Log.i("ThirdFragment","retrofit failed")
                }
            })



        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
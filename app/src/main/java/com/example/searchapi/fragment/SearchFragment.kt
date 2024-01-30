package com.example.searchapi.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.searchapi.R
import com.example.searchapi.adapter.ImageAdapter
import com.example.searchapi.databinding.FragmentSearchBinding
import com.example.searchapi.retrofit.Retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val imageAdapter by lazy { ImageAdapter(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvSearch.adapter = imageAdapter
            rvSearch.layoutManager = GridLayoutManager(context, 2)

            btnSearch.setOnClickListener {
                root.hideKeyboardInput()
                val query = etSearch.text.toString()
                searchImage(query)

            }

        }
    }

    private fun searchImage(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = Retrofit.api.searchImage(query, "accuracy", 1, 80)
            Log.d("aa", "response: $response")

            with(imageAdapter) {
                imageList.clear()
                imageList.addAll(response.documents)
                notifyDataSetChanged()
            }
        }
    }
    private fun View.hideKeyboardInput() {
        val keyBoard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyBoard.hideSoftInputFromWindow(windowToken, 0)
    }
}
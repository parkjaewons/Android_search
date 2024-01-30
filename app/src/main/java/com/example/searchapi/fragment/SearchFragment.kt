package com.example.searchapi.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchapi.adapter.ImageAdapter
import com.example.searchapi.data.Document
import com.example.searchapi.databinding.FragmentSearchBinding
import com.example.searchapi.retrofit.Retrofit
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val imageAdapter by lazy { ImageAdapter(mutableListOf()) }
    private var isLike = false

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
                val query = etSearch.text.toString()
                if(query.isEmpty()) Snackbar.make(view,"내용을 입력해주세요.",Snackbar.LENGTH_SHORT).show() else searchImage(query)
                root.hideKeyboardInput()
            }
        }

        // 플로팅 버튼
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isTop = true
        binding.rvSearch.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.rvSearch.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.fbContactFloating.startAnimation(fadeOut)
                    binding.fbContactFloating.visibility = View.GONE
                    isTop = true
                } else {
                    if (isTop) {
                        binding.fbContactFloating.visibility = View.VISIBLE
                        binding.fbContactFloating.startAnimation(fadeIn)
                        isTop = false
                    }
                }
            }
        })
        binding.fbContactFloating.setOnClickListener {
            binding.rvSearch.smoothScrollToPosition(0)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchImage(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = Retrofit.api.searchImage(query, "accuracy", 1, 80)
            Log.d("aa", "response: $response")

            with(imageAdapter) {
                imageList.clear()
                val addAll = imageList.addAll(response.documents)
                notifyDataSetChanged()
            }
        }
    }

    //키보드 내리기
    private fun View.hideKeyboardInput() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}
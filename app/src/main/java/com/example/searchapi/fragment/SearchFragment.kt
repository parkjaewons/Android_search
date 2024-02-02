package com.example.searchapi.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
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

class SearchFragment(private val likeImage: MutableList<Document>) : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val imageAdapter by lazy { ImageAdapter(mutableListOf(), likeImage) }
    private var page = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        // 플로팅 버튼
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isTop = true
        with(binding) {
            rvSearch.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!rvSearch.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        fbContactFloating.startAnimation(fadeOut)
                        fbContactFloating.visibility = View.GONE
                        isTop = true
                    } else {
                        if (isTop) {
                            fbContactFloating.visibility = View.VISIBLE
                            fbContactFloating.startAnimation(fadeIn)
                            isTop = false
                        }
                    }
                }
            })

            fbContactFloating.setOnClickListener {
                rvSearch.smoothScrollToPosition(0)
            }

            rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @SuppressLint("NotifyDataSetChanged")
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    // 마지막 스크롤된 항목 위치
                    val query = etSearch.text.toString()
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as GridLayoutManagerWrapper?)!!.findLastCompletelyVisibleItemPosition()
                    CoroutineScope(Dispatchers.Main).launch {
                        // 항목 전체 개수
                        val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                        if (lastVisibleItemPosition == itemTotalCount) {
                            page += 1 //스크롤이 최하단이면 다음페이지 출력
                            Log.d("SCROLL", "last Position..."); //스크롤 하단인지 확인
                            Log.d("SCROLL", "page: $page"); //페이지 잘 넘어가는지 확인
                            val response = Retrofit.api.searchImage(query, "accuracy", page, 80)
                            with(imageAdapter) {
                                imageList.addAll(response.documents)
                                notifyDataSetChanged()
                            }
                        }
                    }
                }
            })
        }
        loadQuery()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchImage(query: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = Retrofit.api.searchImage(query, "accuracy", 1, 80)
            with(imageAdapter) {
                imageList.clear()
                val addAll = imageList.addAll(response.documents)
                notifyDataSetChanged()
            }
        }
    }

    private fun initView(view: View) {
        with(binding) {
            rvSearch.adapter = imageAdapter
            rvSearch.layoutManager = GridLayoutManagerWrapper(context, 2)

            btnSearch.setOnClickListener {
                saveQuery()
                val query = etSearch.text.toString()
                if (query.isEmpty()) Snackbar.make(view, "내용을 입력해주세요.", Snackbar.LENGTH_SHORT)
                    .show() else searchImage(query)
                root.hideKeyboardInput()
            }
        }
    }

    //키보드 내리기
    private fun View.hideKeyboardInput() {
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    //검색어 저장
    private fun saveQuery() {
        val pref = requireContext().getSharedPreferences("pref", 0)
        val edit = pref.edit()
        edit.putString("title", binding.etSearch.text.toString())
        edit.apply()
    }

    //저장된 검색어 출력
    private fun loadQuery() {
        val pref = requireContext().getSharedPreferences("pref", 0)
        binding.etSearch.setText(pref.getString("title", ""))
    }

    class GridLayoutManagerWrapper(context: Context?, spanCount: Int) :
        GridLayoutManager(context, spanCount) {
        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }
}
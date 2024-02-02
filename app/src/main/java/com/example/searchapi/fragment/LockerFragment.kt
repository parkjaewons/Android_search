package com.example.searchapi.fragment

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchapi.adapter.ImageAdapter
import com.example.searchapi.data.Document
import com.example.searchapi.databinding.FragmentLockerBinding

class LockerFragment(private val likeImage: MutableList<Document>) : Fragment() {
    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!
    private val imageAdapter by lazy { ImageAdapter(likeImage, likeImage) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        // 플로팅 버튼
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isTop = true
        binding.rvLocker.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.rvLocker.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
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
            binding.rvLocker.smoothScrollToPosition(0)
        }
    }

    private fun initView() {
        with(binding) {
            rvLocker.adapter = imageAdapter
            rvLocker.layoutManager = GridLayoutManagerWrapper(context, 2)
        }
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    class GridLayoutManagerWrapper(context: Context?, spanCount: Int) :
        GridLayoutManager(context, spanCount) {
        override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }
}
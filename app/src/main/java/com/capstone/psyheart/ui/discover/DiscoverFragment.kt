package com.capstone.psyheart.ui.discover

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.psyheart.R
import com.capstone.psyheart.adapter.ListSongCategoryAdapter
import com.capstone.psyheart.databinding.FragmentDiscoverBinding
import com.capstone.psyheart.model.CategoryItem
import com.capstone.psyheart.ui.ViewModelFactory
import com.capstone.psyheart.ui.discover_detail.DiscoverDetailActivity
import com.capstone.psyheart.utils.ResultData

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private lateinit var listSongCategoryAdapter: ListSongCategoryAdapter
    private lateinit var factory: ViewModelFactory
    private val discoverViewModel: DiscoverViewModel by viewModels { factory }
    private lateinit var root: View
    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        factory = ViewModelFactory.getInstance(requireContext())
        root = binding.root

        discoverViewModel.getSongCategories()
        handleSongCategory()
        return root
    }

    private fun handleSongCategory() {
        discoverViewModel.resultSongCategories.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultData.Loading -> {
                        loadingHandler(true)
                    }

                    is ResultData.Failure -> {
                        loadingHandler(false)
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResultData.Success -> {
                        loadingHandler(false)
                        setupView(requireContext(), result.data.data.categories)
                    }
                }
            }
        }
    }

    private fun setupView(context: Context, categories: List<CategoryItem>) {
        val discoverRv = binding.discoverRecyclerView

        discoverRv.layoutManager = GridLayoutManager(context, 2)

        val paddingBottom = 80 // Adjust the value as needed
        discoverRv.setPadding(
            discoverRv.paddingLeft,
            discoverRv.paddingTop,
            discoverRv.paddingRight,
            paddingBottom
        )
        discoverRv.clipToPadding = false

        listSongCategoryAdapter = ListSongCategoryAdapter(categories)
        listSongCategoryAdapter.setOnItemClickCallback(object :
            ListSongCategoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CategoryItem) {
                val intent = Intent(context, DiscoverDetailActivity::class.java).apply {
                    putExtra(DISCOVER_FRAGMENT, data.id)
                }
                startActivity(intent)
            }
        })
        discoverRv.adapter = listSongCategoryAdapter
    }

    private fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            showLoadingPopup()
        } else {
            hideLoadingPopup()
        }
    }

    @SuppressLint("InflateParams")
    private fun showLoadingPopup() {
        if (loadingDialog == null) {
            loadingDialog =
                Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar).apply {
                    val view: View =
                        LayoutInflater.from(context).inflate(R.layout.loading_layout, null)
                    setContentView(view)
                    setCancelable(false)
                }
        }
        loadingDialog?.show()
    }

    private fun hideLoadingPopup() {
        loadingDialog?.takeIf { it.isShowing }?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DISCOVER_FRAGMENT = "music_player"
    }
}

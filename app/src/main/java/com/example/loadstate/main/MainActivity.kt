package com.example.loadstate.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.loadstate.R
import com.example.loadstate.data.State
import com.example.loadstate.databinding.ActivityMainBinding
import com.example.loadstate.details.DetailsActivity
import com.example.loadstate.ext.loadServiceInit
import com.example.loadstate.ext.showEmpty
import com.example.loadstate.ext.showFailed
import com.example.loadstate.ext.startActivity
import com.holo.loadstate.LoadService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var loadService: LoadService
    private lateinit var adapter: RvLoadAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        adapter = RvLoadAdapter { bean ->
            startActivity<DetailsActivity>("bean" to bean)
        }
        binding.recyclerView.adapter = adapter

        loadService = loadServiceInit(binding.recyclerView, adapter, R.layout.item_rv_load_shimmer) {
            viewModel.getDataList()
        }
        binding.srl.setOnRefreshListener { viewModel.getDataList() }

        viewModel.getDataList()
        observer()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_shimmer -> {
                loadService.hide()
                loadService = loadServiceInit(binding.recyclerView, adapter, R.layout.item_rv_load_shimmer) {
                    viewModel.getDataList()
                }
                viewModel.getDataList()
            }
            R.id.menu_lottie -> {
                loadService.hide()
                loadService = loadServiceInit(binding.recyclerView, R.layout.layout_loadsir_loading_lottie, false) {
                    viewModel.getDataList()
                }
                viewModel.getDataList()
            }
            R.id.menu_custom -> {
                loadService.hide()
                loadService = loadServiceInit(binding.recyclerView, R.layout.layout_loadsir_loading) {
                    viewModel.getDataList()
                }
                viewModel.getDataList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val emptyIcons = arrayOf(R.mipmap.ic_shogun, R.mipmap.ic_zhongli, R.mipmap.ic_wendi)
    private val emptyTips = arrayOf("这里神马都木有", "暂无数据哦", "还没有任何未读信息~")
    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataListResp.collect { state ->
                    binding.srl.isRefreshing = false
                    when (state) {
                        is State.Success -> {
                            if (state.data.isNullOrEmpty()) {
                                val random = (0..2).random()
                                loadService.showEmpty(emptyTips[random], emptyIcons[random])
                            } else {
                                loadService.hide()
                                adapter.dataList = state.data
                            }
                        }
                        is State.Error -> {
                            loadService.showFailed(state.err.message)
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
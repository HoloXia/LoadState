package com.example.loadstate.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loadstate.R
import com.example.loadstate.appContext
import com.example.loadstate.data.RvLoadBean
import com.example.loadstate.databinding.ActivityDetailsBinding
import com.example.loadstate.ext.loadServiceInit
import com.holo.loadstate.LoadService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var loadBean: RvLoadBean

    private lateinit var loadService: LoadService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadBean = intent.getParcelableExtra("bean") ?: RvLoadBean(
            R.mipmap.ic_wendi,
            appContext.getString(R.string.str_wd_intro),
            appContext.getString(R.string.str_wd_from),
            appContext.getString(R.string.str_wd_details)
        )

        loadService = loadServiceInit(binding.content, R.layout.activity_details_shimmer) {
            testLoad()
        }

        loadBean.let { bean ->
            binding.ivAvatar.setImageResource(bean.faceUrl)
            binding.tvName.text = bean.name
            binding.tvIntro.text = bean.introduction
            binding.tvFrom.text = bean.from
            binding.tvDetails.text = bean.details
        }

        testLoad()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_shimmer -> {
                loadService.hide()
                loadService = loadServiceInit(binding.content, R.layout.activity_details_shimmer) {
                    testLoad()
                }
                testLoad()
            }
            R.id.menu_lottie -> {
                loadService.hide()
                loadService = loadServiceInit(binding.content, R.layout.layout_loadsir_loading_lottie, false) {
                    testLoad()
                }
                testLoad()
            }
            R.id.menu_custom -> {
                loadService.hide()
                loadService = loadServiceInit(binding.content, R.layout.layout_loadsir_loading) {
                    testLoad()
                }
                testLoad()
            }
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //1.Success 2.Empty 3.Failed
    private var reqRes: Int = 1
    private var job: Job? = null

    private fun testLoad() {
        job?.cancel()
        job = lifecycleScope.launch {
            delay(1300)
            when (reqRes) {
                1 -> { //Success
                    loadService.hide()
                }
                2 -> { //Empty
                    loadService.showEmpty()
                }
                else -> { //Failed
                    loadService.showFailed()
                }
            }
            if (reqRes >= 3) reqRes = 1 else reqRes++
        }
    }
}

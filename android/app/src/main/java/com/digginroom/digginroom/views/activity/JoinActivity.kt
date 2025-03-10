package com.digginroom.digginroom.views.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.digginroom.digginroom.R
import com.digginroom.digginroom.data.di.ViewModelFactory
import com.digginroom.digginroom.databinding.ActivityJoinBinding
import com.digginroom.digginroom.viewmodels.JoinViewModel

class JoinActivity : AppCompatActivity(), ResultListener {

    private lateinit var binding: ActivityJoinBinding
    private val joinViewModel: JoinViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(applicationContext).joinViewModelFactory
        )[JoinViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initJoinBinding()
    }

    private fun initJoinBinding() {
        binding =
            DataBindingUtil.setContentView<ActivityJoinBinding>(this, R.layout.activity_join).also {
                it.lifecycleOwner = this
                it.resultListener = this
                it.viewModel = joinViewModel
            }
    }

    override fun onSucceed() {
        LoginActivity.start(this)
        finish()
    }

    override fun onFailed() {
        with(binding) {
            joinEtInputId.text.clear()
            joinEtInputPassword.text.clear()
        }
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, JoinActivity::class.java)

            context.startActivity(intent)
        }
    }
}

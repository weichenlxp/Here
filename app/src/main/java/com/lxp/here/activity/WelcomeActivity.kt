package com.lxp.here.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.lxp.here.R

/* 功能描述
 * @author lixianpeng
 * @since 2021-12-04
 */

class WelcomeActivity : AppCompatActivity() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        
        /**
         * 延迟3秒进入主界面
         */
        Handler().postDelayed(java.lang.Runnable {
            val intent: android.content.Intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
            startActivity(intent)
            this@WelcomeActivity.finish()
        }, 1500)
    }
}
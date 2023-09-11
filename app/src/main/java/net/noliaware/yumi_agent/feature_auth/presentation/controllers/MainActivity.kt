package net.noliaware.yumi_agent.feature_auth.presentation.controllers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.Args.ACCOUNT_DATA
import net.noliaware.yumi_agent.commun.Push.ACTION_PUSH_DATA
import net.noliaware.yumi_agent.commun.Push.PUSH_BODY
import net.noliaware.yumi_agent.commun.Push.PUSH_TITLE
import net.noliaware.yumi_agent.commun.util.getSerializableExtraCompat
import net.noliaware.yumi_agent.feature_login.domain.model.AccountData

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent.getSerializableExtraCompat<AccountData>(ACCOUNT_DATA)?.let { accountData ->
            supportFragmentManager.beginTransaction().run {
                replace(R.id.main_fragment_container, HomeFragment.newInstance(accountData))
                commit()
            }
        }
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            intent.extras?.let {
                val title = it.getString(PUSH_TITLE)
                val body = it.getString(PUSH_BODY)
                context?.let {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle(title)
                        .setMessage(body)
                        .setPositiveButton(R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            messageReceiver,
            IntentFilter(ACTION_PUSH_DATA)
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }
}
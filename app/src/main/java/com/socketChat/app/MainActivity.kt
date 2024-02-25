package com.socketChat.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.socketChat.app.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.WebSocket

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val webSocketListener by lazy { WebSocketListener(viewModel) }
    private lateinit var webSocket: WebSocket
    private val adapter by lazy { Adapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initializeWebSocket()
        setupClickListener()
        observeData()
        binding.recyclerView.adapter = adapter
    }

    private fun observeData() {

        viewModel.status.observe(this) {
            Toast.makeText(
                this,
                when (it) {
                    Status.CONNECTED -> "Channel Connected"
                    Status.DISCONNECTED -> "Channel Disconnected"
                    else -> "Channel Disconnected"
                }, Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.messageList.observe(this) {
            adapter.submitList(it)
            val itemCount = adapter.itemCount
            binding.recyclerView.scrollToPosition(itemCount - 1)
        }


    }

    private fun setupClickListener() {

        binding.imgSend.setOnClickListener {
            val message: String = binding.edtText.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.addMessage(ChatModel(message, true))
                lifecycleScope.launch(Dispatchers.IO) {
                    webSocket.send(message)
                }
            }
            binding.edtText.text.clear()
        }

    }

    private inline fun initializeWebSocket() {
        webSocket = OkHttpClient().newWebSocket(viewModel.createConnection(), webSocketListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket.close(1001, "No Need anymore")
    }

}
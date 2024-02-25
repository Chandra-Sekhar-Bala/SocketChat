package com.socketChat.app

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener(
    private val viewModel: MainViewModel
) : WebSocketListener() {
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.setStatus(Status.DISCONNECTED)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        viewModel.setStatus(Status.DISCONNECTED)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (viewModel.status.value == Status.CONNECTED) {
            viewModel.addMessage(ChatModel(text, false))
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setStatus(Status.CONNECTED)
        webSocket.send("kotlin Built System Connected 🫡")
    }
}
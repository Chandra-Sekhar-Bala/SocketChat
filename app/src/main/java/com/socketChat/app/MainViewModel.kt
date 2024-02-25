package com.socketChat.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request


class MainViewModel : ViewModel() {

    // chat messages
    private val _messageList = MutableLiveData<List<ChatModel>>()
    val messageList: LiveData<List<ChatModel>>
        get() = _messageList

    // Socket status
    private val _status = MutableLiveData<Status>()
    public val status: LiveData<Status>
        get() = _status


    fun addMessage(message: ChatModel) {
        viewModelScope.launch(Dispatchers.Default) {
            val currentList = _messageList.value.orEmpty().toMutableList()
            currentList.add(ChatModel(message.msg, message.fromMe))
            _messageList.postValue(currentList)
        }
    }

    fun setStatus(status: Status) {
        viewModelScope.launch(Dispatchers.Main) {
            _status.value = status
        }
    }

    fun createConnection(): Request {
        val url =
            "wss://free.blr2.piesocket.com/v3/1?api_key=jTpmtKVCB7JjDNo86vZpRUN2aJ3d5Xykwo9igr9H&notify_self=1"
        return Request.Builder()
            .url(url)
            .build()
    }

}
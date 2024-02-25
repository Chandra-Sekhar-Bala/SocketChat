package com.socketChat.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socketChat.app.databinding.ItemChatBinding

class Adapter : ListAdapter<ChatModel, Adapter.ViewHolder>(Difference) {
    inner class ViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChatModel) {
            when (item.fromMe) {
                true -> {
                    binding.txtMe.visibility = View.VISIBLE
                    binding.txtMe.text = item.msg
                    binding.txtOther.visibility = View.GONE
                }
                false -> {
                    binding.txtOther.visibility = View.VISIBLE
                    binding.txtOther.text = item.msg
                    binding.txtMe.visibility = View.GONE
                }
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    object Difference : DiffUtil.ItemCallback<ChatModel>() {
        override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
            return oldItem == newItem
        }

    }
}
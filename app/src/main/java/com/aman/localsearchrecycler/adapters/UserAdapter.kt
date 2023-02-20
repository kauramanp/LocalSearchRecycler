package com.aman.localsearchrecycler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aman.localsearchrecycler.databinding.LayoutUserModelBinding
import com.aman.localsearchrecycler.models.UserModel
import com.aman.localsearchrecycler.utils.UserModelClick

class UserAdapter(var arrayList: ArrayList<UserModel>, var clickInterface: UserModelClick): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: LayoutUserModelBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int,userModel: UserModel, userModelClick: UserModelClick){
            binding.position = position
            binding.userModel = userModel
            binding.userModelClick = userModelClick
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutUserModelBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var userModel = arrayList[position]
        userModel.id = position
        holder.bind(position, userModel, clickInterface)
    }

    override fun getItemCount() = arrayList.size
}
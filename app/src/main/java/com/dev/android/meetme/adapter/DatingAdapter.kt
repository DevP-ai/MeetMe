package com.dev.android.meetme.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.android.meetme.databinding.StackLayoutBinding
import com.dev.android.meetme.model.UserDataModel

class DatingAdapter(val context: Context,val list:ArrayList<UserDataModel>): RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {

    inner class DatingViewHolder(val binding:StackLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {



        return DatingViewHolder(StackLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {
        holder.binding.StackName.text=list[position].name
        holder.binding.StackEmail.text=list[position].email


        Glide.with(context).load(list[position].image).into(holder.binding.StackImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
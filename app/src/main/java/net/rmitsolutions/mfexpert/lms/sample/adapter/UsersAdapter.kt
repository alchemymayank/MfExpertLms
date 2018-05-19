package net.rmitsolutions.mfexpert.lms.sample.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.databinding.ActivitySampleBinding
import net.rmitsolutions.mfexpert.lms.databinding.SampleItemBinding
import net.rmitsolutions.mfexpert.lms.sample.Users

class UsersAdapter: RecyclerView.Adapter<UserViewHolder>() {
    lateinit var  context : Context
    var items: List<Users> = emptyList()
    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        context = parent.context
        if (inflater == null){
            inflater = LayoutInflater.from(context)
        }
        val binding = SampleItemBinding.inflate(inflater!!, parent, false)
//        val v = LayoutInflater.from(context).inflate(R.layout.sample_item, parent, false)
        return UserViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindView(items[position])
    }
}
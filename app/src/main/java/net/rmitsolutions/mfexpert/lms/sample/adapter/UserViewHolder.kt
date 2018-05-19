package net.rmitsolutions.mfexpert.lms.sample.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import net.rmitsolutions.mfexpert.lms.databinding.ActivitySampleBinding
import net.rmitsolutions.mfexpert.lms.databinding.SampleItemBinding
import net.rmitsolutions.mfexpert.lms.sample.Users

class UserViewHolder(dataBinding: SampleItemBinding) : RecyclerView.ViewHolder(dataBinding.root) {

    var dataBinding = dataBinding

    fun bindView(users: Users){
        dataBinding.users = users
//        dataBinding.users?.lastName = users.lastName
//        dataBinding.users?.mobileNumber = users.mobileNumber
    }
}
package net.rmitsolutions.mfexpert.lms.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import net.rmitsolutions.mfexpert.lms.BaseActivity
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.sample.adapter.UsersAdapter

class SampleData : BaseActivity() {

    private lateinit var recyclerView: RecyclerView

    val adapter: UsersAdapter by lazy { UsersAdapter() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_data)

        recyclerView = findViewById(R.id.sample_data_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter

        getRoomData()
    }

    private fun getRoomData() {
        val list = MfExpertApp.database.userDao().getAllUsers()
        setRecyclerViewAdapter(list)
    }

    private fun setRecyclerViewAdapter(list: List<Users>) {
//        showLogDebug("Setting Recycler View Adapter")
        adapter.items = list
    }

    override fun getSelfNavDrawerItem() = R.id.nav_dashboard
}

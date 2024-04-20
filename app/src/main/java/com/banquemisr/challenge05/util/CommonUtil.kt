package com.banquemisr.androidtask.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.banquemisr.androidtask.data.model.GridModel


class CommonUtil(val context: Context) {
    fun setRecycleView(recyclerView: RecyclerView?, adaptor: RecyclerView.Adapter<*>,
                       verticalOrHorizontal: Int?, context: Context, gridModel: GridModel?, includeEdge : Boolean) {
        var layoutManger : RecyclerView.LayoutManager? = null
        if (gridModel==null) // normal linear
            layoutManger = LinearLayoutManager(context, verticalOrHorizontal!!,false)
        else
        {
            layoutManger = GridLayoutManager(context, gridModel.numberOfItems)
            if (recyclerView?.itemDecorationCount==0)
                recyclerView.addItemDecoration(SpacesItemDecoration(gridModel.numberOfItems, gridModel.space, includeEdge))
        }
        recyclerView?.apply {
            setLayoutManager(layoutManger)
            setHasFixedSize(true)
            adapter = adaptor

        }
    }
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
            val capabilities = connectivityManager.getNetworkCapabilities(networkCapabilities)
            capabilities?.let {
                result = it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&  capabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        //     if (result)
        //        return internetIsConnected()

        return result
    }
    fun changeFragmentBack(activity: FragmentActivity, fragment: Fragment, tag: String, bundle: Bundle?, id : Int ) {

        val transaction = activity.supportFragmentManager.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        transaction.replace(id, fragment, tag)
        transaction.addToBackStack(tag)
        //    transaction.addToBackStack(null)
        transaction.commit()

    }


    fun showSnackMessages(
        activity: Activity?,
        error: String?, color : Int= android.R.color.holo_red_dark
    ) {
        activity?.let {
            Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                //.title(activity.getString(R.string.errors))
                .message(error!!)
                .backgroundColorRes(color)
                .dismissOnTapOutside()
                .duration(2500)
                .enableSwipeToDismiss()
                .enterAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(550)
                        .alpha()
                        .overshoot()
                )
                .exitAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(200)
                        .anticipateOvershoot()
                )
                .build().show()
        }
    }
}
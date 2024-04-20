package com.banquemisr.swensonhe.di

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.banquemisr.androidtask.data.model.Constants.SHAREDPREFS
import com.banquemisr.androidtask.util.CommonUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier
@Module
@InstallIn(ViewModelComponent::class,
    FragmentComponent::class,ActivityComponent::class,ServiceComponent::class)
class CommonDi {
    @Provides
    fun getUtil(@ApplicationContext context: Context?): CommonUtil {
        return  CommonUtil(context!!)
    }
    @Provides
    fun getSharedPrefs(@ApplicationContext context: Context?): SharedPreferences {
        return context!!.getSharedPreferences(
            SHAREDPREFS,
            Context.MODE_PRIVATE
        )
    }
}
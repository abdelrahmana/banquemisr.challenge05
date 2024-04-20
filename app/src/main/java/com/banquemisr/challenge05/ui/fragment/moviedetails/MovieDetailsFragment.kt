package com.banquemisr.challenge05.ui.fragment.moviedetails

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.banquemisr.androidtask.data.model.Constants.MOVIE_ID
import com.banquemisr.androidtask.util.CommonUtil
import com.banquemisr.challenge05.R
import com.banquemisr.challenge05.data.model.ProductionCompany
import com.banquemisr.challenge05.data.model.Result
import com.banquemisr.challenge05.data.model.SourceModel
import com.banquemisr.challenge05.databinding.MovieDetailsFragmentBinding
import com.banquemisr.challenge05.ui.fragment.moviedetails.adaptor.ProductionCompanyAdaptor
import com.banquemisr.challenge05.ui.fragment.moviefragment.MovieViewModel
import com.banquemisr.challenge05.ui.fragment.moviefragment.Offline
import com.banquemisr.challenge05.ui.fragment.moviefragment.OnlineConnection
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(){
    @Inject lateinit var utils : CommonUtil
    @Inject lateinit var sharedPrefs : SharedPreferences
    val model : MovieViewModel by viewModels()
    var binding : MovieDetailsFragmentBinding ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = MovieDetailsFragmentBinding.inflate(layoutInflater, container, false)
        binding?.containerHeader?.backArrow?.setOnClickListener{
            findNavController().popBackStack()

        }
        model.getMovieDetails(if(utils.isInternetAvailable(requireContext()))OnlineConnection()

        else
             Offline(),arguments?.getInt(MOVIE_ID)?:0
        )
        setViewModelObservers()
        return binding!!.root
    }

    private fun setViewModelObservers() {
        model.movieLiveData.observe(viewLifecycleOwner, Observer{ // for sending to maintenance
            if (it!=null){
                setViews(it,requireContext(),SourceModel.baseUrlImage)

            }
        })

        model.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                binding?.progress?.visibility = it
                model.setNetworkLoader(null)
            }
        })

        model.errorViewModel.observe(viewLifecycleOwner) {
            it?.let { error ->
                utils.showSnackMessages(requireActivity(), error)

            }
        }
    }
    var adaptorRecycleCompanies : ProductionCompanyAdaptor? = null
    private fun setViews(it: Result,context : Context,baseUrl : String) {
        binding?.movieName?.text = it.title?:""
        binding?.productionYear?.text = it.release_date?:""
        binding?.ratingText?.text = it.vote_average.toString()
        binding?.overViewText?.text = it.overview.toString()
        binding?.containerHeader?.headerTitle?.text = it.title?:""
        Glide.with(context).load( baseUrl+it.poster_path)
            .error(R.color.gray_new).placeholder(R.color.gray_new).dontAnimate().into(binding!!.posterImage)
        it.production_companies?.let { casts->
            adaptorRecycleCompanies = ProductionCompanyAdaptor(context,
                casts as ArrayList<ProductionCompany>
            )
            utils.setRecycleView(binding!!.castRecycle,adaptorRecycleCompanies!!,LinearLayoutManager.VERTICAL,context,null,false
            )
        }
    }

}
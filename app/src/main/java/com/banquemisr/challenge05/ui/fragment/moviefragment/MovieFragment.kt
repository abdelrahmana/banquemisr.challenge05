package com.banquemisr.challenge05.ui.fragment.moviefragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.banquemisr.androidtask.data.model.Constants.MOVIE_ID
import com.banquemisr.androidtask.data.model.GridModel
import com.banquemisr.androidtask.util.CommonUtil
import com.banquemisr.challenge05.R
import com.banquemisr.challenge05.data.model.Genre
import com.banquemisr.challenge05.data.model.Result
import com.banquemisr.challenge05.databinding.MovieFragmentBinding
import com.banquemisr.challenge05.ui.fragment.moviefragment.adaptor.AdaptorMovie
import com.banquemisr.challenge05.ui.fragment.moviefragment.adaptor.MovieCategoryAdaptor
import com.banquemisr.challenge05.util.NestedScrollPaginationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment(), NestedScrollPaginationView.OnMyScrollChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
  //  lateinit var interfaceImages: InterfaceImages // this interface will change depend on each selection
    @Inject
    lateinit var util: CommonUtil
    @Inject lateinit var prefsUtil: SharedPreferences
    val viewModel : MovieViewModel by viewModels()
    var arrayListMovies = ArrayList<Result>()
    var arrayListCategory = ArrayList<Genre>()
    var selectedCategoryFilterId = 0 // all

    var binding : MovieFragmentBinding? =null
    var adaptorCategory : MovieCategoryAdaptor?= null
    var adaptorMovie : AdaptorMovie?= null
    var internetStatus : InternetStatus? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = MovieFragmentBinding.inflate(layoutInflater,container,false)
        binding?.paginationScrolling?.myScrollChangeListener = this
        if (util.isInternetAvailable(requireContext()))internetStatus = OnlineConnection()
        else
            internetStatus = Offline()
        initValues()
        setViewModelObservers() // lisenter
        //internetStatus = if (util.isInternetAvailable(requireContext())) OnlineConnection() else Offline()
        binding?.swipe?.setOnRefreshListener{
            resetCall()
            binding?.paginationScrolling?.resetPageCounter()
            if (util.isInternetAvailable(requireContext()))internetStatus = OnlineConnection()
            else
                internetStatus = Offline()
            if (selectedCategoryFilterId == 0)
            getMovieList()
            else viewModel.getFilteredMovies(internetStatus!!,selectedCategoryFilterId,page)


        }
        binding?.searchEditText?.setOnClickListener{
            findNavController().navigate(R.id.nav_movie_search)
        }
        getMovieList()
        viewModel.getCategory(internetStatus!!)
        //callGetBox.invoke() // get default cart box when loading here
        return binding!!.root
    }

    private fun initValues() {
        arrayListMovies.clear()
        arrayListCategory.clear()
        resetCall()
        adaptorCategory = MovieCategoryAdaptor(requireContext(),arrayListCategory,callBackFilter,selectedCategoryFilterId)
        adaptorMovie = AdaptorMovie(requireContext(),arrayListMovies,::onClickMovie)
        util.setRecycleView(binding!!.movieGridRecycle,adaptorMovie!!,null,requireContext(),GridModel(2,30),false)
        util.setRecycleView(binding!!.recycleTopFilter,adaptorCategory!!,
            LinearLayoutManager.HORIZONTAL,requireContext(),null,false)

    }

    fun onClickMovie(movie: Result) { // when click on movie
        findNavController().navigate(R.id.nav_movie_details, bundleOf(MOVIE_ID to movie.id))
    }
    val callBackFilter :(Genre)->Unit= {
        if (util.isInternetAvailable(requireContext()))internetStatus = OnlineConnection()
        else
            internetStatus = Offline()
        resetCall()
        selectedCategoryFilterId = it.id
        if (selectedCategoryFilterId == 0)
        getMovieList() // this should call the selected  genre if it is all
        else
            viewModel.getFilteredMovies(internetStatus!!, it.id, page)

    }
    fun setViewModelObservers() {
        viewModel.movieListLiveData.observe(viewLifecycleOwner, Observer{ // for sending to maintenance
            binding?.swipe?.isRefreshing = false
            it?.let {
               // arrayListMovies.addAll(it.results)
                    hasMore = it.page < it.total_pages
                adaptorMovie?.updateList(it.results)
                checkIfArrayListIsEmpty(arrayListMovies)
            }
        })
        viewModel.movieListLiveDataOffline.observe(viewLifecycleOwner, Observer{ // for sending to maintenance
            binding?.swipe?.isRefreshing = false
            it?.let {
                // arrayListMovies.addAll(it.results)
                hasMore = arrayListMovies.size < it.size
                var subList : List<Result> ?=null
               if (it.size-arrayListMovies.size>0)
                 subList = it.subList(arrayListMovies.size,
                     if (it.size-arrayListMovies.size>=20)20+arrayListMovies.size else (it.size-arrayListMovies.size)+arrayListMovies.size)
                     adaptorMovie?.updateList(subList?:ArrayList())
                checkIfArrayListIsEmpty(arrayListMovies)
            }
        })

        viewModel.categoryTypesGenrLiveData.observe(viewLifecycleOwner, Observer{ // for sending to maintenance
            it?.let {
                arrayListCategory.add(Genre(name = getString(R.string.all)))
                arrayListCategory.addAll(it)
                adaptorCategory?.notifyDataSetChanged()
            }
        })


        viewModel.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                binding?.progress?.visibility = it
                viewModel.setNetworkLoader(null)
            }
        })

        viewModel.errorViewModel.observe(viewLifecycleOwner) {
            it?.let { error ->
                binding?.swipe?.isRefreshing = false
                util.showSnackMessages(requireActivity(), error)

            }
        }
    }

    private fun checkIfArrayListIsEmpty(arrayListResults: ArrayList<Result>) {
        if(arrayListResults.size==0)
            binding?.noResult?.visibility = View.VISIBLE
        else
            binding?.noResult?.visibility = View.GONE

    }

    fun getMovieList(){
        binding?.progress?.show()
        if (util.isInternetAvailable(requireContext()))internetStatus = OnlineConnection()
        else
            internetStatus = Offline()
        viewModel.getMovieList(internetStatus!!,HashMap<String, Any>().also {
            it.put(PAGE,page)
        })
    }
    //var hidePass = false
    fun resetCall () {
        page = 1
        binding?.paginationScrolling?.resetPageCounter()
        arrayListMovies.clear()
        hasMore = true
        // get the call from the beginning
    }

    override fun onDestroyView() {
        viewModel.clearListener(this)
        super.onDestroyView()
    }

    companion object {
        const val PAGE="page"
    }

    var hasMore = false
    var page = 1 // first page
    override fun onLoadMore(currentPage: Int) {
        if (getHasMorePages(hasMore)) {
            page = currentPage
            if (util.isInternetAvailable(requireContext()))internetStatus = OnlineConnection()
            else
                internetStatus = Offline()
            if (selectedCategoryFilterId ==0) // all
            getMovieList()
            else
             viewModel.getFilteredMovies(internetStatus!!, selectedCategoryFilterId, page)
        }
            // do second call
    }

    private fun getHasMorePages(hasMore: Boolean): Boolean {
        return hasMore
    }
}
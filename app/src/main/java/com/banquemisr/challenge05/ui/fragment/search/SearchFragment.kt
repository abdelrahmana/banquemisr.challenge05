package com.banquemisr.challenge05.ui.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.banquemisr.androidtask.data.model.Constants.MOVIE_ID
import com.banquemisr.androidtask.data.model.Constants.QUERY
import com.banquemisr.androidtask.data.model.GridModel
import com.banquemisr.androidtask.util.CommonUtil
import com.banquemisr.challenge05.R
import com.banquemisr.challenge05.data.model.Result
import com.banquemisr.challenge05.databinding.SearchLayoutBinding
import com.banquemisr.challenge05.ui.fragment.moviefragment.InternetStatus
import com.banquemisr.challenge05.ui.fragment.moviefragment.MovieViewModel
import com.banquemisr.challenge05.ui.fragment.moviefragment.Offline
import com.banquemisr.challenge05.ui.fragment.moviefragment.OnlineConnection
import com.banquemisr.challenge05.ui.fragment.moviefragment.adaptor.AdaptorMovie
import com.banquemisr.challenge05.ui.fragment.moviefragment.adaptor.MovieCategoryAdaptor
import com.banquemisr.challenge05.util.NestedScrollPaginationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), NestedScrollPaginationView.OnMyScrollChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
  //  lateinit var interfaceImages: InterfaceImages // this interface will change depend on each selection
    @Inject
    lateinit var util: CommonUtil
    val viewModel : MovieViewModel by viewModels()
    var arrayListMovies = ArrayList<Result>()

    var binding : SearchLayoutBinding? =null
    var adaptorMovie : AdaptorMovie?= null
    var internetStatus : InternetStatus? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SearchLayoutBinding.inflate(layoutInflater,container,false)
        binding?.paginationScrolling?.myScrollChangeListener = this
        if (util.isInternetAvailable(requireContext()))internetStatus = OnlineConnection()
        else
            internetStatus = Offline()
        initValues()
        setViewModelObservers() // lisenter
        //internetStatus = if (util.isInternetAvailable(requireContext())) OnlineConnection() else Offline()
        binding?.search?.setOnClickListener{
            if (binding?.searchEditText?.text?.isNotEmpty() == true) {
                resetCall()
                binding?.paginationScrolling?.resetPageCounter()
                if (util.isInternetAvailable(requireContext())) internetStatus = OnlineConnection()
                else
                    internetStatus = Offline()
                    getMovieList(binding?.searchEditText?.text.toString())
            }

        }
        binding?.searchEditText?.post { binding?.searchEditText?.requestFocus() } // focus the search
        setImeOptionSearchClicked()
      //  getMovieList(binding?.searchEditText?.text.toString())
        //callGetBox.invoke() // get default cart box when loading here
        return binding!!.root
    }

    private fun initValues() {
        arrayListMovies.clear()
        resetCall()
        adaptorMovie = AdaptorMovie(requireContext(),arrayListMovies,::onClickMovie)
        util.setRecycleView(binding!!.movieGridRecycle,adaptorMovie!!,null,requireContext(),GridModel(2,30),false)

    }

    fun onClickMovie(movie: Result) { // when click on movie
        findNavController().navigate(R.id.nav_movie_details, bundleOf(MOVIE_ID to movie.id))
    }
    fun setViewModelObservers() {
        viewModel.movieListLiveData.observe(viewLifecycleOwner, Observer{ // for sending to maintenance
            it?.let {
               // arrayListMovies.addAll(it.results)
                    hasMore = it.page < it.total_pages
                adaptorMovie?.updateList(it.results)
                checkIfArrayListIsEmpty(arrayListMovies)
            }
        })
        viewModel.movieListLiveDataOffline.observe(viewLifecycleOwner, Observer{ // for sending to maintenance
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

        viewModel.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                binding?.progress?.visibility = progress
                viewModel.setNetworkLoader(null)
            }
        })

        viewModel.errorViewModel.observe(viewLifecycleOwner) {
            it?.let { error ->
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
    fun setImeOptionSearchClicked() {
        binding?.searchEditText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
              if (binding?.searchEditText?.text?.isNotEmpty() == true) {
                 resetCall()
                  getMovieList(binding?.searchEditText?.text.toString())
              }
                return@setOnEditorActionListener true
            }
            false
        }
    }
    fun getMovieList(queryString: String) {
        if (util.isInternetAvailable(requireContext()))internetStatus = OnlineConnection()
        else
            internetStatus = Offline()
        viewModel.getSearchMovies(internetStatus!!,HashMap<String, Any>().also {
            it.put(PAGE,page)
            it.put(QUERY,queryString)
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
            getMovieList(binding?.searchEditText?.text.toString())
        }
            // do second call
    }

    private fun getHasMorePages(hasMore: Boolean): Boolean {
        return hasMore
    }
}
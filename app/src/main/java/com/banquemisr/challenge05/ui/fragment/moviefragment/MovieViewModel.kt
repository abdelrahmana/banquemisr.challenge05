package com.banquemisr.challenge05.ui.fragment.moviefragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.banquemisr.androidtask.data.source.remote.repository.MovieRepository
import com.banquemisr.currencyapp.ui.base.BaseViewModel
import com.banquemisr.challenge05.data.model.Genre
import com.banquemisr.challenge05.data.model.MovieResponse
import com.banquemisr.challenge05.data.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MovieViewModel @Inject constructor(private val repoMovie: MovieRepository) : BaseViewModel() {
    private val _movieListMutable = MutableLiveData<MovieResponse?>()
    val movieListLiveData :LiveData<MovieResponse?> = _movieListMutable

    private val _movieListMutableOffline = MutableLiveData<List<Result>?>()
    val movieListLiveDataOffline :LiveData<List<Result>?> = _movieListMutableOffline

    private val _movieMutable = MutableLiveData<Result?>()  // details
    val movieLiveData :LiveData<Result?> = _movieMutable
    private val _categoryTypesGenrMutable = MutableLiveData<List<Genre>?>()
    val categoryTypesGenrLiveData :LiveData<List<Genre>?> = _categoryTypesGenrMutable

    fun setMovieDetails(movie : Result?) {
        _movieMutable.postValue(movie)
    }
    fun setCategoryGenre(genre : List<Genre>?) {
        _categoryTypesGenrMutable.postValue(genre)
    }
    fun setMovieList(movieList : MovieResponse?) {
        _movieListMutable.postValue(movieList)
    }
    fun setMovieOfflineMode(movieList : List<Result>?) {
        _movieListMutableOffline.postValue(movieList)
    }
    fun getMovieList(status: InternetStatus, hashMap: HashMap<String, Any>){
        setNetworkLoader(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO) {
            // get movie depend on internet connection
            status.getMovies(repoMovie,this@MovieViewModel
                ,hashMap)
        }
    }
    fun getCategory(internetStatus: InternetStatus){
        setNetworkLoader(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO){
            internetStatus.getGenreList(repoMovie,this@MovieViewModel,)
        }
    }
    fun getMovieDetails(internetStatus: InternetStatus,movieId : Int){
        setNetworkLoader(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO) {
            internetStatus.getMovieDetails(repoMovie,this@MovieViewModel,movieId)
        }
    }
    fun getFilteredMovies(internetStatus: InternetStatus, genreId: Int, page: Int){
        setNetworkLoader(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO) {
            internetStatus.getFilteredMovie(repoMovie,this@MovieViewModel,genreId,page)
        }
    }
    fun getSearchMovies(internetStatus: InternetStatus, hashMap: HashMap<String, Any>){
        viewModelScope.launch(Dispatchers.IO) {
            setNetworkLoader(View.VISIBLE)
            internetStatus.getSearchMovieByName(repoMovie,this@MovieViewModel,hashMap)
        }
    }

    fun clearListener(fragment : Fragment){
        _movieListMutable.removeObservers(fragment)
        _movieListMutable.postValue(null)
        _movieListMutableOffline.removeObservers(fragment)
        _movieListMutableOffline.postValue(null)
        errorViewModel.removeObservers(fragment)
        setError(null)
        networkLoader.removeObservers(fragment)
        setNetworkLoader(null)

    }
}
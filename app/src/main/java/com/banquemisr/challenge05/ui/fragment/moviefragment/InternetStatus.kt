package com.banquemisr.challenge05.ui.fragment.moviefragment

import android.view.View
import com.banquemisr.androidtask.data.model.Constants
import com.banquemisr.androidtask.data.model.Constants.QUERY
import com.banquemisr.androidtask.data.source.remote.repository.MovieRepository

interface InternetStatus {
    suspend fun getMovies(repoMovie:MovieRepository, movieViewModel: MovieViewModel, hashMap : HashMap<String,Any>)
    suspend fun getFilteredMovie(repoMovie:MovieRepository,movieViewModel: MovieViewModel
                                 ,genreId : Int,page : Int)

    suspend fun getSearchMovieByName(repoMovie:MovieRepository, movieViewModel: MovieViewModel, hashMap : HashMap<String,Any>)

    suspend fun getMovieDetails(repoMovie:MovieRepository,movieViewModel: MovieViewModel
                                 ,movieId : Int)

    suspend fun getGenreList(repoMovie:MovieRepository,movieViewModel: MovieViewModel
                                )

}
class OnlineConnection() : InternetStatus {
    override suspend fun getMovies(repoMovie:MovieRepository, movieViewModel: MovieViewModel, hashMap : HashMap<String,Any>) {
        repoMovie.getMovieList(hashMap){ response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)

            response?.let { it ->
                movieViewModel.setMovieList(it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }

        }
    }

    override suspend fun getSearchMovieByName(
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel,
        hashMap: HashMap<String, Any>
    ) {
        repoMovie.getMovieByKeyWord(hashMap){ response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)

            response?.let { it ->
                movieViewModel.setMovieList(it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }

        }
    }

    override suspend fun getFilteredMovie(
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel,
        genreId: Int, page: Int
    ) {
        repoMovie.getFilteredMovies(HashMap<String,Any>().also {
            it.put(Constants.WITHGENRES,genreId)
            it.put(MovieFragment.PAGE,page)
        }){ response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)
            response?.let { it ->
                movieViewModel.setMovieList( it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }

        }
    }

    override suspend fun getMovieDetails(
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel,
        movieId: Int
    ) {
        repoMovie.getMovieDetails(movieId){ response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)
            response?.let { it ->
                movieViewModel.setMovieDetails(it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }

        }

    }

    override suspend fun getGenreList(
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel
    ) {
        repoMovie.getGenreTypes{ response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)
            response?.let { it ->
                movieViewModel.setCategoryGenre(it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }
        }
    }


}

class Offline () : InternetStatus {
    override suspend fun getMovies(
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel,
        hashMap: HashMap<String, Any>
    ) {
        repoMovie.getMovieFromLocal(0) { response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)

            response?.let { it ->
                movieViewModel.setMovieOfflineMode(it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }
        }


    }

    override suspend fun getFilteredMovie( // when filtered clicked
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel,
        genreId: Int,
        page: Int
    ) {
            repoMovie.getMovieFromLocal(genreId) { response, errors ->
                movieViewModel.setNetworkLoader(View.GONE)

                response?.let { it ->
                    movieViewModel.setMovieOfflineMode(it)
                }
                errors?.let { it ->
                    movieViewModel.setError(it)
                }
            }

    }
    override suspend fun getSearchMovieByName(
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel,
        hashMap: HashMap<String, Any>
    ) {
        repoMovie.getMovieByKeyWordLocale((hashMap.get(QUERY)?:"").toString()){ response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)
            response?.let { it ->
                movieViewModel.setMovieOfflineMode(it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }

        }
    }

    override suspend fun getMovieDetails(
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel,
        movieId: Int
    ) {
        repoMovie.getMovieDetailsFromLocal(movieId){ response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)
            response?.let { it ->
                movieViewModel.setMovieDetails(it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }

        }

    }

    override suspend fun getGenreList(
        repoMovie: MovieRepository,
        movieViewModel: MovieViewModel
    ) {
        repoMovie.getGenreFromLocal{ response, errors ->
            movieViewModel.setNetworkLoader(View.GONE)
            response?.let { it ->
                movieViewModel.setCategoryGenre(it)
            }
            errors?.let { it ->
                movieViewModel.setError(it)
            }
        }
    }
}
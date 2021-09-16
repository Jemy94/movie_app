package com.jemy.moviedb.data.remote

import com.jemy.moviedb.data.response.PopularMoviesResponse
import com.jemy.moviedb.utils.Constants
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET(Endpoints.POPULAR)
    fun getPopularMovies(
        @Query("sort_by") sortBy: String = Constants.POPULAR_QUERY,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): Single<Response<PopularMoviesResponse>>
}
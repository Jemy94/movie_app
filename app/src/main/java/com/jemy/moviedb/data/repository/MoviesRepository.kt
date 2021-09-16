package com.jemy.moviedb.data.repository

import com.jemy.moviedb.data.common.Resource
import com.jemy.moviedb.data.common.Validator
import com.jemy.moviedb.data.remote.ApiInterface
import com.jemy.moviedb.data.response.PopularMoviesResponse
import io.reactivex.Single
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val validator: Validator
) {

    fun getPopularMovies(): Single<Resource<PopularMoviesResponse>> {
        return apiInterface.getPopularMovies()
            .map { validator.validateResponse(it) }
            .map { Resource(it.state, if (it.data == null) null else it.data, it.message) }
    }
}
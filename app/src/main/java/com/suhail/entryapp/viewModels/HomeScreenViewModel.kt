package com.suhail.entryapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.suhail.entryapp.paging.MoviesRemoteMediator
import com.suhail.entryapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {


    @ExperimentalPagingApi
    val pager = Pager(PagingConfig(pageSize = 10),
        remoteMediator = MoviesRemoteMediator(repository, 1)) {
        Log.i("WorkingConcept","ViewModel")
            repository.getAllMoviesDao()

        }.flow
}
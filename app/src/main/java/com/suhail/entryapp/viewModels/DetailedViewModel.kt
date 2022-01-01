package com.suhail.entryapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.suhail.entryapp.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailedViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    var coverImage = MutableLiveData<String>()
    var posterImage = MutableLiveData<String>()
    var movieTitle = MutableLiveData<String>()
    var movieDescription = MutableLiveData<String>()


    init {
        initialzeItems()
    }

    private fun initialzeItems(){
        val item = stateHandle.get<Result>("itemDet")
        coverImage.value = item?.backdropPath
        posterImage.value = item?.posterPath
        movieTitle.value = item?.title
        movieDescription.value = item?.overview
    }
}
package com.suhail.entryapp.paging

import android.util.Log
import androidx.paging.*
import com.suhail.entryapp.Constants
import com.suhail.entryapp.data.PageKey
import com.suhail.entryapp.data.Result
import com.suhail.entryapp.repository.MovieRepository
import java.io.InvalidObjectException

@ExperimentalPagingApi
class MoviesRemoteMediator(
    private val repository: MovieRepository,
    private val initialPage: Int = 1
) : RemoteMediator<Int, Result>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
        Log.i("Working","Entered to load function")
        return try {
            val page = when (loadType) {
                LoadType.APPEND -> {

                    val remoteKey = getLastKey(state) ?: throw InvalidObjectException("Invalid")
                    Log.i("Working","Append - ${if (remoteKey!=null)remoteKey else null}")
                    remoteKey.next ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.PREPEND -> {
                    Log.i("Working","Prepend")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH -> {
                    val remoteKey = getClosestPage(state)
                    Log.i("Working","REfresh - ${if (remoteKey!=null)remoteKey else initialPage}")
                    remoteKey?.next?.minus(1) ?: initialPage
                }
            }

            Log.i("Working", "Page number-${page.toString()}")


            val responce = repository.getNowPlayingMovies(Constants.MOVIE_API, page)
            val endOfPagination = responce.body()?.results?.size!! < state.config.pageSize


            Log.i("Working" ,endOfPagination.toString())

            if (responce.isSuccessful) {
                responce.body().let {
                    if (loadType == LoadType.REFRESH) {
                        repository.deleteAllMovies()
                        repository.deleteAllPageKeys()
                    }
                    val prev = if (page == initialPage) null else page - 1
                    val next = if (endOfPagination) null else page + 1
                    val list = responce.body()?.results?.map { result ->
                        PageKey(result.title, next, prev)
                    }
                    if (list != null) {
                        repository.insertAllKeys(list)
                    }
                    it?.results?.let { it1 ->
                        repository.insertMovies(it1)
                    }

                }
                MediatorResult.Success(endOfPagination)
            } else {
                MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (e: Exception) {
            Log.i("error", e.message.toString())
            MediatorResult.Error(e)

        }
    }

    private suspend fun getClosestPage(state: PagingState<Int, Result>): PageKey? {

        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let { result ->
                repository.getAllPageKeys(result.title)

            }
        }
    }

    private suspend fun getLastKey(state: PagingState<Int, Result>): PageKey? {
        return state.lastItemOrNull()?.let {
            repository.getAllPageKeys(it.title)
        }
    }
}
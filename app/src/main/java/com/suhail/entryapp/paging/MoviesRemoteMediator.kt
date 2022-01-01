package com.suhail.entryapp.paging

import android.util.Log
import androidx.paging.*
import com.suhail.entryapp.data.PageKey
import com.suhail.entryapp.data.Result
import com.suhail.entryapp.repository.MovieRepository
import java.io.InvalidObjectException

@ExperimentalPagingApi
class MoviesRemoteMediator (
    private val repository : MovieRepository,
    private val initialPage:Int =1
        ) : RemoteMediator<Int,Result>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Result>): MediatorResult {
       return try {
           val page = when(loadType){
               LoadType.APPEND ->{

                   Log.i("stateWatch",state.toString())
                   Log.i("finalWork","append")
                   val remoteKey = getLastKey(state) ?: throw InvalidObjectException("Invalid")
                   Log.i("PageNumber",remoteKey.next.toString())
                   remoteKey.next?: return MediatorResult.Success(endOfPaginationReached = true)
               }
               LoadType.PREPEND ->{
                   Log.i("finalWork","prepend")
                   return MediatorResult.Success(endOfPaginationReached = true)
               }
               LoadType.REFRESH ->{
                   Log.i("finalWork","refresh")
                   Log.i("refreshState",state.toString())
                   val remoteKey = getClosestPage(state)
                   remoteKey?.next?.minus(1) ?: initialPage
               }
           }

           Log.i("PageNumber",page.toString())


           val responce = repository.getNowPlayingMovies("f6d8693db12e1b1399d7d2212be79767",page)
           val endOfPagination = responce.body()?.results?.size!! < state.config.pageSize

           Log.i("pageconfigResponce",responce.body()?.results?.size.toString())
           Log.i("pageconfig",state.config.pageSize.toString())
           Log.i("endofPagination",endOfPagination.toString())

           if(responce.isSuccessful){
               Log.i("finalWork","responceSuccessful")
               responce.body().let {
                   if (loadType == LoadType.REFRESH){
                       Log.i("finalWork","databaseContentUpdated")
                       repository.deleteAllMovies()
                       repository.deleteAllPageKeys()
                   }
                   val prev = if(page == initialPage) null else page-1
                   val next = if(endOfPagination)null else page+1
                   Log.i("finalWork","PageNumberUpdated ${prev.toString()}, ${next.toString()}")
                   Log.i("PageNumbers","${prev.toString()},${next.toString()}")
                   val list = responce.body()?.results?.map {result->
                       Log.i("finalWork","keytableUpdating ${PageKey(result.title,next,prev)}")
                       PageKey(result.title,next,prev)
                   }
                   if (list != null) {
                       Log.i("finalWork","valuesInsertedToKeytable")
                       repository.insertAllKeys(list)
                       Log.i("CheckingDB",repository.getAllPageKeys(responce.body()?.results?.get(0)?.title!!).toString())
                   }
                   it?.results?.let { it1 ->
                       Log.i("Movies",it1.toString())
                       repository.insertMovies(it1)
                   }

               }
               MediatorResult.Success(endOfPagination)
           }else{
               MediatorResult.Success(endOfPaginationReached = true)
           }
       }catch (e:Exception){
           Log.i("error",e.message.toString())
           MediatorResult.Error(e)

       }
    }

    private suspend fun getClosestPage(state: PagingState<Int, Result>) : PageKey?{

       return state.anchorPosition?.let {

           Log.i("anchorPosition",it.toString())
            state.closestItemToPosition(it)?.let {result->
               Log.i("finalWork","getClosestKey")
                Log.i("getClosest",repository.getAllPageKeys(result.title).toString())
                repository.getAllPageKeys(result.title)

            }
        }
    }

    private suspend fun getLastKey(state: PagingState<Int, Result>) : PageKey?{
        Log.i("stateDetails","${state.pages.map { 
          it.data.map { 
              it.title
          } }}")
        return state.lastItemOrNull()?.let {
            Log.i("movieDetailsGetLast",it.toString())
            Log.i("finalWork","getLastKey")
            Log.i("getLasttKEy",repository.getAllPageKeys(it.title).toString())
            repository.getAllPageKeys(it.title)
        }
    }
}
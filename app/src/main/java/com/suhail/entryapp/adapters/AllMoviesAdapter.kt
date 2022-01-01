package com.suhail.entryapp.adapters

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.suhail.entryapp.R
import com.suhail.entryapp.data.Result

class AllMoviesAdapter : PagingDataAdapter<Result,AllMoviesAdapter.MyViewHolder>(DIFF_UTIL) {

    companion object{
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val movieName : TextView = view.findViewById(R.id.Item_name)
        val moviePoster : ImageView = view.findViewById(R.id.item_thumb)

    }

    override fun onBindViewHolder(holder: AllMoviesAdapter.MyViewHolder, position: Int) {

        val item = getItem(position)
        holder.movieName.text = item?.originalTitle
        holder.moviePoster.load("https://image.tmdb.org/t/p/w500${item?.posterPath}"){
            placeholder(R.drawable.ic_baseline_movie_24)
            .transformations(RoundedCornersTransformation(30f))
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                item?.let { it1 -> it(it1) }
            }
        }

    }

    fun setOnClickListner(listner:(Result)->Unit){
        onItemClickListener = listner
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllMoviesAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item_layout,parent,false)
        return MyViewHolder(view)
    }

    private var onItemClickListener : ((Result)->Unit)?=null

}
package com.suhail.entryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import com.suhail.entryapp.R
import com.suhail.entryapp.databinding.FragmentDetailedBinding
import com.suhail.entryapp.viewModels.DetailedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedFragment : Fragment() {

    private val viewModel : DetailedViewModel by viewModels()
    lateinit var binding : FragmentDetailedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailedBinding.inflate(layoutInflater)

        viewModel.apply {
            coverImage.observe(viewLifecycleOwner, Observer {
                binding.posterPathDetail.load("https://image.tmdb.org/t/p/w500$it")
            })

            posterImage.observe(viewLifecycleOwner, Observer {
                binding.smallPosterDetail.load("https://image.tmdb.org/t/p/w500$it")
            })

            movieTitle.observe(viewLifecycleOwner, Observer {
                binding.movieNameDetail.text = it.toString()
            })

            movieDescription.observe(viewLifecycleOwner, Observer {
                binding.movieDescription.text = it.toString()
            })
        }
        setHasOptionsMenu(true)
        return binding.root
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

}
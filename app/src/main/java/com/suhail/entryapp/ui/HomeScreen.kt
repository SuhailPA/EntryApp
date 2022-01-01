package com.suhail.entryapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.suhail.entryapp.R
import com.suhail.entryapp.adapters.AllMoviesAdapter
import com.suhail.entryapp.databinding.FragmentHomeScreenBinding
import com.suhail.entryapp.viewModels.HomeScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class HomeScreen : Fragment() {

    lateinit var navController: NavController
    lateinit var adapterM: AllMoviesAdapter
    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel: HomeScreenViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = findNavController()
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterM = AllMoviesAdapter()
        binding.movieRecyclerView.apply {
            adapter = adapterM
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pager.collectLatest {
                Log.i("WorkingConcept","HomeScreen")
                adapterM.submitData(it)
            }
        }

        adapterM.setOnClickListner {
            val action = HomeScreenDirections.actionHomeScreenToDetailedFragment(it)
            navController.navigate(action)
        }


    }
}
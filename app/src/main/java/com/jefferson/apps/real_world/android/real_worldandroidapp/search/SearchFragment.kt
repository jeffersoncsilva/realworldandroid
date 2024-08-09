package com.jefferson.apps.real_world.android.real_worldandroidapp.search

import android.net.http.HttpException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jefferson.apps.real_world.android.real_worldandroidapp.R
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NoMoreAnimalsException
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.AnimalsAdapter
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.Event
import com.jefferson.apps.real_world.android.real_worldandroidapp.databinding.FragmentSearchBinding
import java.io.IOException

class SearchFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: FragmentSearchBinding? = null

    companion object{
        private const val ITEMS_PER_ROW = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stupUI()
    }

    private fun stupUI(){
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        observeViewStateUpdates(adapter)
    }

    private fun createAdapter(): AnimalsAdapter = AnimalsAdapter()

    private fun setupRecyclerView(adpt: AnimalsAdapter){
        binding.searchRecyclerView.apply{
            adapter = adpt
            layoutManager = GridLayoutManager(requireContext(), ITEMS_PER_ROW)
            setHasFixedSize(true)
        }
    }

    private fun observeViewStateUpdates(searchAdapter: AnimalsAdapter){

    }

    private fun handleFailures(failure: Event<Throwable>?){
        val unhandledFailure = failure?.getContentIfNotHandled() ?: return
        handleThrowable(unhandledFailure)
    }

    private fun handleThrowable(ex: Throwable){
        val fallbackMessage = getString(R.string.an_error_occurred)
        val snackbarMessage = when(ex){
            is NoMoreAnimalsException -> ex.message ?: fallbackMessage
            is IOException, is HttpException -> fallbackMessage
            else -> ""
        }
        if(snackbarMessage.isNotEmpty()){
            Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

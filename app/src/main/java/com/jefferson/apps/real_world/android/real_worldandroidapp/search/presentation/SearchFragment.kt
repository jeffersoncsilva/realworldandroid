package com.jefferson.apps.real_world.android.real_worldandroidapp.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jefferson.apps.real_world.android.real_worldandroidapp.R
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NoMoreAnimalsException
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.AnimalsAdapter
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.Event
import com.jefferson.apps.real_world.android.real_worldandroidapp.databinding.FragmentSearchBinding
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases.GetSearchFilters
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: SearchFragmentViewModel by viewModels()
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
        prepareForSearch()
    }

    private fun stupUI(){
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): AnimalsAdapter = AnimalsAdapter()

    private fun setupRecyclerView(adpt: AnimalsAdapter){
        binding.searchRecyclerView.apply{
            adapter = adpt
            layoutManager = GridLayoutManager(requireContext(), ITEMS_PER_ROW)
            setHasFixedSize(true)
        }
    }

    private fun subscribeToViewStateUpdates(searchAdapter: AnimalsAdapter){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect{
                    updateScreenState(it, searchAdapter)
                }
            }
        }
    }

    private fun updateScreenState(newState: SearchViewState, searchAdapter: AnimalsAdapter){
        val (
            inInitialState,
            searchResults,
            ageFilterValues,
            typefilterValues,
            searchingRemotely,
            noResultsState,
            failure
        ) = newState
    }

    private fun updateInitialStateViews(inInitialState: Boolean){
        binding.initialSearchImageView.isVisible = inInitialState
        binding.initialSearchText.isVisible = inInitialState
    }

    private fun updateRemoteSearchViews(searchingRemotely: Boolean){
        binding.searchRemotelyProgressBar.isVisible = searchingRemotely
        binding.searchRecyclerView.isVisible = searchingRemotely
    }

    private fun updateNoResultsViews(noResultsState: Boolean){
        binding.noSearchResultsImageView.isVisible = noResultsState
        binding.noSearchResultsText.isVisible = noResultsState
    }

    private fun handleFailures(failure: Event<Throwable>?){
        val unhandledFailure = failure?.getContentIfNotHandled() ?: return
        val fallbackMessage = getString(R.string.an_error_occurred)
        val snackbarMessage = if(unhandledFailure.message.isNullOrEmpty()){
            fallbackMessage
        }else{
            unhandledFailure.message!!
        }

        if(snackbarMessage.isNotEmpty()){
            Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupFilterValues(filter: AutoCompleteTextView, filterValues: List<String>){
        if(filterValues == null || filterValues.isEmpty()) return

        filter.setAdapter(createFilterAdapter(filterValues))
        filter.setText(GetSearchFilters.NO_FILTER_SELECTED, false)
    }

    private fun createFilterAdapter(adapterValues: List<String>): ArrayAdapter<String>{
        return ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, adapterValues)
    }

    private fun prepareForSearch(){
        setupFilterListeners()
        setupSearchViewListener()
        viewModel.onEvent(SearchEvent.PrepareForSearch)
    }

    private fun setupSearchViewListener(){
        val searchView = binding.searchWidget.search

        searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.onEvent(SearchEvent.QueryInput(query.orEmpty()))
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.onEvent(SearchEvent.QueryInput(newText.orEmpty()))
                    return true
                }
            }
        )
    }

    private fun setupFilterListeners(){
        with(binding.searchWidget){
            setupFilterListenerFor(age){ item ->
                viewModel.onEvent(SearchEvent.AgeValueSelected(item))
            }
            setupFilterListenerFor(type){ item ->
                viewModel.onEvent(SearchEvent.TypeValueSelected(item))
            }
        }
    }

    private fun setupFilterListenerFor(filter: AutoCompleteTextView, block: (item: String) -> Unit){
        filter.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            parent?.let{
                block(it.adapter.getItem(position) as String)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

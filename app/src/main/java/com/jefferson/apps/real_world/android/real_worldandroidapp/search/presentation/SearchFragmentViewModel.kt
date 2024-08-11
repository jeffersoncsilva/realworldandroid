package com.jefferson.apps.real_world.android.real_worldandroidapp.search.presentation

import android.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefferson.apps.real_world.android.logging.main.Logger
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NoMoreAnimalsException
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Animal
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.Pagination
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.model.mappers.UiAnimalMapper
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.createExceptionHandler
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model.SearchParameters
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model.SearchResults
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases.GetSearchFilters
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases.SearchAnimals
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases.SearchAnimalsRemotely
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchFragmentViewModel @Inject constructor(
    private val uiAnimalMapper: UiAnimalMapper,
    private val searchAnimalsRemotely: SearchAnimalsRemotely,
    private val searchAnimals: SearchAnimals,
    private val getSearchFilters: GetSearchFilters,
    private val compositeDisposable: CompositeDisposable
): ViewModel() {
    private var currentPage = 0
    private var remoteSearchJob: Job = Job()

    private val _state = MutableStateFlow(SearchViewState())
    private val querySubject = BehaviorSubject.create<String>()
    private val ageSubject = BehaviorSubject.createDefault("")
    private val typeSubject = BehaviorSubject.createDefault("")

    val state: StateFlow<SearchViewState> = _state.asStateFlow()

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.PrepareForSearch -> prepareForSearch()
            else -> onSearchParatemersUpdate(event)
        }
    }

    private fun prepareForSearch(){
        loadFilterValues()
        setupSearchSubscription()
    }

    private fun loadFilterValues(){
        val exceptionHandler = createExceptionHandler(message =  "Failed to get filter values!")
        viewModelScope.launch(exceptionHandler){
            val (ages, types) = getSearchFilters()
            udpateStateWithFilterValues(ages, types)
        }
    }

    private fun createExceptionHandler(message: String) : CoroutineExceptionHandler{
        return viewModelScope.createExceptionHandler(message){
            onFailure(it)
        }
    }

    private fun udpateStateWithFilterValues(ages: List<String>, types: List<String>){
        _state.update{ oldState ->
            oldState.updateToReadyToSearch(ages, types)
        }
    }

    private fun setupSearchSubscription(){
        searchAnimals(querySubject, ageSubject, typeSubject)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSearchResults(it) },
                { onFailure(it) }
            )
            .addTo(compositeDisposable)
    }

    private fun onSearchResults(searchResults: SearchResults){
        val (animals, searchParameters) = searchResults

        if(animals.isEmpty()){
            onEmptyCacheResults(searchParameters)
        }else{
            onAnimalList(animals)
        }
    }

    private fun onEmptyCacheResults(searchParameters: SearchParameters){
        _state.update { oldState ->
            oldState.updateToSearchingRemotely()
        }
        searchRemotely(searchParameters)
    }

    private fun searchRemotely(searchParameters: SearchParameters){
        val exceptionHandler = createExceptionHandler("Failed to search remotely.")
        remoteSearchJob = viewModelScope.launch(exceptionHandler){

        }
    }

    private fun onSearchParatemersUpdate(event: SearchEvent){
        remoteSearchJob.cancel(CancellationException("New search parameters incoming!"))
        when(event){
            is SearchEvent.QueryInput -> updateQuery(event.input)
            is SearchEvent.AgeValueSelected -> updateAgeValue(event.age)
            is SearchEvent.TypeValueSelected -> updateTypeValue(event.type)
            else -> Logger.d("wrong SearchEvent in onSearchParametersUpdate!")
        }
    }

    private fun updateQuery(input: String){
        resetPagination()
        querySubject.onNext(input)
        if(input.isEmpty()){
            setNoSearchQueryState()
        }else{
            setSearchingState()
        }
    }

    private fun updateAgeValue(age: String){
        ageSubject.onNext(age)
    }

    private fun updateTypeValue(type: String){
        typeSubject.onNext(type)
    }

    private fun setSearchingState(){
        _state.update { oldState -> oldState.updateToSearching() }
    }

    private fun setNoSearchQueryState(){
        _state.update { oldState -> oldState.updateToNoSearchQuery() }
    }

    private fun onAnimalList(animals: List<Animal>){
        _state.update { oldState -> oldState.updateToHasSearchResults(animals.map { uiAnimalMapper.mapToView(it) }) }
    }

    private fun resetPagination(){
        currentPage = 0
    }

    private fun onPaginationInfoObtained(pagination: Pagination){
        currentPage = pagination.currentPage
    }

    private fun onFailure(throwable: Throwable){
        _state.update{ oldState ->
            if(throwable is NoMoreAnimalsException){
                oldState.updateToNoResultsAvailable()
            } else {
                oldState.updateToHasFailure(throwable)
            }
        }
    }

    override fun onCleared(){
        super.onCleared()
        compositeDisposable.clear()
    }
}
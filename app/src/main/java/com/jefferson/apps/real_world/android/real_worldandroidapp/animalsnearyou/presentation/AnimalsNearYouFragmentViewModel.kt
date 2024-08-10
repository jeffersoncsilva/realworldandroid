package com.jefferson.apps.real_world.android.real_worldandroidapp.animalsnearyou.presentation

import android.net.http.NetworkException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefferson.apps.real_world.android.logging.main.Logger
import com.jefferson.apps.real_world.android.real_worldandroidapp.animalsnearyou.domain.usecases.GetAnimals
import com.jefferson.apps.real_world.android.real_worldandroidapp.animalsnearyou.domain.usecases.RequestNextPageOfAnimals
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NetworkUnavailableException
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NoMoreAnimalsException
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.Pagination
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.Event
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.model.UIAnimal
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.model.mappers.UiAnimalMapper
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.createExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalsNearYouFragmentViewModel @Inject constructor(
    private val getAnimals: GetAnimals,
    private val requestNextPageOfAnimals: RequestNextPageOfAnimals,
    private val uiAnimalMapper: UiAnimalMapper,
    private val compositeDisposable: CompositeDisposable
) : ViewModel(){
    companion object{
        const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
    }

    init{
        subscribeToAnimalUpdates()
    }

    private val _state = MutableStateFlow(AnimalsNearYouViewState())
    private var currentPage = 0

    val state: StateFlow<AnimalsNearYouViewState> = _state.asStateFlow()

    val isLastPage: Boolean
        get() = state.value.noMoreAnimalsNearby

    var isLoadingMoreAnimals: Boolean = false
        private set

    fun onEvent(event: AnimalsNearYouEvent){
        when(event){
            is AnimalsNearYouEvent.RequestInitialAnimalsList -> loadAnimals()
            is AnimalsNearYouEvent.RequestMoreAnimals -> loadNextAnimalPage()
        }
    }

    private fun subscribeToAnimalUpdates(){
        getAnimals()
            .map { animals -> animals.map { uiAnimalMapper.mapToView(it) } }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNewAnimalList(it) },
                { onFailure(it) }
            )
    }

    private fun onNewAnimalList(animals: List<UIAnimal>){
        Logger.d("got more animals!")
        val updatedAnimalSet = (state.value.animals + animals).toSet()
        _state.update{ oldState ->
            oldState.copy(loading = false, animals = updatedAnimalSet.toList())
        }
    }

    private fun loadAnimals(){
        if(state.value.animals.isEmpty()){
            loadNextAnimalPage()
        }
    }

    private fun loadNextAnimalPage(){
        isLoadingMoreAnimals = true
        val errorMessage = "Failed to fetch nearby animals"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage){ onFailure(it) }
        viewModelScope.launch(exceptionHandler) {
            Logger.d("requesting more animals.")
            val pagination = requestNextPageOfAnimals(++currentPage)
            onPaginationInfoObtained(pagination)
            isLoadingMoreAnimals = false
        }
    }

    private fun onPaginationInfoObtained(pagination: Pagination){
        currentPage = pagination.currentPage
    }

    private fun onFailure(failure: Throwable){
        when(failure){
            is NetworkException, is NetworkUnavailableException -> {
                _state.update{ oldState ->
                    oldState.copy(loading = false, failure = Event(failure))
                }
            }
            is NoMoreAnimalsException ->{
                _state.update { oldState ->
                    oldState.copy(noMoreAnimalsNearby = true, failure = Event(failure))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}
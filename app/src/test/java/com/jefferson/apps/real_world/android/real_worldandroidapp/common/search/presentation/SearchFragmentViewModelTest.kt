package com.jefferson.apps.real_world.android.real_worldandroidapp.common.search.presentation

import com.jefferson.apps.real_world.android.real_worldandroidapp.RxImmediateSchedulerRule
import com.jefferson.apps.real_world.android.real_worldandroidapp.TestCoroutineRule
import com.jefferson.apps.real_world.android.real_worldandroidapp.data.FakeRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.model.mappers.UiAnimalMapper
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.DispatchersProvider
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases.GetSearchFilters
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases.SearchAnimals
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases.SearchAnimalsRemotely
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.presentation.SearchFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class SearchFragmentViewModelTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    @get:Rule
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var viewModel: SearchFragmentViewModel
    private lateinit var repository: FakeRepository
    private lateinit var getSearchfilters: GetSearchFilters

    private val uiAnimalsMapper = UiAnimalMapper()

    @Before
    fun setup(){
        val dispatchersProvider = object : DispatchersProvider{
            override fun io() = testCoroutineRule.testDispatcher
        }

        repository = FakeRepository()
        getSearchfilters = GetSearchFilters(repository, dispatchersProvider)
        viewModel = SearchFragmentViewModel(
            uiAnimalsMapper,
            SearchAnimalsRemotely(repository, dispatchersProvider),
            SearchAnimals(repository),
            getSearchfilters,
            CompositeDisposable()
        )
    }
}
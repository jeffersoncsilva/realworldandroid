package com.jefferson.apps.real_world.android.real_worldandroidapp.animalsnearyou.presentation

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Interceptor

abstract class InifiniteScrollListener(
    private val layoutManager: GridLayoutManager,
    private val pageSize: Int
) : RecyclerView.OnScrollListener(){
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val fisrtVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if(!isLoading() && isLastPage()){
            if((visibleItemCount + fisrtVisibleItemPosition) >= totalItemCount && fisrtVisibleItemPosition >= 0 && totalItemCount >= pageSize){
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}
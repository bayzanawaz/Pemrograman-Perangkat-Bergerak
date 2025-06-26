package com.uilover.project2202.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.uilover.project2202.Domain.FilmItemModel
import com.uilover.project2202.Repository.MainRepository

class MainViewModel:ViewModel() {
    private val repository=MainRepository()

    fun loadUpcoming():LiveData<MutableList<FilmItemModel>> {
        return repository.loadUpcoming()
    }

    fun loadItems():LiveData<MutableList<FilmItemModel>>{
        return repository.loadItems()
    }
}
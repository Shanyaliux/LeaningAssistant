package com.shanya.leaningassistant.word

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WordRepository
    val allWords: LiveData<List<Word>>

    init {
        val wordDao = WordDatabase.getDatabase(application,viewModelScope).wordDao()
        repository = WordRepository(wordDao)
        allWords = repository.allWords
    }

    fun insert(vararg word: Word) = viewModelScope.launch{
        repository.insert(*word)
    }

    fun delete(vararg word: Word) = viewModelScope.launch {
        repository.delete(*word)
    }

    fun update(vararg word: Word) = viewModelScope.launch {
        repository.update(*word)
    }
}

package com.shanya.leaningassistant.word

import androidx.lifecycle.LiveData

class WordRepository(private val wordDao: WordDao) {
    val allWords: LiveData<List<Word>> = wordDao.getAllWordsLive()

    suspend fun insert(vararg word: Word){
        wordDao.insertWords(*word)
    }

    suspend fun delete(vararg  word: Word){
        wordDao.deleteWords(*word)
    }

    suspend fun update(vararg word: Word){
        wordDao.updateWords(*word)
    }
}
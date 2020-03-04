package com.shanya.leaningassistant.word

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {
    @Insert
    suspend fun insertWords(vararg words: Word?)

    @Update
    suspend fun updateWords(vararg words: Word?)

    @Delete
    suspend fun deleteWords(vararg words: Word?)

    @Query("SELECT * FROM word_database ORDER BY id DESC")
    fun getAllWordsLive():LiveData<List<Word>>
}
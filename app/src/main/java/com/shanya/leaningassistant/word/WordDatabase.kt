package com.shanya.leaningassistant.word

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Word::class),version = 1,exportSchema = false)
abstract class WordDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {database ->

                scope.launch {
                    val wordDao = database.wordDao()

                    val word = Word(0,"hello","你好")
                    wordDao.insertWords(word)
                }
            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: WordDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): WordDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordDatabase::class.java,
                    "word_database"
                ).addCallback(WordDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
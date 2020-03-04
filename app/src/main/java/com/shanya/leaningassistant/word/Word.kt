package com.shanya.leaningassistant.word

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_database")
data class Word (
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "word_english") val word:String,
    @ColumnInfo(name = "word_chinese") val chineseMeaning:String,
    @ColumnInfo(name = "chinese_invisible") var chineseInvisible: Boolean = false
)
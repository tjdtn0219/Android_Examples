package com.example.chapter7_words_book

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordDao {
    @Query("SELECT * FROM word ORDER BY id DESC")
    fun getAll(): List<Word>

    @Query("SELECT * FROM word ORDER BY ID DESC LIMIT 1")
    fun getLatestWord(): Word

    @Insert
    fun insert(word: Word)

    @Delete
    fun delete(word: Word)

    @Update
    fun update(word: Word)
}
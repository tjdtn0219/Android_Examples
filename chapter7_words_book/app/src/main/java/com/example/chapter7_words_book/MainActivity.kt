package com.example.chapter7_words_book

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.chapter7_words_book.databinding.ActivityMainBinding

/**
 * Recycler View, Adapter 실습
 * ChipGroup
 */

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let {
                startActivity(it)
            }
        }
    }

    private fun initRecyclerView() {
        val dummyList = mutableListOf<Word>(
            Word("weather", "날씨", "명사"),
            Word("honey", "꿀", "명사"),
            Word("run", "실행하다", "동사"),
        )

        wordAdapter = WordAdapter(dummyList, this)

        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }


    override fun onClick(word: Word) {
        Toast.makeText(this, "${word.text}가 클릭 되었습니다.", Toast.LENGTH_SHORT).show()
    }
}
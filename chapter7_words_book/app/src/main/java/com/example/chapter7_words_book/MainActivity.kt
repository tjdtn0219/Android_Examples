package com.example.chapter7_words_book

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.example.chapter7_words_book.databinding.ActivityMainBinding

/**
 * Recycler View, Adapter 실습
 * ChipGroup
 * kotlin-kapt 적용 (build.gradle)
 *
 */

class MainActivity : AppCompatActivity(), WordAdapter.ItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var wordAdapter: WordAdapter
    private var selectedWord: Word? = null
    private val updateAddWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        //단어가 추가될 때만, UI 업데이트를 하기위해 사용, AddActivity의 add()의 SetResult와 호환
        val isUpdated = result.data?.getBooleanExtra("isUpdated", false) ?: false
        if(result.resultCode == RESULT_OK && isUpdated) {
            updateAddWord()
        }
    }
    private val updateEditWordResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val editWord = result.data?.getParcelableExtra<Word>("editWord")
        if(result.resultCode == RESULT_OK && editWord != null) {
            updateEditWord(editWord)
        }
    }

    private fun updateEditWord(word: Word) {
        val index = wordAdapter.list.indexOfFirst { it.id == word.id }
        wordAdapter.list[index] = word
        runOnUiThread {
            selectedWord = word
            wordAdapter.notifyItemChanged(index)
            binding.textTextView.text = word.text
            binding.meanTextView.text = word.mean
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        binding.addButton.setOnClickListener {
            Intent(this, AddActivity::class.java).let {
                updateAddWordResult.launch(it)
            }
        }

        binding.deleteButton.setOnClickListener {
            delete()
        }

        binding.editButton.setOnClickListener {
            edit()
        }
    }

    private fun edit() {
        if(selectedWord == null) return

        val intent = Intent(this, AddActivity::class.java).putExtra("originWord", selectedWord)
        updateEditWordResult.launch(intent)
    }

    private fun delete() {
        if(selectedWord == null) return
        Thread {
            selectedWord?.let { word ->
                AppDatabase.getInstance(this)?.wordDao()?.delete(word)
                runOnUiThread {
                    wordAdapter.list.remove(word)
                    wordAdapter.notifyDataSetChanged()
                    binding.textTextView.text = ""
                    binding.meanTextView.text = ""
                    Toast.makeText(this, "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun initRecyclerView() {

        wordAdapter = WordAdapter(mutableListOf(), this)

        binding.wordRecyclerView.apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }

        Thread {
            val list = AppDatabase.getInstance(this)?.wordDao()?.getAll() ?: emptyList()
            wordAdapter.list.addAll(list)
            runOnUiThread {
                //Adapter에 list 추가했다고해서 UI반영 X, 아래와 같이 따로 함수 호출해야함
                //notifyDataSetChanged()를 사용할 때, Adapter에 없는 Index를 update하지 않게 주의
                wordAdapter.notifyDataSetChanged()
            }
        }.start()

    }

    private fun updateAddWord() {
        Thread {
            AppDatabase.getInstance(this)?.wordDao()?.getLatestWord()?.let {
                word -> wordAdapter.list.add(0, word)
                runOnUiThread {
                    wordAdapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    override fun onClick(word: Word) {
        selectedWord = word
        binding.textTextView.text = word.text
        binding.meanTextView.text = word.mean
    }
}
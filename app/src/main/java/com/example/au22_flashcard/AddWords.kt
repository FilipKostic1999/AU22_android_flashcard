package com.example.au22_flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddWords : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    private lateinit var db: AppDatabase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var SwedishView: EditText
    lateinit var EnglishView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)
        job = Job()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "word_items"
        )
            .fallbackToDestructiveMigration()
            .build()

        SwedishView = findViewById(R.id.Swedish)
        EnglishView = findViewById(R.id.English)

        val button = findViewById<Button>(R.id.addWordB)

        button.setOnClickListener {
            val sweText = SwedishView.text.toString()
            val engText = EnglishView.text.toString()

            val word = Word(0, "$engText", "$sweText")
            saveNewWord(word)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun saveNewWord(word: Word) {

        launch(Dispatchers.IO) {
            db.wordDao.insert(word)
        }
    }
}
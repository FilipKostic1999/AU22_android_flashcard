package com.example.au22_flashcard

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WordList : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var db: AppDatabase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    val wordList = mutableListOf<Word>()
    private val usedWords = mutableListOf<Word>()


    //  val defWordlist = loadAllWords()
    //  launch {
    //      val list = defWordlist.await()
    //      for(word in list){
    //          wordList.add(word)
    //      }
    //      showNewWord()
//
    //  }
    // job = Job()
//
    // db = Room.databaseBuilder(applicationContext,
    // AppDatabase::class.java,
    // "word_items")
    // .fallbackToDestructiveMigration()
    // .build()
    //alternativ 1

    fun getNewWord(): Word {
        if (wordList.size == usedWords.size) {
            usedWords.clear()
        }

        var word: Word? = null
        do {
            val rnd = (0 until wordList.size).random()
            word = wordList[rnd]
        } while (usedWords.contains(word))

        usedWords.add(word!!)

        return word
    }

    fun loadAllWords(): Deferred<List<Word>> =
        async(Dispatchers.IO) {
            db.wordDao.getAll()
        }


}

// 1. en till lista med redan använda ord
// 2. lista med index på använda ord
// 3. använt ord tas bort från listan
// 4. ordet håller reda på om det redan är använt




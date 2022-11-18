package com.example.au22_flashcard


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var db: AppDatabase
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var wordView: TextView
    var currentWord: Word? = null

    var wordList = mutableListOf<Word>()

    //added

   // var used = mutableListOf<Word>()

    //



    //val list : List<Word>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "word_items"
        )
            .fallbackToDestructiveMigration()
            .build()


        saveWord(Word(0, " Home", " Hem"))
        saveWord(Word(0, " Tree", " Trä"))
        saveWord(Word(0, " Dog", " Hund"))
        saveWord(Word(0, " Good bye", " Hej då"))
        saveWord(Word(0, " Thank you", " Tack"))
        saveWord(Word(0, " Welcome", " Välkommen"))
        saveWord(Word(0, " Computer", " Dator"))

        wordView = findViewById(R.id.TextView)



        wordView.setOnClickListener {

            // added



      //   val found =   used.any {it.used == true}


        /*    val distinct = used.toSet().toList()

            fou.text = ""

                for (i in 0 until distinct.size) {
                    fou.append(distinct[i].swedish)
                }

*/








            //

            revealTranslation()
        }
        val button2 = findViewById<Button>(R.id.removeB)
        val defWordlist = loadAllWords()
        launch {
            val list = defWordlist.await()



            for (word in list) {
                wordList.add(word)
            }


            showNewWord()
        }
        val button = findViewById<Button>(R.id.AddNewB)

        button.setOnClickListener {
            val intent = Intent(this, AddWords::class.java)
            startActivity(intent)
            finish()
        }
        button2.setOnClickListener {
            val badWord = currentWord
            if (badWord != null) {

                delete(badWord)
                showNewWord()
            }










        }
    }

    fun getNewWord(): Word {
        val usedWords = mutableListOf<Word>()
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

    fun delete(word: Word) =
        launch(Dispatchers.IO) {
            db.wordDao.delete(word)
        }

    fun loadAllWords(): Deferred<List<Word>> =
        async(Dispatchers.IO) {
            db.wordDao.getAll()
        }

    fun saveWord(word: Word) {

        launch(Dispatchers.IO) {
            db.wordDao.insert(word)
        }
    }


    fun revealTranslation() {
        wordView.text = currentWord?.english
    }


    fun showNewWord() {

        currentWord = getNewWord()
        wordView.text = currentWord?.swedish
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }

        return true
    }


}
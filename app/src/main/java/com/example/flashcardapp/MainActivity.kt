package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()
    var currCardDisplayedIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        val flashCardQuestion = findViewById<TextView>(R.id.flash_card_question)
        val flashCardAnswer = findViewById<TextView>(R.id.flash_card_answer)
        val addQuestion = findViewById<ImageView>(R.id.add_question_button)

        if(allFlashcards.size > 0){
            flashCardQuestion.text = allFlashcards[0].question
            flashCardAnswer.text = allFlashcards[0].answer
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->

            val data: Intent? = result.data

            if(data != null){

                val questionString = data.getStringExtra("QUESTION_KEY")
                val answerString = data.getStringExtra("ANSWER_KEY")

                flashCardQuestion.text = questionString
                flashCardAnswer.text = answerString

                Log.i("Elton: MainActivity", "question: $questionString")
                Log.i("Elton: MainActivity", "answer: $answerString")

                if(!questionString.isNullOrEmpty() && !answerString.isNullOrEmpty()){
                    flashcardDatabase.insertCard(Flashcard(questionString, answerString))
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                }
            }
            else {
                Log.i("Elton: MainActivity", "Returned null data from AddCardActivity")
            }
        }

        flashCardAnswer.visibility = View.INVISIBLE

        flashCardQuestion.setOnClickListener {
            flashCardAnswer.visibility = View.VISIBLE
            flashCardQuestion.visibility = View.INVISIBLE

            //Snackbar.make(flashCardQuestion, "Question button was clicked",
            //    Snackbar.LENGTH_SHORT).show()

            //Log.i("Elton", "Question button was clicked")
        }

        flashCardAnswer.setOnClickListener {
            flashCardAnswer.visibility = View.INVISIBLE
            flashCardQuestion.visibility = View.VISIBLE
        }

        addQuestion.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

        val nextButton = findViewById<ImageView>(R.id.next_card_button)
        nextButton.setOnClickListener {

            if(allFlashcards.isEmpty()){
                return@setOnClickListener
            }
            currCardDisplayedIndex++

            if(currCardDisplayedIndex >= allFlashcards.size){

                currCardDisplayedIndex = 0
            }

            allFlashcards = flashcardDatabase.getAllCards().toMutableList()

            val question = allFlashcards[currCardDisplayedIndex].question
            val answer = allFlashcards[currCardDisplayedIndex].answer

            flashCardQuestion.text =question
            flashCardAnswer.text = answer
        }

    }
}
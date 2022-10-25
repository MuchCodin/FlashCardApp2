package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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

            val answerSideView = findViewById<View>(R.id.flash_card_answer)
            val questionSideView = findViewById<View>(R.id.flash_card_question)
            // get the center for the clipping circle

// get the center for the clipping circle
            val cx = answerSideView.width / 2
            val cy = answerSideView.height / 2

// get the final radius for the clipping circle

// get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

// create the animator for this view (the start radius is zero)

// create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius)

            // hide the question and show the answer to prepare for playing the animation
            questionSideView.visibility = View.INVISIBLE
            answerSideView.visibility = View.VISIBLE

            anim.duration = 3000
            anim.start()
        }

        flashCardAnswer.setOnClickListener {
            flashCardAnswer.visibility = View.INVISIBLE
            flashCardQuestion.visibility = View.VISIBLE
        }

        addQuestion.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }

        val nextButton = findViewById<ImageView>(R.id.next_card_button)
        nextButton.setOnClickListener {

            if(allFlashcards.isEmpty()){
                return@setOnClickListener
            }

            val leftOutAnim = AnimationUtils.loadAnimation(it.getContext(), R.anim.left_out)
            val rightInAnim = AnimationUtils.loadAnimation(it.getContext(), R.anim.right_in)

            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts
                    flashCardQuestion.visibility = View.VISIBLE
                    flashCardAnswer.visibility = View.INVISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // this method is called when the animation is finished playing
                    flashCardQuestion.startAnimation(rightInAnim)

                    currCardDisplayedIndex++

                    if(currCardDisplayedIndex >= allFlashcards.size){

                        currCardDisplayedIndex = 0
                    }

                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()

                    val question = allFlashcards[currCardDisplayedIndex].question
                    val answer = allFlashcards[currCardDisplayedIndex].answer

                    flashCardQuestion.text =question
                    flashCardAnswer.text = answer

                    flashCardAnswer.visibility = View.INVISIBLE
                    flashCardQuestion.visibility = View.VISIBLE
                }
                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })
            flashCardQuestion.startAnimation(leftOutAnim)

        }
    }
}
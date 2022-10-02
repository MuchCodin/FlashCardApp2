package com.example.flashcardapp

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flashCardQuestion = findViewById<TextView>(R.id.flash_card_question)
        val flashCardAnswer = findViewById<TextView>(R.id.flash_card_answer)
        val addQuestion = findViewById<ImageView>(R.id.add_question_button)

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
            }
            else {
                Log.i("Elton: MainActivity", "Returned null data from AddCardActivity")
            }
        }

        flashCardAnswer.visibility = View.INVISIBLE

        flashCardQuestion.setOnClickListener {
            flashCardAnswer.visibility = View.VISIBLE
            flashCardQuestion.visibility = View.INVISIBLE

            Snackbar.make(flashCardQuestion, "Question button was clicked",
                Snackbar.LENGTH_SHORT).show()

            Log.i("Paulina", "Question button was clicked")
        }

        flashCardAnswer.setOnClickListener {
            flashCardAnswer.visibility = View.INVISIBLE
            flashCardQuestion.visibility = View.VISIBLE
        }

        addQuestion.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }
    }
}
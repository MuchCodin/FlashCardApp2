package com.example.flashcardapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val cancelButton = findViewById<ImageView>(R.id.cancel_button)
        val saveButton = findViewById<ImageView>(R.id.save_button)

        val userQuestion = findViewById<EditText>(R.id.user_question)
        val userAnswer = findViewById<EditText>(R.id.user_answer)


        cancelButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {
            val data = Intent()

            val questionString = userQuestion.text.toString()
            val questionAnswer = userAnswer.text.toString()
            data.putExtra("QUESTION_KEY", questionString)
            data.putExtra("ANSWER_KEY", questionAnswer)

            setResult(RESULT_OK, data)
            finish()
        }
    }
}
package com.example.login4


import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.login4.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding
    private var score: Int = 0
    private var totalQuestions: Int = 0
    private var percentage: Int = 0
    private lateinit var results: ArrayList<QuestionResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data passed from QuizActivity
        val intent = intent
        results = intent.getParcelableArrayListExtra("results") ?: arrayListOf()
        score = intent.getIntExtra("score", 0)
        totalQuestions = intent.getIntExtra("totalQuestions", 0)
        percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        // Bind values to the views
        binding.scoreTitle.text = if (percentage > 60) {
            "Congrats! You passed!"
        } else {
            "Oops! You failed"
        }
        binding.scoreTitle.setTextColor(if (percentage > 60) Color.GREEN else Color.RED)

        binding.scoreProgressText.text = "$percentage%"

        // Display results for each question
        val resultString = StringBuilder()
        results.forEach {
            val status = if (it.isCorrect) "Correct" else "Wrong"
            resultString.append("Q: ${it.question}\nYour Answer: ${it.selectedAnswer} - $status\n\n")
        }
        binding.scoreSubtitle.text = resultString.toString()

        // Finish button functionality
        binding.finishBtn.setOnClickListener {
            finish()  // Close the activity and go back
        }
    }
}

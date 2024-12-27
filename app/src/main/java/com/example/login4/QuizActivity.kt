    package com.example.login4

    import android.graphics.Color
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.os.CountDownTimer
    import android.util.Log
    import android.view.View
    import android.widget.Button
    import android.widget.Toast
    import androidx.appcompat.app.AlertDialog
    import com.example.login4.databinding.ActivityQuizBinding
    import com.example.login4.databinding.ScoreDialogBinding
    import com.google.firebase.firestore.FirebaseFirestore

    class QuizActivity : AppCompatActivity(), View.OnClickListener {

        companion object {
            var questionModelList: List<QuestionModel> = listOf()
            var time: String = ""
        }

        lateinit var binding: ActivityQuizBinding

        var currentQuestionIndex = 0
        var score = 0

        // To keep track of answers and correctness
        private val selectedAnswers = mutableMapOf<Int, String>()
        private val questionResults = mutableListOf<QuestionResult>()

        private val db = FirebaseFirestore.getInstance()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityQuizBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Button Click Listeners
            binding.apply {
                btn0.setOnClickListener(this@QuizActivity)
                btn1.setOnClickListener(this@QuizActivity)
                btn2.setOnClickListener(this@QuizActivity)
                btn3.setOnClickListener(this@QuizActivity)
                skipBtn.setOnClickListener(this@QuizActivity)
                prevBtn.setOnClickListener(this@QuizActivity)
                nextBtn.setOnClickListener(this@QuizActivity)

                backButton.setOnClickListener {
                    showExitDialog()
                }
            }

            loadQuestions()
            startTimer()
        }

        private fun startTimer() {
            val totalTimeInMillis = time.toInt() * 60 * 1000L
            object : CountDownTimer(totalTimeInMillis, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / 1000
                    val minutes = seconds / 60
                    val remainingSeconds = seconds % 60
                    binding.timerIndicatorTextview.text =
                        String.format("%02d:%02d", minutes, remainingSeconds)
                }

                override fun onFinish() {
                    finishQuiz()
                }
            }.start()
        }

        private fun loadQuestions() {
            if (currentQuestionIndex >= questionModelList.size) {
                finishQuiz()
                return
            }

            binding.apply {
                // Update question details
                questionIndicatorTextview.text = "Question ${currentQuestionIndex + 1}/${questionModelList.size}"
                questionProgressIndicator.progress =
                    ((currentQuestionIndex + 1).toFloat() / questionModelList.size.toFloat() * 100).toInt()
                questionTextview.text = questionModelList[currentQuestionIndex].question
                btn0.text = questionModelList[currentQuestionIndex].options[0]
                btn1.text = questionModelList[currentQuestionIndex].options[1]
                btn2.text = questionModelList[currentQuestionIndex].options[2]
                btn3.text = questionModelList[currentQuestionIndex].options[3]

                // Restore selected answer for the current question (if any)
                resetButtonColors()
                val selectedAnswer = selectedAnswers[currentQuestionIndex]
                when (selectedAnswer) {
                    btn0.text.toString() -> btn0.setBackgroundColor(getColor(R.color.orange))
                    btn1.text.toString() -> btn1.setBackgroundColor(getColor(R.color.orange))
                    btn2.text.toString() -> btn2.setBackgroundColor(getColor(R.color.orange))
                    btn3.text.toString() -> btn3.setBackgroundColor(getColor(R.color.orange))
                }

                // Disable "Previous" button if on the first question
                prevBtn.isEnabled = currentQuestionIndex > 0
            }
        }

        private fun resetButtonColors() {
            binding.apply {
                btn0.setBackgroundColor(getColor(R.color.gray))
                btn1.setBackgroundColor(getColor(R.color.gray))
                btn2.setBackgroundColor(getColor(R.color.gray))
                btn3.setBackgroundColor(getColor(R.color.gray))
            }
        }

        override fun onClick(view: View?) {
            val clickedBtn = view as? Button

            when (clickedBtn?.id) {
                R.id.next_btn -> {
                    val selectedAnswer = selectedAnswers[currentQuestionIndex]
                    if (selectedAnswer.isNullOrEmpty()) {
                        Toast.makeText(applicationContext, "Please select an answer to continue", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val isCorrect = selectedAnswer == questionModelList[currentQuestionIndex].correct
                    questionResults.add(QuestionResult(questionModelList[currentQuestionIndex].question, selectedAnswer, isCorrect))
                    if (isCorrect) score++

                    if (currentQuestionIndex >= questionModelList.size - 1) {
                        finishQuiz() // End quiz if on the last question
                    } else {
                        currentQuestionIndex++
                        loadQuestions()
                    }
                }

                R.id.skipBtn -> {
                    if (currentQuestionIndex >= questionModelList.size - 1) {
                        finishQuiz() // End quiz if on the last question
                    } else {
                        currentQuestionIndex++
                        loadQuestions()
                    }
                }


                R.id.prev_btn -> {
                    // Go back to the previous question
                    if (currentQuestionIndex > 0) {
                        currentQuestionIndex--
                        loadQuestions()
                    } else {
                        Toast.makeText(applicationContext, "This is the first question!", Toast.LENGTH_SHORT).show()
                    }
                }

                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3 -> {
                    // Select an answer
                    val selectedAnswerText = clickedBtn.text.toString()
                    selectedAnswers[currentQuestionIndex] = selectedAnswerText
                    resetButtonColors()
                    clickedBtn.setBackgroundColor(getColor(R.color.orange)) // Highlight the selected answer
                }
            }
        }
        private fun finishQuiz() {
            val totalQuestions = questionModelList.size
            val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

            // Show a summary of the quiz result
            val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
            dialogBinding.apply {
                scoreProgressIndicator.progress = percentage
                scoreProgressText.text = "$percentage%"

                if (percentage > 35) {
                    scoreTitle.text = "Congrats! You have passed"
                    scoreTitle.setTextColor(Color.GREEN)
                } else {
                    scoreTitle.text = "Oops! You have failed"
                    scoreTitle.setTextColor(Color.RED)
                }

                // Prepare detailed result string
                val resultString = StringBuilder()
                questionResults.forEach { result ->
                    val status = if (result.isCorrect) "Correct" else "Wrong"
                    resultString.append("Q: ${result.question}\n")
                    resultString.append("Your Answer: ${result.selectedAnswer} - $status\n")

                    // If the answer is wrong, display the correct answer
                    if (!result.isCorrect) {
                        val correctAnswer = questionModelList.find { it.question == result.question }?.correct
                        resultString.append("Correct Answer: $correctAnswer\n")
                    }
                    resultString.append("\n")
                }
                scoreSubtitle.text = resultString.toString()

                // Finish button functionality
                finishBtn.setOnClickListener {
                    saveResultToFirebase(percentage)
                    finish() // Close the QuizActivity and go back to the previous activity
                }
            }

            // Show the dialog after finishing the quiz
            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogBinding.root)
                .setCancelable(false)
                .create()

            alertDialog.show()
        }



        private fun saveResultToFirebase(percentage: Int) {
            // Store the results in Firebase Firestore
            val userResult = hashMapOf(
                "score" to score,
                "percentage" to percentage,
                "questions" to questionResults.map {
                    hashMapOf(
                        "question" to it.question,
                        "answer" to it.selectedAnswer,
                        "isCorrect" to it.isCorrect
                    )
                }
            )

            db.collection("quiz_results")
                .add(userResult)
                .addOnSuccessListener {
                    Log.d("QuizActivity", "Result saved successfully!")
                }
                .addOnFailureListener { e ->
                    Log.w("QuizActivity", "Error saving result", e)
                }
        }

        private fun showExitDialog() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Exit Quiz")
            builder.setMessage("Are you sure you want to exit the quiz?")
            builder.setCancelable(false)

            builder.setPositiveButton("Yes") { _, _ ->
                finish()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }

        override fun onBackPressed() {
            showExitDialog()
        }

        data class QuestionResult(val question: String, val selectedAnswer: String, val isCorrect: Boolean)
    }

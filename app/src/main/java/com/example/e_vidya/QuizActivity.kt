package com.example.e_vidya

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {

    private lateinit var txtQuestionNo: TextView
    private lateinit var txtQuestion: TextView
    private lateinit var questionList: MutableList<Question>
    private var standardName: String? = null

    private lateinit var txtSubject: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtProgress: TextView
    private lateinit var txtTimer: TextView

    private var subjectName: String? = null

    private lateinit var rb1: RadioButton
    private lateinit var rb2: RadioButton
    private lateinit var rb3: RadioButton
    private lateinit var rb4: RadioButton

    private lateinit var radioGroup: RadioGroup
    private lateinit var btnNext: Button

    private var currentQuestion = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz)
        txtSubject = findViewById(R.id.txtSubject)

        progressBar = findViewById(R.id.progressBar)
        txtProgress = findViewById(R.id.txtProgress)
        txtTimer = findViewById(R.id.txtTimer)

        txtQuestionNo = findViewById(R.id.txtQuestionNo)
        txtQuestion = findViewById(R.id.txtQuestion)

        rb1 = findViewById(R.id.rb1)
        rb2 = findViewById(R.id.rb2)
        rb3 = findViewById(R.id.rb3)
        rb4 = findViewById(R.id.rb4)

        radioGroup = findViewById(R.id.radioGroup)
        btnNext = findViewById(R.id.btnNext)

        subjectName =
            intent.getStringExtra("subjectName")

        standardName =
            intent.getStringExtra("standardName")

        txtSubject.text =
            "$standardName • $subjectName"


        val standard =
            when(standardName){

                "9th Standard" -> "9"
                "10th Standard" -> "10"
                "11th Standard" -> "11"
                "12th Standard" -> "12"

                else -> "9"
            }

        val subject =
            when(subjectName){

                "Mathematics" -> "math"
                "Physics" -> "physics"
                "Chemistry" -> "chemistry"
                "Biology" -> "biology"
                "Computer" -> "computer"
                "English" -> "english"

                else -> "math"
            }

        val fileName =
            "${standard}_${subject}.json"

        questionList =
            JsonLoader.loadQuestions(
                this,
                fileName
            ).shuffled()
                .take(10)
                .toMutableList()

        loadQuestion()
        startTimer()

        btnNext.setOnClickListener {

            val selectedId =
                radioGroup.checkedRadioButtonId

            if (selectedId == -1) {

                Toast.makeText(
                    this,
                    "Select an answer",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val selectedAnswer =
                when (selectedId) {

                    R.id.rb1 -> 1
                    R.id.rb2 -> 2
                    R.id.rb3 -> 3
                    else -> 4
                }

            questionList[currentQuestion].selectedAnswer =
                selectedAnswer

            if (selectedAnswer ==
                questionList[currentQuestion].correctAnswer
            ) {
                score++
            }

            currentQuestion++

            if (currentQuestion < questionList.size) {

                loadQuestion()

            } else {

                val intent =
                    Intent(
                        this,
                        ResultActivity::class.java
                    )

                intent.putExtra(
                    "score",
                    score
                )

                intent.putExtra(
                    "total",
                    questionList.size
                )

                intent.putExtra(
                    "subject",
                    subjectName
                )

                intent.putExtra(
                    "standard",
                    standardName
                )

                startActivity(intent)

                finish()
            }
        }
    }

    private fun loadQuestion() {

        radioGroup.clearCheck()

        val question =
            questionList[currentQuestion]

        txtQuestionNo.text =
            "Question ${currentQuestion + 1}/${questionList.size}"

        txtQuestion.text =
            question.question

        rb1.text = question.option1
        rb2.text = question.option2
        rb3.text = question.option3
        rb4.text = question.option4

        val progress =
            ((currentQuestion + 1) * 100) /
                    questionList.size

        progressBar.progress =
            progress

        txtProgress.text =
            "$progress%"

        if (currentQuestion ==
            questionList.size - 1
        ) {

            btnNext.text =
                "Finish Quiz"

        } else {

            btnNext.text =
                "Submit Answer"
        }
    }

    private fun startTimer() {

        object : CountDownTimer(
            600000,
            1000
        ) {

            override fun onTick(
                millisUntilFinished: Long
            ) {

                val minutes =
                    millisUntilFinished / 1000 / 60

                val seconds =
                    (millisUntilFinished / 1000) % 60

                txtTimer.text =
                    String.format(
                        "%02d:%02d",
                        minutes,
                        seconds
                    )
            }

            override fun onFinish() {

                Toast.makeText(
                    this@QuizActivity,
                    "Time Up!",
                    Toast.LENGTH_LONG
                ).show()

                val intent =
                    Intent(
                        this@QuizActivity,
                        ResultActivity::class.java
                    )

                intent.putExtra(
                    "score",
                    score
                )

                intent.putExtra(
                    "total",
                    questionList.size
                )

                intent.putExtra(
                    "subject",
                    subjectName
                )

                intent.putExtra(
                    "standard",
                    standardName
                )

                startActivity(intent)

                finish()
            }

        }.start()
    }
}
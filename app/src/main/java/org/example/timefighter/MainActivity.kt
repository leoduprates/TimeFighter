package org.example.timefighter

import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var score: Int = 0
    private var gameStarted: Boolean = false
    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown: Long = 6000
    private val countDownInterval: Long = 1000
    internal var timeLeftOnTimer: Long = 60000

    private lateinit var tapMeButton: Button
    private lateinit var gameScoreTextView: TextView
    private lateinit var timeLeftTextView: TextView

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arg = intent.getBooleanExtra("runTests", false)
        Log.d(TAG, "Argument: $arg")

        Log.d(TAG, "onCreate called. Score is: $score")

        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        tapMeButton.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionAbout) {
            showInfo()
        }

        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.aboutTitle, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called.")
    }

    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.yourScore, score)

        val restoredTime = timeLeftOnTimer / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, restoredTime)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        countDownTimer.start()
        gameStarted = true
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun resetGame() {
        score = 0
        gameScoreTextView.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()
        resetGame()
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }

        score += 1
        val newScore = getString(R.string.yourScore, score)
        gameScoreTextView.text = newScore

        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        gameScoreTextView.startAnimation(blinkAnimation)
    }
}
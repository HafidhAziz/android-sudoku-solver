package com.example.sudokusolver

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sudokusolver.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainView {

    lateinit var binding: ActivityMainBinding
    private lateinit var countDownTimer: CountDownTimer

    companion object {
        private const val DEFAULT_TIME = 600L
        private const val MILLISECONDS = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupView()
        setupSudokuNumberAdapter()
    }

    override fun setupView() {
        binding.apply {
            btnNewGame.setOnClickListener {
                startNewGame()
            }
            btnSolve.setOnClickListener {
                solveGame()
            }
        }
    }

    override fun setupSudokuNumberAdapter() {
        val numbers = resources.getStringArray(R.array.sudoku_numbers)

    }

    override fun startNewGame() {
        resetTimer()
        manageSolveButton(true)
        // todo reset board
    }

    override fun solveGame() {
        // todo solve game
    }

    override fun manageSolveButton(enable: Boolean) {
        binding.apply {
            btnSolve.isClickable = enable
            btnSolve.isEnabled = enable
            btnSolve.alpha = if (enable) 1f else 0.5f
        }
    }

    override fun resetTimer() {
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        countDownTimer = object : CountDownTimer((DEFAULT_TIME * MILLISECONDS), MILLISECONDS) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity, getString(R.string.time_up), Toast.LENGTH_LONG)
                    .show()
                // todo reset board
            }

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text = getTimeoutFormat(millisUntilFinished)
            }

        }.start()
    }

    override fun getTimeoutFormat(closedTimeout: Long?): String {
        var timeoutFormatted = ""
        closedTimeout?.let {
            timeoutFormatted = String.format(
                "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(it),
                TimeUnit.MILLISECONDS.toMinutes(it) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(
                        it
                    )
                ),
                TimeUnit.MILLISECONDS.toSeconds(it) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(
                        it
                    )
                )
            )
        }
        return timeoutFormatted
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        super.onDestroy()
    }

}
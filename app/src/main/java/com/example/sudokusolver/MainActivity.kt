package com.example.sudokusolver

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.*
import com.example.sudokusolver.adapter.SudokuNumberAdapter
import com.example.sudokusolver.data.SudokuNumber
import com.example.sudokusolver.databinding.ActivityMainBinding
import com.example.sudokusolver.util.toPx
import java.util.concurrent.TimeUnit

/**
 * Created by M Hafidh Abdul Aziz on 13/02/21.
 */

class MainActivity : AppCompatActivity(), MainView, SudokuNumberAdapter.ClickItemListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var numberAdapter: SudokuNumberAdapter
    private lateinit var numberList: MutableList<SudokuNumber>
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
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(9, RecyclerView.VERTICAL)
        binding.numbersRecycler.let {
            it.hasFixedSize()
            it.itemAnimator = null
            it.layoutManager = staggeredGridLayoutManager
            it.addItemDecoration(object : DividerItemDecoration(it.context, VERTICAL) {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(8.toPx(), 0.toPx(), 0.toPx(), 0.toPx())
                }

                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

                }
            })

            val numbers = resources.getStringArray(R.array.sudoku_numbers)
            numberList = mutableListOf()
            for (position in numbers.indices) {
                numberList.add(SudokuNumber(numbers[position]))
            }

            numberAdapter = SudokuNumberAdapter(numberList)
            numberAdapter.setClickItemListener(this)
            binding.numbersRecycler.adapter = numberAdapter
        }
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
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.time_up),
                    Toast.LENGTH_LONG
                )
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

    override fun onClickItemListener(number: String) {
        //todo sudoku choose number
    }

}
package com.example.sudokusolver

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.*
import com.example.sudokusolver.adapter.SudokuNumberAdapter
import com.example.sudokusolver.data.SudokuNumber
import com.example.sudokusolver.databinding.ActivityMainBinding
import com.example.sudokusolver.util.TableEntryTextView
import com.example.sudokusolver.util.toPx
import java.util.concurrent.TimeUnit

/**
 * Created by M Hafidh Abdul Aziz on 13/02/21.
 */

class MainActivity : AppCompatActivity(), MainView, SudokuNumberAdapter.ClickItemListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var numberAdapter: SudokuNumberAdapter
    private lateinit var numberList: MutableList<SudokuNumber>
    private lateinit var countDownTimer: CountDownTimer

    companion object {
        private const val DEFAULT_TIME = 600L
        private const val MILLISECONDS = 1000L
        private const val BOARD_SIZE = 9
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupView()
        setupSudokuNumberAdapter()
        setupTableBoard()
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
        manageSolveButton(false)
    }

    override fun setupSudokuNumberAdapter() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            BOARD_SIZE,
            RecyclerView.VERTICAL
        )
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
        clearBoard()
        manageSolveButton(true)
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
        countDownTimer = object : CountDownTimer((DEFAULT_TIME * MILLISECONDS), MILLISECONDS) {
            override fun onFinish() {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.time_up),
                    Toast.LENGTH_LONG
                ).show()
                clearBoard()
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
        cancelTimer()
        super.onDestroy()
    }

    override fun onClickItemListener(number: String) {
        for (i in 0 until binding.tableSudoku.childCount) { //loops through all rows in tableLayout
            val tempTR = binding.tableSudoku.getChildAt(i) as TableRow //sets a temp TableRow
            for (j in 0 until tempTR.childCount) { //loops through all textviews in current TableRow
                val textView: TableEntryTextView = tempTR.getChildAt(j) as TableEntryTextView
                if (textView.isSelected) {
                    textView.text = number
                }
            }
        }
    }

    override fun clearBoard() {
        for (i in 0 until binding.tableSudoku.childCount) {
            val tempTR = binding.tableSudoku.getChildAt(i) as TableRow
            for (j in 0 until tempTR.childCount) {
                val textView: TableEntryTextView = tempTR.getChildAt(j) as TableEntryTextView
                textView.text = ""
                textView.setEditableCell(true)
            }
        }
    }

    override fun setupTableBoard() {
        for (i in 0 until BOARD_SIZE) {
            val tableRow = TableRow(this)
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT
            )
            tableRow.id = i + 100 //sets id between 100 and 100 + boardSize
            for (j in 0 until BOARD_SIZE) {
                val textView = TableEntryTextView(this)
                textView.id = i * BOARD_SIZE + j //sets id between 0 and 80
                textView.setEditableCell(false)
                setupBoardBackground(textView, i, j)
                textView.setOnClickListener {
                    resetItemBoardBackground()
                    textView.isSelected = true
                    setupBoardBackground(textView, i, j)
                }
                binding.tableSudoku.post {
                    textView.height = binding.tableSudoku.width / BOARD_SIZE
                }
                //http://stackoverflow.com/questions/3591784/getwidth-and-getheight-of-view-returns-0
                tableRow.addView(textView)
            }
            binding.tableSudoku.addView(tableRow)
        }
    }

    override fun setupBoardBackground(
        textView: TableEntryTextView,
        positionX: Int,
        positionY: Int
    ) {
        if ((positionX + positionY) % 2 == 0) {
            textView.setBackgroundResource(if (textView.isSelected) R.drawable.bg_cell_default_blue_dark_selected else R.drawable.bg_cell_default_blue_dark)
        } else {
            textView.setBackgroundResource(if (textView.isSelected) R.drawable.bg_cell_default_blue_soft_selected else R.drawable.bg_cell_default_blue_soft)
        }
    }

    override fun resetItemBoardBackground() {
        for (i in 0 until binding.tableSudoku.childCount) {
            val tempTR = binding.tableSudoku.getChildAt(i) as TableRow
            for (j in 0 until tempTR.childCount) {
                val textView: TableEntryTextView = tempTR.getChildAt(j) as TableEntryTextView
                textView.isSelected = false
                setupBoardBackground(textView, i, j)
            }
        }
    }

    override fun cancelTimer() {
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

}
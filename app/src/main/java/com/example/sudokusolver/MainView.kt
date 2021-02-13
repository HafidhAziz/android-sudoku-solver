package com.example.sudokusolver

import com.example.sudokusolver.util.TableEntryTextView

/**
 * Created by M Hafidh Abdul Aziz on 12/02/21.
 */

interface MainView {
    fun setupView()
    fun setupSudokuNumberAdapter()
    fun startNewGame()
    fun solveGame()
    fun manageSolveButton(enable: Boolean)
    fun resetTimer()
    fun getTimeoutFormat(closedTimeout: Long?): String
    fun clearBoard()
    fun setupTableBoard()
    fun setupBoardBackground(
        textView: TableEntryTextView,
        positionX: Int,
        positionY: Int
    )
    fun resetItemBoardBackground()
    fun cancelTimer()
}
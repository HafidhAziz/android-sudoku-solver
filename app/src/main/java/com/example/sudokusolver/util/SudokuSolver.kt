package com.example.sudokusolver.util

import java.util.stream.IntStream

/**
 * Created by M Hafidh Abdul Aziz on 14/02/21.
 */
internal class SudokuSolver {

    companion object {
        private const val BOARD_SIZE = 9
        private const val SUBSECTION_SIZE = 3
        private const val BOARD_START_INDEX = 0
        private const val NO_VALUE = 0
        private const val MIN_VALUE = 1
        private const val MAX_VALUE = 9
    }

    fun solve(board: Array<IntArray>): Boolean {
        for (row in BOARD_START_INDEX until BOARD_SIZE) {
            for (column in BOARD_START_INDEX until BOARD_SIZE) {
                if (board[row][column] == NO_VALUE) {
                    for (k in MIN_VALUE..MAX_VALUE) {
                        board[row][column] = k
                        if (isValid(board, row, column) && solve(board)) {
                            return true
                        }
                        board[row][column] = NO_VALUE
                    }
                    return false
                }
            }
        }
        return true
    }

    private fun isValid(board: Array<IntArray>, row: Int, column: Int): Boolean {
        return (rowConstraint(board, row)
                && columnConstraint(board, column)
                && subsectionConstraint(board, row, column))
    }

    private fun rowConstraint(board: Array<IntArray>, row: Int): Boolean {
        val constraint = BooleanArray(BOARD_SIZE)
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
            .allMatch { column: Int -> checkConstraint(board, row, constraint, column) }
    }

    private fun columnConstraint(board: Array<IntArray>, column: Int): Boolean {
        val constraint = BooleanArray(BOARD_SIZE)
        return IntStream.range(BOARD_START_INDEX, BOARD_SIZE)
            .allMatch { row: Int -> checkConstraint(board, row, constraint, column) }
    }

    private fun subsectionConstraint(board: Array<IntArray>, row: Int, column: Int): Boolean {
        val constraint = BooleanArray(BOARD_SIZE)
        val subsectionRowStart = row / SUBSECTION_SIZE * SUBSECTION_SIZE
        val subsectionRowEnd = subsectionRowStart + SUBSECTION_SIZE
        val subsectionColumnStart = column / SUBSECTION_SIZE * SUBSECTION_SIZE
        val subsectionColumnEnd = subsectionColumnStart + SUBSECTION_SIZE
        for (row in subsectionRowStart until subsectionRowEnd) {
            for (column in subsectionColumnStart until subsectionColumnEnd) {
                if (!checkConstraint(board, row, constraint, column)) return false
            }
        }
        return true
    }

    private fun checkConstraint(
        board: Array<IntArray>,
        row: Int,
        constraint: BooleanArray,
        column: Int
    ): Boolean {
        if (board[row][column] != NO_VALUE) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true
            } else {
                return false
            }
        }
        return true
    }
}
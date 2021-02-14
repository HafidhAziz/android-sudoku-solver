package com.example.sudokusolver.util

import android.content.Context
import com.example.sudokusolver.R
import java.util.*

/**
 * Created by M Hafidh Abdul Aziz on 13/02/21.
 */

class TableBoard(private var size: Int, private val context: Context) {

    private var data: Array<IntArray> = Array(size) { IntArray(size) }
    private var solvedData: Array<IntArray> = Array(size) { IntArray(size) }
    private var entriesData: ArrayList<Coordinate>? = null

    private var duplicateRowFound: Boolean
    private var duplicateColumnFound: Boolean
    private var duplicateBoxFound: Boolean

    private class Coordinate(var x: Int, var y: Int)

    init {
        entriesData = ArrayList()
        duplicateRowFound = false
        duplicateColumnFound = false
        duplicateBoxFound = false
    }

    fun setData(indexById: Int, value: Int) {
        val x = indexById % size
        val y = indexById / size
        data[x][y] = value
        var duplicateEntryFound = false
        for (coordinate in entriesData!!) {
            if (coordinate.x == x && coordinate.y == y) {
                duplicateEntryFound = true
                break
            }
        }
        if (!duplicateEntryFound) {
            entriesData?.add(Coordinate(x, y))
        }
        checkDuplicateDataInput(indexById)
    }

    fun checkIsSolvable(): Boolean {
        val solverAlgorithm = SudokuSolver()
        for (i in 0 until size) {
            System.arraycopy(data[i], 0, solvedData[i], 0, size)
        }
        return !duplicateRowFound && !duplicateColumnFound && !duplicateBoxFound &&
                solverAlgorithm.solve(solvedData)
    }

    fun getEntriesSize(): Int {
        return entriesData?.size ?: 0
    }

    fun deleteData() {
        for (i in 0 until size) {
            for (j in 0 until size) {
                data[i][j] = 0
            }
        }
        entriesData?.clear()
        duplicateRowFound = false
        duplicateColumnFound = false
        duplicateBoxFound = false
    }

    fun getSolvedData(index: Int): Int {
        return solvedData[index % size][index / size]
    }

    fun getSize(): Int {
        return size
    }

    private fun checkDuplicateDataInput(indexById: Int) {
        val x = indexById % size
        val y = indexById / size
        val value = data[x][y]
        if (value != 0) {
            duplicateRowFound = findDuplicateValueInRow(y, value)
            duplicateColumnFound = findDuplicateValueInColumn(x, value)
            duplicateBoxFound = findDuplicateValueInBox(x, y, value)
        }
    }

    private fun findDuplicateValueInRow(y: Int, value: Int): Boolean {
        var duplicateFound = false
        for (x in 0 until size) {
            if (data[x][y] == value) {
                if (duplicateFound) {
                    return true
                }
                duplicateFound = true
            }
        }
        return false
    }

    private fun findDuplicateValueInColumn(x: Int, value: Int): Boolean {
        var duplicateFound = false
        for (y in 0 until size) {
            if (data[x][y] == value) {
                if (duplicateFound) {
                    return true
                }
                duplicateFound = true
            }
        }
        return false
    }

    private fun findDuplicateValueInBox(
        startX: Int,
        startY: Int,
        value: Int
    ): Boolean {
        var mStartX = startX
        var mStartY = startY
        mStartX -= mStartX % 3
        mStartY -= mStartY % 3
        var duplicateFound = false
        for (x in 0..2) {
            for (y in 0..2) {
                if (data[x + mStartX][y + mStartY] == value) {
                    if (duplicateFound) {
                        return true
                    }
                    duplicateFound = true
                }
            }
        }
        return false
    }

    fun getUnsolvableMessage(): String {
        return when {
            duplicateRowFound -> {
                context.getString(R.string.solve_me_duplicate_row_error)
            }
            duplicateColumnFound -> {
                context.getString(R.string.solve_me_duplicate_column_error)
            }
            duplicateBoxFound -> {
                context.getString(R.string.solve_me_duplicate_box_error)
            }
            else -> {
                context.getString(R.string.solve_me_error)
            }
        }
    }

}
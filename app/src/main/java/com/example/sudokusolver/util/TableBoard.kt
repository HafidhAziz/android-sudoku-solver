package com.example.sudokusolver.util

import java.util.*

/**
 * Created by M Hafidh Abdul Aziz on 13/02/21.
 */

class TableBoard(private var size: Int) {

    private var data: Array<IntArray> = Array(size) { IntArray(size) }
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
    }

    fun checkIsSolvable(): Boolean {
        return !duplicateRowFound && !duplicateColumnFound && !duplicateBoxFound
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
        duplicateColumnFound = false
        duplicateRowFound = false
        duplicateBoxFound = false
    }

}
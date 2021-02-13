package com.example.sudokusolver.util

import android.content.res.Resources

/**
 * Created by M Hafidh Abdul Aziz on 13/02/21.
 */

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
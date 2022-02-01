package com.zihad.imagefilteringapp.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CoroutinesUtil {
    fun io(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch { work() }
}
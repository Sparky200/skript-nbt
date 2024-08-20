package dev.sparky200.skriptnbt.nbt

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
interface NbtTag<T> {
    val value: Any?

    fun toNbtString(): String = value.toString()
}
package dev.sparky200.skriptnbt.nbt

import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
class NbtByteArrayTag(override var value: ByteArray) : NbtTag<ByteArray> {
    override fun toNbtString(): String = value.joinToString(separator = ",", prefix = "[", postfix = "]")
}
@Experimental
class NbtIntArrayTag(override var value: IntArray) : NbtTag<IntArray> {
    override fun toNbtString(): String = value.joinToString(separator = ",", prefix = "[", postfix = "]")
}
@Experimental
class NbtLongArrayTag(override var value: LongArray) : NbtTag<LongArray> {
    override fun toNbtString(): String = value.joinToString(separator = ",", prefix = "[", postfix = "]")
}

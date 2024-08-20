package dev.sparky200.skriptnbt.nbt

import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
class NbtByteTag(override val value: Byte) : NbtTag<Byte>
@Experimental
class NbtDoubleTag(override var value: Double) : NbtTag<Double>
@Experimental
class NbtFloatTag(override var value: Float) : NbtTag<Float>
@Experimental
class NbtIntTag(override var value: Int) : NbtTag<Int>
@Experimental
class NbtLongTag(override var value: Long) : NbtTag<Long>
@Experimental
class NbtShortTag(override val value: Short) : NbtTag<Short>
@Experimental
class NbtStringTag(override var value: String) : NbtTag<String> {
    override fun toNbtString(): String = "\"$value\""
}
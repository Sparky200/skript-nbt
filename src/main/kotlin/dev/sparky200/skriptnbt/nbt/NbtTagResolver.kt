package dev.sparky200.skriptnbt.nbt

import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
fun interface NbtTagResolver {
    fun resolve(value: Any?): NbtTag<*>

    companion object Default : NbtTagResolver {
        override fun resolve(value: Any?): NbtTag<*> = when (value) {
            // do not need to resolve something that is a tag
            is NbtTag<*> -> value
            is Map<*, *> -> resolveMap(value)
            is String -> NbtStringTag(value)
            is Byte -> NbtByteTag(value)
            is Int -> NbtIntTag(value)
            is Long -> NbtLongTag(value)
            is Float -> NbtFloatTag(value)
            is Double -> NbtDoubleTag(value)
            is ByteArray -> NbtByteArrayTag(value)
            is IntArray -> NbtIntArrayTag(value)
            is LongArray -> NbtLongArrayTag(value)
            is Short -> NbtShortTag(value)
            is List<*> -> NbtListTag().apply { value.forEach { add(it) } }
            null -> throw IllegalArgumentException("Cannot resolve null value")
            else -> throw IllegalArgumentException("Unknown NBT value type ${value::class}")
        }

        private fun resolveMap(mapValue: Map<*, *>): NbtCompound {
            if (mapValue.isEmpty()) return NbtCompound()
            val (keySample, _) = mapValue.entries.first()
            if (keySample !is String) throw IllegalArgumentException("Map keys must be String")

            return NbtCompound(this, *(mapValue.entries.map { it.key as String to it.value }.toTypedArray()))
        }
    }
}
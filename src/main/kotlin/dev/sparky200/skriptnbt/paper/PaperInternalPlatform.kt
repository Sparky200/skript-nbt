package dev.sparky200.skriptnbt.paper

import dev.sparky200.skriptnbt.InternalPlatform
import dev.sparky200.skriptnbt.reflect.MappingCollection
import org.bukkit.entity.Entity

class PaperInternalPlatform(override val mappings: MappingCollection) : InternalPlatform {
    override fun nbtCompoundTag(): Any = mappings.nbtCompoundTagClass()
    override fun nbtListTag(): Any = mappings.nbtListTagClass()
    override fun nbtByteTag(byte: Byte) = mappings.nbtByteTagClass(byte)
    override fun nbtShortTag(short: Short) = mappings.nbtShortTagClass(short)
    override fun nbtIntTag(int: Int) = mappings.nbtIntTagClass(int)
    override fun nbtLongTag(long: Long) = mappings.nbtLongTagClass(long)
    override fun nbtFloatTag(float: Float) = mappings.nbtFloatTagClass(float)
    override fun nbtDoubleTag(double: Double) = mappings.nbtDoubleTagClass(double)
    override fun nbtStringTag(string: String) = mappings.nbtStringTagClass(string)
    override fun nbtByteArrayTag(byteArray: ByteArray) = mappings.nbtByteArrayTagClass(byteArray)
    override fun nbtIntArrayTag(intArray: IntArray) = mappings.nbtIntArrayTagClass(intArray)
    override fun nbtLongArrayTag(longArray: LongArray) = mappings.nbtLongArrayTagClass(longArray)

    override fun nbtCompoundSet(compound: Any, key: String, value: Any) =
        mappings.nbtCompoundSetFunction(compound, key, value)

    @Suppress("UNCHECKED_CAST")
    override fun nbtCompoundGetKeys(compound: Any): Collection<String> =
        mappings.nbtCompoundGetKeysFunction(compound) as Collection<String>

    override fun nbtCompoundGet(compound: Any, key: String): Any =
        mappings.nbtCompoundGetFunction(compound, key)

    override fun nbtListSize(list: Any): Int =
        mappings.nbtListSizeFunction(list) as Int

    override fun nbtListGet(compound: Any, index: Int): Any =
        mappings.nbtListGetFunction(compound, index)

    override fun nbtListAdd(compound: Any, index: Int, value: Any) =
        mappings.nbtListAddFunction(compound, index, value) as? Unit ?: Unit

    override fun nbtByteTagGet(tag: Any): Byte =
        mappings.nbtByteTagGetFunction(tag) as Byte

    override fun nbtShortTagGet(tag: Any): Short =
        mappings.nbtShortTagGetFunction(tag) as Short

    override fun nbtIntTagGet(tag: Any): Int =
        mappings.nbtIntTagGetFunction(tag) as Int

    override fun nbtLongTagGet(tag: Any): Long =
        mappings.nbtLongTagGetFunction(tag) as Long

    override fun nbtFloatTagGet(tag: Any): Float =
        mappings.nbtFloatTagGetFunction(tag) as Float

    override fun nbtDoubleTagGet(tag: Any): Double =
        mappings.nbtDoubleTagGetFunction(tag) as Double

    override fun nbtStringTagGet(tag: Any): String =
        mappings.nbtStringTagGetFunction(tag) as String

    override fun nbtByteArrayTagGet(tag: Any): ByteArray =
        mappings.nbtByteArrayTagGetFunction(tag) as ByteArray

    override fun nbtIntArrayTagGet(tag: Any): IntArray =
        mappings.nbtIntArrayTagGetFunction(tag) as IntArray

    override fun nbtLongArrayTagGet(tag: Any): LongArray =
        mappings.nbtLongArrayTagGetFunction(tag) as LongArray

    override fun cbEntityGetHandle(bukkitEntity: Entity): Any =
        mappings.cbEntityGetHandleFunction(bukkitEntity)

    override fun nmsEntitySave(entity: Any, compound: Any): Any =
        mappings.nmsEntitySaveFunction(entity, compound)

    override fun nmsEntityLoad(entity: Any, compound: Any): Any =
        mappings.nmsEntityLoadFunction(entity, compound)
}

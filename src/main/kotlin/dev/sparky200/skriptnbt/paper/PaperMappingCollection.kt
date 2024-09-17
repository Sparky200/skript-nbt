package dev.sparky200.skriptnbt.paper

import dev.sparky200.skriptnbt.reflect.FunctionDef
import dev.sparky200.skriptnbt.reflect.MappingCollection
import dev.sparky200.skriptnbt.reflect.loadClass

class PaperMappingCollection(private val classLoader: ClassLoader) : MappingCollection {
    private fun load(name: String) = loadClass(classLoader, name)

    private fun nbt(name: String) = load("net.minecraft.nbt.$name")
    private fun entity(name: String) = load("net.minecraft.world.entity.$name")
    private fun cbEntity(name: String) = load("org.bukkit.craftbukkit.entity.$name")

    override val nbtTagClass = nbt("Tag")

    override val nbtCompoundTagClass = nbt("CompoundTag")
    override val nbtListTagClass = nbt("ListTag")

    override val nbtNumericTagClass = nbt("NumericTag")
    override val nbtByteTagClass = nbt("ByteTag")
    override val nbtShortTagClass = nbt("ShortTag")
    override val nbtIntTagClass = nbt("IntTag")
    override val nbtLongTagClass = nbt("LongTag")
    override val nbtFloatTagClass = nbt("FloatTag")
    override val nbtDoubleTagClass = nbt("DoubleTag")

    override val nbtStringTagClass = nbt("StringTag")

    override val nbtByteArrayTagClass = nbt("ByteArrayTag")
    override val nbtIntArrayTagClass = nbt("IntArrayTag")
    override val nbtLongArrayTagClass = nbt("LongArrayTag")

    override val nmsEntityClass = entity("Entity")
    override val cbEntityClass = cbEntity("CraftEntity")

    override val nbtCompoundSetFunction: FunctionDef<Any, Unit> =
        nbtCompoundTagClass.function("put", Unit::class.java, String::class.java, nbtTagClass.cl)

    override val nbtCompoundGetKeysFunction: FunctionDef<Any, Any> =
        nbtCompoundTagClass.function("getAllKeys", Collection::class.java)

    override val nbtCompoundGetFunction: FunctionDef<Any, Any> =
        nbtCompoundTagClass.function("get", nbtTagClass.cl, String::class.java)

    override val nbtListSizeFunction: FunctionDef<Any, Any> =
        nbtListTagClass.function("size", Int::class.java)

    override val nbtListGetFunction: FunctionDef<Any, Any> =
        nbtListTagClass.function("get", nbtTagClass.cl, Int::class.java)

    override val nbtListAddFunction: FunctionDef<Any, Any> =
        nbtListTagClass.function("add", Unit::class.java, Int::class.java, nbtTagClass.cl)

    override val nbtByteTagGetFunction: FunctionDef<Any, Any> =
        nbtNumericTagClass.function("getAsByte", Byte::class.java)

    override val nbtShortTagGetFunction: FunctionDef<Any, Any> =
        nbtNumericTagClass.function("getAsShort", Short::class.java)

    override val nbtIntTagGetFunction: FunctionDef<Any, Any> =
        nbtNumericTagClass.function("getAsInt", Int::class.java)

    override val nbtLongTagGetFunction: FunctionDef<Any, Any> =
        nbtNumericTagClass.function("getAsLong", Long::class.java)

    override val nbtFloatTagGetFunction: FunctionDef<Any, Any> =
        nbtNumericTagClass.function("getAsFloat", Float::class.java)

    override val nbtDoubleTagGetFunction: FunctionDef<Any, Any> =
        nbtNumericTagClass.function("getAsDouble", Double::class.java)

    override val nbtStringTagGetFunction: FunctionDef<Any, Any> =
        nbtStringTagClass.function("getAsString", String::class.java)

    override val nbtByteArrayTagGetFunction: FunctionDef<Any, Any> =
        nbtByteArrayTagClass.function("getAsByteArray", ByteArray::class.java)

    override val nbtIntArrayTagGetFunction: FunctionDef<Any, Any> =
        nbtIntArrayTagClass.function("getAsIntArray", IntArray::class.java)

    override val nbtLongArrayTagGetFunction: FunctionDef<Any, Any> =
        nbtLongArrayTagClass.function("getAsLongArray", LongArray::class.java)

    override val cbEntityGetHandleFunction: FunctionDef<Any, Any> =
        cbEntityClass.function("getHandle", nmsEntityClass.cl)

    override val nmsEntitySaveFunction: FunctionDef<Any, Any> =
        nmsEntityClass.function("saveWithoutId", cbEntityClass.cl, nbtCompoundTagClass.cl)

    override val nmsEntityLoadFunction: FunctionDef<Any, Any> =
        nmsEntityClass.function("load", cbEntityClass.cl, nbtCompoundTagClass.cl)
}

package dev.sparky200.skriptnbt

import dev.sparky200.skriptnbt.reflect.MappingCollection
import org.bukkit.entity.Entity
import org.jetbrains.annotations.ApiStatus.Internal

/**
 * Internal layer for accessing reflective values using
 * a mappings collection.
 *
 * Many return types and function references lack strong
 * typing at compile-time. Please refer to the documentation
 * attached to members for guaranteed contracts.
 *
 * The Real signatures are defined on each function,
 * which are based on mojang mappings.
 */
@Internal
interface InternalPlatform {
    @get:Internal
    val mappings: MappingCollection

    /**
     * Real signature:
     * ```kt
     * nbtCompoundTag(): CompoundTag
     * ```
     */
    @Internal
    fun nbtCompoundTag(): Any

    /**
     * Real signature:
     * ```kt
     * nbtListTag(): ListTag
     * ```
     */
    @Internal
    fun nbtListTag(): Any

    /**
     * Real signature:
     * ```kt
     * nbtByteTag(byte: Byte): ByteTag
     * ```
     */
    @Internal
    fun nbtByteTag(byte: Byte): Any

    /**
     * Real signature:
     * ```kt
     * nbtShortTag(short: Short): ShortTag
     * ```
     */
    @Internal
    fun nbtShortTag(short: Short): Any

    /**
     * Real signature:
     * ```kt
     * nbtIntTag(int: Int): IntTag
     * ```
     */
    @Internal
    fun nbtIntTag(int: Int): Any

    /**
     * Real signature:
     * ```kt
     * nbtLongTag(long: Long): LongTag
     * ```
     */
    @Internal
    fun nbtLongTag(long: Long): Any

    /**
     * Real signature:
     * ```kt
     * nbtFloatTag(float: Float): FloatTag
     * ```
     */

    fun nbtFloatTag(float: Float): Any

    /**
     * Real signature:
     * ```kt
     * nbtDoubleTag(double: Double): DoubleTag
     * ```
     */
    @Internal
    fun nbtDoubleTag(double: Double): Any

    /**
     * Real signature:
     * ```kt
     * nbtStringTag(string: String): StringTag
     * ```
     */
    @Internal
    fun nbtStringTag(string: String): Any

    /**
     * Real signature:
     * ```kt
     * nbtByteArrayTag(byteArray: ByteArray): ByteArrayTag
     * ```
     */
    @Internal
    fun nbtByteArrayTag(byteArray: ByteArray): Any

    /**
     * Real signature:
     * ```kt
     * nbtIntArrayTag(intArray: IntArray): IntArrayTag
     * ```
     */
    @Internal
    fun nbtIntArrayTag(intArray: IntArray): Any

    /**
     * Real signature:
     * ```kt
     * nbtLongArrayTag(longArray: LongArray): LongArrayTag
     * ```
     */
    @Internal
    fun nbtLongArrayTag(longArray: LongArray): Any

    /**
     * Real signature:
     * ```kt
     * nbtCompoundSet(compound: CompoundTag, key: String, value: Tag<*>)
     * ```
     */
    @Internal
    fun nbtCompoundSet(compound: Any, key: String, value: Any)

    /**
     * Real signature:
     * ```kt
     * nbtCompoundGetKeys(compound: CompoundTag): Collection<String>
     * ```
     */
    @Internal
    fun nbtCompoundGetKeys(compound: Any): Collection<String>

    /**
     * Real signature:
     * ```kt
     * nbtCompoundGet(compound: CompoundTag, key: String): Tag<*>
     * ```
     */
    @Internal
    fun nbtCompoundGet(compound: Any, key: String): Any

    /**
     * Real signature:
     * ```kt
     * nbtListSize(list: ListTag): Int
     * ```
     */
    @Internal
    fun nbtListSize(list: Any): Int

    /**
     * Real signature:
     * ```kt
     * nbtListGet(list: ListTag, index: Int): Tag<*>
     * ```
     */
    @Internal
    fun nbtListGet(compound: Any, index: Int): Any

    /**
     * Real signature:
     * ```kt
     * nbtListAdd(list: ListTag, index: Int, value: Tag<*>)
     * ```
     */
    @Internal
    fun nbtListAdd(compound: Any, index: Int, value: Any)

    /**
     * Real signature:
     * ```kt
     * nbtByteTagGet(tag: NumericTag): Byte
     * ```
     */
    @Internal
    fun nbtByteTagGet(tag: Any): Byte

    /**
     * Real signature:
     * ```kt
     * nbtShortTagGet(tag: NumericTag): Short
     * ```
     */
    @Internal
    fun nbtShortTagGet(tag: Any): Short

    /**
     * Real signature:
     * ```kt
     * nbtIntTagGet(tag: NumericTag): Int
     * ```
     */
    @Internal
    fun nbtIntTagGet(tag: Any): Int

    /**
     * Real signature:
     * ```kt
     * nbtLongTagGet(tag: NumericTag): Long
     * ```
     */
    @Internal
    fun nbtLongTagGet(tag: Any): Long

    /**
     * Real signature:
     * ```kt
     * nbtFloatTagGet(tag: NumericTag): Float
     * ```
     */
    @Internal
    fun nbtFloatTagGet(tag: Any): Float

    /**
     * Real signature:
     * ```kt
     * nbtDoubleTagGet(tag: NumericTag): Double
     * ```
     */
    @Internal
    fun nbtDoubleTagGet(tag: Any): Double

    /**
     * Real signature:
     * ```kt
     * nbtStringTagGet(tag: StringTag): String
     * ```
     */
    @Internal
    fun nbtStringTagGet(tag: Any): String

    /**
     * Real signature:
     * ```kt
     * nbtByteArrayTagGet(tag: ByteArrayTag): ByteArray
     * ```
     */
    @Internal
    fun nbtByteArrayTagGet(tag: Any): ByteArray

    /**
     * Real signature:
     * ```kt
     * nbtIntArrayTagGet(tag: IntArrayTag): IntArray
     * ```
     */
    @Internal
    fun nbtIntArrayTagGet(tag: Any): IntArray

    /**
     * Real signature:
     * ```kt
     * nbtLongArrayTagGet(tag: LongArrayTag): Any
     * ```
     */
    @Internal
    fun nbtLongArrayTagGet(tag: Any): LongArray

    /**
     * Real signature:
     * ```kt
     * cbEntityGetHandle(bukkitEntity: CraftEntity): nms.Entity
     * ```
     */
    @Internal
    fun cbEntityGetHandle(bukkitEntity: Entity): Any

    /**
     * Real signature:
     * ```kt
     * nmsEntitySave(entity: nms.Entity, compound: CompoundTag)
     * ```
     */
    @Internal
    fun nmsEntitySave(entity: Any, compound: Any): Any

    /**
     * Real signature:
     * ```kt
     * nmsEntityLoad(entity: nms.Entity, compound: CompoundTag)
     * ```
     */
    @Internal
    fun nmsEntityLoad(entity: Any, compound: Any): Any
}
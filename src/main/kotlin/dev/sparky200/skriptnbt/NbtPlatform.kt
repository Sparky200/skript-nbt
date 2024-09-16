package dev.sparky200.skriptnbt

import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtTag
import org.bukkit.entity.Entity
import org.jetbrains.annotations.ApiStatus
import java.nio.file.Path

/**
 * A class designed around allowing multiple platforms to provide bindings.
 *
 * Intended usage:
 *
 * ```kt
 * with (SkriptNbt.platform) {
 *     val compound = entity.getNbt()
 *     compound["my_key"] = "my value"
 *     entity.setNbt(compound)
 * }
 * ```
 *
 * @author Sparky200
 */
interface NbtPlatform {
    @get:ApiStatus.Internal
    val internal: InternalPlatform

    fun Entity.getNbt(): NbtCompound
    fun Entity.setNbt(compound: NbtCompound)

    fun nbtToFile(path: Path, compound: NbtCompound)
    fun nbtFromFile(path: Path): NbtCompound

    fun nbtToString(compound: NbtCompound): String
    fun nbtFromString(string: String): NbtCompound

    fun nbtTagOf(any: Any?): NbtTag =
        nbtTagOfOrNull(any) ?: throw IllegalArgumentException("Invalid type: ${any?.javaClass?.name}")

    fun nbtTagOfOrNull(any: Any?): NbtTag?

    fun nbtValueOf(tag: NbtTag?): Any?

    companion object {
        @JvmStatic
        val Uninitialized = object : NbtPlatform {
            private val error = NotImplementedError("skript-nbt has not been initialized!")

            override val internal: InternalPlatform
                get() = throw error

            override fun nbtFromFile(path: Path) = throw error
            override fun nbtToFile(path: Path, compound: NbtCompound) = throw error

            override fun nbtFromString(string: String) = throw error
            override fun nbtToString(compound: NbtCompound) = throw error

            override fun Entity.getNbt(): Nothing = throw error
            override fun Entity.setNbt(compound: NbtCompound) = throw error

            override fun nbtTagOfOrNull(any: Any?) = throw error

            override fun nbtValueOf(tag: NbtTag?) = throw error

        }
    }
}

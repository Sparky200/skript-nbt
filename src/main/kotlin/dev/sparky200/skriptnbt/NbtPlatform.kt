package dev.sparky200.skriptnbt

import dev.sparky200.skriptnbt.nbt.NbtCompound
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

    fun nbtFromFile(path: Path): NbtCompound

    companion object {
        @JvmStatic
        val Uninitialized = object : NbtPlatform {
            private val error = NotImplementedError("skript-nbt has not been initialized!")

            override val internal: InternalPlatform
                get() = throw error

            override fun nbtFromFile(path: Path) = throw error

            override fun Entity.getNbt(): Nothing = throw error
            override fun Entity.setNbt(compound: NbtCompound) = throw error

        }
    }
}
package dev.sparky200.skriptnbt.paper

import dev.sparky200.skriptnbt.InternalPlatform
import dev.sparky200.skriptnbt.NbtPlatform
import dev.sparky200.skriptnbt.SkriptNbt
import dev.sparky200.skriptnbt.skript.SkriptHook
import dev.sparky200.skriptnbt.skript.types.MutableCompound
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToString
import net.benwoodworth.knbt.Nbt
import net.benwoodworth.knbt.NbtByte
import net.benwoodworth.knbt.NbtByteArray
import net.benwoodworth.knbt.NbtCompound
import net.benwoodworth.knbt.NbtCompression
import net.benwoodworth.knbt.NbtDouble
import net.benwoodworth.knbt.NbtFloat
import net.benwoodworth.knbt.NbtInt
import net.benwoodworth.knbt.NbtIntArray
import net.benwoodworth.knbt.NbtList
import net.benwoodworth.knbt.NbtLong
import net.benwoodworth.knbt.NbtLongArray
import net.benwoodworth.knbt.NbtShort
import net.benwoodworth.knbt.NbtString
import net.benwoodworth.knbt.NbtTag
import net.benwoodworth.knbt.NbtVariant
import net.benwoodworth.knbt.StringifiedNbt
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.notExists
import kotlin.io.path.readBytes
import kotlin.io.path.writeBytes

// Suppress Unused: Entrypoint
@Suppress("Unused")
class PaperHook : JavaPlugin() {
    override fun onEnable() {
        val platform = try {
            PaperPlatform()
        } catch (e: Exception) {
            logger.severe("Failed to create platform")
            logger.severe(e.stackTraceToString())
            pluginLoader.disablePlugin(this)
            return
        }
        SkriptNbt.enable(platform)
        with (SkriptHook) { hook() }
    }

    override fun onDisable() {
        if (!SkriptNbt.initialized) return
        SkriptNbt.disable()
    }
}

class PaperPlatform : NbtPlatform {
    private val nbt = Nbt {
        variant = NbtVariant.Java
        compression = NbtCompression.None
    }
    private val sNbt = StringifiedNbt { }

    override val internal: InternalPlatform
        get() = PaperInternalPlatform(PaperMappingCollection(javaClass.classLoader))

    private fun nmsToInternalTag(tag: Any): NbtTag = when (tag.javaClass) {
        internal.mappings.nbtCompoundTagClass.cl -> run {
            val keys = internal.nbtCompoundGetKeys(tag)
            val map = mutableMapOf<String, NbtTag>()

            for (key in keys) {
                val nmsTag = internal.nbtCompoundGet(tag, key)
                map[key] = nmsToInternalTag(nmsTag)
            }

            NbtCompound(map)
        }
        internal.mappings.nbtByteTagClass.cl -> NbtByte(internal.nbtByteTagGet(tag))
        internal.mappings.nbtFloatTagClass.cl -> NbtFloat(internal.nbtFloatTagGet(tag))
        internal.mappings.nbtDoubleTagClass.cl -> NbtDouble(internal.nbtDoubleTagGet(tag))
        internal.mappings.nbtIntTagClass.cl -> NbtInt(internal.nbtIntTagGet(tag))
        internal.mappings.nbtShortTagClass.cl -> NbtShort(internal.nbtShortTagGet(tag))
        internal.mappings.nbtLongTagClass.cl -> NbtLong(internal.nbtLongTagGet(tag))
        internal.mappings.nbtListTagClass.cl -> run {
            val size = internal.nbtListSize(tag)
            val list = mutableListOf<NbtTag>()

            for (i in 0 until size) {
                list.add(nmsToInternalTag(internal.nbtListGet(tag, i)))
            }

            nbtList(list)
        }
        internal.mappings.nbtByteArrayTagClass.cl -> NbtByteArray(internal.nbtByteArrayTagGet(tag))
        internal.mappings.nbtIntArrayTagClass.cl -> NbtIntArray(internal.nbtIntArrayTagGet(tag))
        internal.mappings.nbtLongArrayTagClass.cl -> NbtLongArray(internal.nbtLongArrayTagGet(tag))
        internal.mappings.nbtStringTagClass.cl -> NbtString(internal.nbtStringTagGet(tag))
        else -> throw IllegalArgumentException("Unsupported type: ${tag.javaClass.name}")
    }

    private fun internalToNmsTag(tag: NbtTag): Any = when (tag) {
        is NbtCompound -> internal.nbtCompoundTag().apply {
            for (key in tag.keys) {
                internal.nbtCompoundSet(this, key, internalToNmsTag(tag[key]!!))
            }
        }
        is NbtByte -> internal.nbtByteTag(tag.value)
        is NbtFloat -> internal.nbtFloatTag(tag.value)
        is NbtDouble -> internal.nbtDoubleTag(tag.value)
        is NbtInt -> internal.nbtIntTag(tag.value)
        is NbtShort -> internal.nbtShortTag(tag.value)
        is NbtLong -> internal.nbtLongTag(tag.value)
        is NbtList<*> -> internal.nbtListTag().apply {
            for (v in tag) {
                internal.nbtListAdd(this, internal.nbtListSize(this), internalToNmsTag(v))
            }
        }
        is NbtByteArray -> internal.nbtByteArrayTag(tag.toByteArray())
        is NbtIntArray -> internal.nbtIntArrayTag(tag.toIntArray())
        is NbtLongArray -> internal.nbtLongArrayTag(tag.toLongArray())
        is NbtString -> internal.nbtStringTag(tag.value)
        else -> throw IllegalArgumentException("Unsupported type: ${tag.javaClass.name}")
    }

    override fun Entity.getNbt(): NbtCompound {
        val nmsCompound = internal.nbtCompoundTag()

        internal.nmsEntitySave(internal.cbEntityGetHandle(this), nmsCompound)

        return nmsToInternalTag(nmsCompound) as NbtCompound
    }

    override fun Entity.setNbt(compound: NbtCompound) {
        val nmsCompound = internalToNmsTag(compound)

        internal.nmsEntityLoad(internal.cbEntityGetHandle(this), nmsCompound)
    }

    override fun nbtFromFile(path: Path): NbtCompound {
        val bytes = path.readBytes()
        return nbt.decodeFromByteArray(bytes)
    }

    override fun nbtToFile(path: Path, compound: NbtCompound) {
        if (path.notExists()) path.createFile()
        val bytes = nbt.encodeToByteArray(compound)
        path.writeBytes(bytes)
    }

    override fun nbtFromString(s: String): NbtCompound {
        return sNbt.decodeFromString<NbtCompound>(s)
    }

    override fun nbtToString(compound: NbtCompound): String {
        return sNbt.encodeToString<NbtCompound>(compound)
    }

    private fun nbtList(list: List<*>) = NbtList::class.java
        .getDeclaredConstructor(List::class.java)
        .apply { isAccessible = true }
        .newInstance(list) as NbtList<NbtTag>

    override fun nbtTagOfOrNull(any: Any?): NbtTag? = when (any) {
        is NbtTag -> any
        is Byte -> NbtByte(any)
        is Short -> NbtShort(any)
        is Int -> NbtInt(any)
        is Long -> NbtLong(any)
        is Float -> NbtFloat(any)
        is Double -> NbtDouble(any)
        is String -> NbtString(any)
        is ByteArray -> NbtByteArray(any)
        is IntArray -> NbtIntArray(any)
        is LongArray -> NbtLongArray(any)
        is Collection<*> -> nbtList(any.map { nbtTagOf(it) })
        is Array<*> -> nbtList(any.map { nbtTagOf(it) })
        is MutableCompound -> nbtTagOfOrNull(any.map)
        is Map<*, *> -> {
            if (any.isEmpty()) NbtCompound(mapOf())
            else {
                if (any.any { it.key !is String }) throw IllegalArgumentException("Maps must have string keys")
                NbtCompound(any.toList().associate { it.first!! as String to nbtTagOf(it.second!!) })
            }
        }
        null -> NbtByte(0)
        else -> throw IllegalArgumentException("Unsupported type: ${any.javaClass.name}")
    }

    override fun nbtValueOf(tag: NbtTag?): Any? = when (tag) {
        is NbtByte -> tag.value
        is NbtInt -> tag.value
        is NbtShort -> tag.value
        is NbtLong -> tag.value
        is NbtFloat -> tag.value
        is NbtDouble -> tag.value
        is NbtString -> tag.value
        is NbtByteArray -> tag.toByteArray()
        is NbtIntArray -> tag.toIntArray()
        is NbtLongArray -> tag.toLongArray()
        is NbtCompound -> tag.toList().associate { it.first to nbtValueOf(it.second) }
        is NbtList<*> -> tag.toList()
        null -> null
        else -> throw IllegalArgumentException("Unsupported type: ${tag.javaClass.name}")
    }
}

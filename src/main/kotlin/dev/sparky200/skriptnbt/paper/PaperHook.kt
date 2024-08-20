package dev.sparky200.skriptnbt.paper

import dev.sparky200.skriptnbt.InternalPlatform
import dev.sparky200.skriptnbt.NbtPlatform
import dev.sparky200.skriptnbt.SkriptNbt
import dev.sparky200.skriptnbt.nbt.*
import dev.sparky200.skriptnbt.skript.SkriptHook
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path

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
    override val internal: InternalPlatform
        get() = PaperInternalPlatform(PaperMappingCollection(javaClass.classLoader))

    private fun nmsToInternalTag(tag: Any): NbtTag<*> = when (tag.javaClass) {
        internal.mappings.nbtCompoundTagClass.cl -> NbtCompound().apply {
            val keys = internal.nbtCompoundGetKeys(tag)

            for (key in keys) {
                val nmsTag = internal.nbtCompoundGet(tag, key)
                setTag(key, nmsToInternalTag(nmsTag))
            }
        }
        internal.mappings.nbtByteTagClass.cl -> NbtByteTag(internal.nbtByteTagGet(tag))
        internal.mappings.nbtFloatTagClass.cl -> NbtFloatTag(internal.nbtFloatTagGet(tag))
        internal.mappings.nbtDoubleTagClass.cl -> NbtDoubleTag(internal.nbtDoubleTagGet(tag))
        internal.mappings.nbtIntTagClass.cl -> NbtIntTag(internal.nbtIntTagGet(tag))
        internal.mappings.nbtShortTagClass.cl -> NbtShortTag(internal.nbtShortTagGet(tag))
        internal.mappings.nbtLongTagClass.cl -> NbtLongTag(internal.nbtLongTagGet(tag))
        internal.mappings.nbtListTagClass.cl -> NbtListTag().apply {
            val size = internal.nbtListSize(tag)
            for (i in 0 until size) {
                addTag(nmsToInternalTag(internal.nbtListGet(tag, i)))
            }
        }
        internal.mappings.nbtByteArrayTagClass.cl -> NbtByteArrayTag(internal.nbtByteArrayTagGet(tag))
        internal.mappings.nbtIntArrayTagClass.cl -> NbtIntArrayTag(internal.nbtIntArrayTagGet(tag))
        internal.mappings.nbtLongArrayTagClass.cl -> NbtLongArrayTag(internal.nbtLongArrayTagGet(tag))
        internal.mappings.nbtStringTagClass.cl -> NbtStringTag(internal.nbtStringTagGet(tag))
        else -> throw IllegalArgumentException("Unsupported type: ${tag.javaClass.name}")
    }

    private fun internalToNmsTag(tag: NbtTag<*>): Any = when (tag) {
        is NbtCompound -> internal.nbtCompoundTag().apply {
            for (key in tag.keys) {
                internal.nbtCompoundSet(this, key, internalToNmsTag(tag.getAsTag(key)!!))
            }
        }
        is NbtByteTag -> internal.nbtByteTag(tag.value)
        is NbtFloatTag -> internal.nbtFloatTag(tag.value)
        is NbtDoubleTag -> internal.nbtDoubleTag(tag.value)
        is NbtIntTag -> internal.nbtIntTag(tag.value)
        is NbtShortTag -> internal.nbtShortTag(tag.value)
        is NbtLongTag -> internal.nbtLongTag(tag.value)
        is NbtListTag -> internal.nbtListTag().apply {
            for (v in tag.tags) {
                internal.nbtListAdd(this, internal.nbtListSize(this), internalToNmsTag(v))
            }
        }
        is NbtByteArrayTag -> internal.nbtByteArrayTag(tag.value)
        is NbtIntArrayTag -> internal.nbtIntArrayTag(tag.value)
        is NbtLongArrayTag -> internal.nbtLongArrayTag(tag.value)
        is NbtStringTag -> internal.nbtStringTag(tag.value)
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
        TODO("Not yet implemented")
    }
}
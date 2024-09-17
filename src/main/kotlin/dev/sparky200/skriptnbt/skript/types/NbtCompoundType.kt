package dev.sparky200.skriptnbt.skript.types

import ch.njol.skript.classes.Changer
import ch.njol.skript.classes.Parser
import ch.njol.skript.classes.Serializer
import ch.njol.skript.lang.ParseContext
import ch.njol.yggdrasil.Fields
import dev.sparky200.skriptnbt.SkriptNbt
import dev.sparky200.skriptnbt.skript.registerClass
import kotlinx.serialization.decodeFromString
import net.benwoodworth.knbt.StringifiedNbt

class MutableCompound(val map: MutableMap<String, Any?>)

fun MutableMap<String, Any?>.wrap() = MutableCompound(this)

object NbtCompoundType {
    init {
        registerClass<MutableCompound>("nbtcompound") {
            user("nbt ?compound")
            name("NBT Compound")
            description(
                "An NBT Compound is a type holding values associated by text keys.",
                "For example, '\"coins\" of nbt compound {_myCompound}' can be associated",
                "with a number value, and that syntax would return the amount of coins",
                "stored in the compound."
            )
            examples("TODO")
            since("1.0.0")
            parser(object : Parser<MutableCompound>() {
                override fun canParse(context: ParseContext?) = false

                override fun toString(o: MutableCompound, flags: Int): String = with (SkriptNbt.platform) {
                    nbtTagOf(o).toString()
                }

                override fun toVariableNameString(o: MutableCompound): String = with (SkriptNbt.platform) {
                    "nbt:${nbtTagOf(o)}"
                }
            })
            serializer(object : Serializer<MutableCompound>() {
                override fun serialize(o: MutableCompound): Fields {
                    return Fields().apply { putObject("nbt", o.toString()) }
                }

                override fun canBeInstantiated() = false

                override fun deserialize(o: MutableCompound?, f: Fields?) {
                    throw NotImplementedError("invalid deserialization entrypoint")
                }

                override fun mustSyncDeserialization() = true

                override fun deserialize(fields: Fields): MutableCompound {
                    return StringifiedNbt.decodeFromString(
                        fields.getObject("nbt", String::class.java)
                            ?: throw IllegalStateException("No NBT string in fields")
                    )
                }
            })
            changer(object : Changer<MutableCompound> {
                override fun acceptChange(mode: Changer.ChangeMode): Array<Class<*>>? {
                    return when (mode) {
                        Changer.ChangeMode.ADD,
                        Changer.ChangeMode.SET -> arrayOf(MutableCompound::class.java)
                        Changer.ChangeMode.REMOVE -> arrayOf(String::class.java)
                        // Array and collection must contain keys
                        Changer.ChangeMode.REMOVE_ALL -> arrayOf(MutableCompound::class.java, Array::class.java, Collection::class.java)
                        Changer.ChangeMode.RESET,
                        Changer.ChangeMode.DELETE -> arrayOf()
                        else -> null
                    }
                }

                override fun change(
                    what: Array<out MutableCompound>,
                    delta: Array<out Any?>?,
                    mode: Changer.ChangeMode
                ) {
                    for (compound in what) {
                        when (mode) {
                            Changer.ChangeMode.ADD,
                            Changer.ChangeMode.SET -> {
                                if (delta == null)
                                    throw IllegalArgumentException(
                                        "delta must not be null for add or set change modes"
                                    )

                                for (change in delta) {
                                    if (mode == Changer.ChangeMode.SET) compound.map.clear()
                                    if (change !is Map<*, *>) continue
                                    for ((key, value) in change) {
                                        if (key !is String) continue
                                        compound.map[key] = value
                                    }
                                }
                            }
                            Changer.ChangeMode.REMOVE,
                            Changer.ChangeMode.REMOVE_ALL -> {
                                if (delta == null)
                                    throw IllegalArgumentException(
                                        "delta must not be null for remove change modes"
                                    )
                                for (change in delta) {
                                    @Suppress("UNCHECKED_CAST")
                                    val keys = when (change) {
                                        is String -> listOf(change)
                                        is Collection<*> -> {
                                            if (change.isNotEmpty() && change.first() !is String)
                                                throw IllegalArgumentException("delta must contain strings")
                                            else change as Collection<String>
                                        }
                                        is Array<*> -> {
                                            if (change.isNotEmpty() && change[0] !is String)
                                                throw IllegalArgumentException("delta must contain strings")
                                            else (change as Array<String>).toList()
                                        }
                                        else -> throw IllegalArgumentException(
                                            "delta must be string, collection of strings, or array of strings"
                                        )
                                    }
                                    for (key in keys) compound.map.remove(key)
                                }
                            }
                            Changer.ChangeMode.RESET -> compound.map.clear()
                            else -> throw IllegalArgumentException("mode ${mode.name} not supported")
                        }
                    }
                }

            })
        }
    }
}

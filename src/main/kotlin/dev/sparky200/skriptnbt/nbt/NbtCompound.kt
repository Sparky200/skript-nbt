package dev.sparky200.skriptnbt.nbt

import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
class NbtCompound(val resolver: NbtTagResolver = NbtTagResolver.Default) : NbtTag<Map<String, Any?>> {
    private val map = mutableMapOf<String, NbtTag<*>>()

    override val value: Map<String, Any?>
        get() = map.entries.associate { (key, value) -> key to value.value }

    val keys: Collection<String>
        get() = map.keys

    // TODO: type it!
    operator fun set(key: String, value: Any?) {
        map[key] = resolver.resolve(value)
    }

    fun setTag(key: String, tag: NbtTag<*>) {
        map[key] = tag
    }

    operator fun get(key: String): Any? = map[key]?.value
    fun getAsTag(key: String): NbtTag<*>? = map[key]

    inline fun <reified T> getAs(key: String): T? {
        val tag = getAsTag(key)
        if (tag is T) return tag
        if (tag != null && tag.value is T) return tag.value as T
        return null
    }

    fun clear() {
        map.clear()
    }

    fun remove(key: String): Any? {
        return map.remove(key)?.value
    }

    operator fun contains(key: String): Boolean = key in map

    override fun toNbtString(): String = map.entries.joinToString(separator = ",", prefix = "{", postfix = "}") { (key, value) -> "$key=${value.toNbtString()}" }

    override fun toString(): String {
        return "NbtCompound{${map.entries.joinToString { "\"${it.key}\"=\"${it.value}\"" }}}"
    }
}

fun NbtCompound(resolver: NbtTagResolver, vararg elements: Pair<String, Any?>): NbtCompound {
    val compound = NbtCompound(resolver)
    elements.forEach { (key, value) ->
        compound[key] = value
    }
    return compound
}

fun NbtCompound(vararg elements: Pair<String, Any?>): NbtCompound = NbtCompound(NbtTagResolver.Default, *elements)

private fun parseArrayOrListTag(value: String): NbtTag<*> {
    TODO()
}

private fun parseStringTag(value: String): NbtTag<*> {
    if (value.startsWith("\"")) {
        if (!value.endsWith("\"")) throw IllegalArgumentException("string literal must end with \"")
        return NbtStringTag(value.substring(1, value.length - 1))
    }
    if (value.startsWith("[")) {
        if (!value.endsWith("]")) throw IllegalArgumentException("array or list must end with ]")
        parseArrayOrListTag(value.substring(1, value.length - 1))
    }
    if (value.startsWith('{')) {
        return NbtCompound(value)
    }
    if (value.endsWith('b')) return NbtByteTag(value.trimEnd('b').toByte())
    if (value.endsWith('s')) return NbtShortTag(value.trimEnd('s').toShort())
    if (value.endsWith('l')) return NbtLongTag(value.trimEnd('l').toLong())
    if (value.endsWith('f')) return NbtFloatTag(value.trimEnd('f').toFloat())
    if (value.endsWith('d')) return NbtDoubleTag(value.trimEnd('d').toDouble())
    return NbtIntTag(value.toIntOrNull() ?: throw IllegalArgumentException("unable to parse value '$value'"))
}

fun NbtCompound(s: String): NbtCompound {
    if (!s.startsWith("{") || !s.endsWith("}")) throw IllegalArgumentException("compound must be enclosed with curly braces")
    val str = s.trim('{', '}')
    val split = str.split(',')
    val compound = NbtCompound()
    for (pair in split) {
        val (key, value) = pair.split(':', limit = 2)
        compound[key] = parseStringTag(value)
    }
    return compound
}

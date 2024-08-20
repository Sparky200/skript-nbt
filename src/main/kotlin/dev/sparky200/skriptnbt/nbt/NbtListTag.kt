package dev.sparky200.skriptnbt.nbt

import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
class NbtListTag(val resolver: NbtTagResolver = NbtTagResolver.Default) : NbtTag<List<Any?>> {
    private val list = mutableListOf<NbtTag<*>>()
    override val value: List<Any?>
        get() = list.map { it.value }

    val tags: List<NbtTag<*>> get() = list.toList()

    fun add(value: Any?) {
        list.add(resolver.resolve(value))
    }

    fun addTag(value: NbtTag<*>) {
        list.add(value)
    }

    operator fun set(index: Int, value: Any?) {
        list[index] = resolver.resolve(value)
    }

    fun setTag(index: Int, value: NbtTag<*>) {
        list[index] = value
    }

    operator fun get(index: Int): Any? = list[index].value
    fun getAsTag(index: Int): NbtTag<*> = list[index]

    override fun toNbtString(): String = list.joinToString(separator = ",", prefix = "[", postfix = "]") { it.toNbtString() }

    inline fun <reified T> getAs(index: Int): T {
        val tag = getAsTag(index)
        if (tag is T) return tag
        return tag.value as T
    }

    fun size() = list.size
}

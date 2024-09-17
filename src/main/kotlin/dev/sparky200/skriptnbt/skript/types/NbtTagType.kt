package dev.sparky200.skriptnbt.skript.types

import dev.sparky200.skriptnbt.skript.registerEnumClass

enum class NbtTagType {
    Byte,
    Short,
    Int,
    Long,
    Float,
    Double,
    String,
    ByteArray,
    IntArray,
    LongArray,
    List,
    Compound,

    ;

    companion object {
        init {
            registerEnumClass<NbtTagType>("tagtype", "tag types") {
                user("tag ?type")
                name("Nbt Tag Type")
                description(
                    "Represents a type of NBT Tag.",
                    "One of: byte, short, int, long, float, double,",
                    "string, byte array, int array, long array, list, compound"
                )
            }
        }

        fun enforceIs(tag: NbtTagType, value: Any?): Any? {
            if (value == null) return null
            return when (tag) {
                Byte -> value as? Byte
                Short -> value as? Short
                Int -> value as? Int
                Long -> value as? Long
                Float -> value as? Float
                Double -> value as? Double
                String -> value as? String
                ByteArray -> value as? ByteArray
                IntArray -> value as? IntArray
                LongArray -> value as? LongArray
                List -> value as? List<*>
                Compound -> value as? MutableCompound
            }
        }
    }
}
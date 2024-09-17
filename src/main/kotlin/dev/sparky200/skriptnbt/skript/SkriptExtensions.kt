package dev.sparky200.skriptnbt.skript

import ch.njol.skript.classes.ClassInfo
import ch.njol.skript.classes.EnumClassInfo
import ch.njol.skript.registrations.Classes

inline fun <reified T> registerClass(codeName: String, block: ClassInfo<T>.() -> Unit) =
    Classes.registerClass(ClassInfo(T::class.java, codeName).apply(block))

inline fun <reified T : Enum<T>> registerEnumClass(codeName: String, languageNode: String, block: EnumClassInfo<T>.() -> Unit) =
    Classes.registerClass(EnumClassInfo(T::class.java, codeName, languageNode).apply(block))
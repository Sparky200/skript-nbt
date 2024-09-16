package dev.sparky200.skriptnbt.skript

import ch.njol.skript.classes.ClassInfo
import ch.njol.skript.registrations.Classes

inline fun <reified T> registerClass(codeName: String, block: ClassInfo<T>.() -> Unit) =
    Classes.registerClass(ClassInfo(T::class.java, codeName).apply(block))

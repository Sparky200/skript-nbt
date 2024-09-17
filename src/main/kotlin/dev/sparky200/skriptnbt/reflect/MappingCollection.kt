package dev.sparky200.skriptnbt.reflect

import org.jetbrains.annotations.ApiStatus.Internal

@Internal
interface MappingCollection {
    //region Classes
    @get:Internal
    val nbtTagClass: ClassDef

    @get:Internal
    val nbtCompoundTagClass: ClassDef
    @get:Internal
    val nbtListTagClass: ClassDef

    @get:Internal
    val nbtNumericTagClass: ClassDef
    @get:Internal
    val nbtByteTagClass: ClassDef
    @get:Internal
    val nbtShortTagClass: ClassDef
    @get:Internal
    val nbtIntTagClass: ClassDef
    @get:Internal
    val nbtLongTagClass: ClassDef
    @get:Internal
    val nbtFloatTagClass: ClassDef
    @get:Internal
    val nbtDoubleTagClass: ClassDef

    @get:Internal
    val nbtStringTagClass: ClassDef

    @get:Internal
    val nbtByteArrayTagClass: ClassDef
    @get:Internal
    val nbtIntArrayTagClass: ClassDef
    @get:Internal
    val nbtLongArrayTagClass: ClassDef

    @get:Internal
    val nmsEntityClass: ClassDef
    @get:Internal
    val cbEntityClass: ClassDef
    //endregion

    //region Functions
    @get:Internal
    val nbtCompoundSetFunction: FunctionDef<Any, Unit>

    @get:Internal
    val nbtCompoundGetKeysFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtCompoundGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtListSizeFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtListGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtListAddFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtByteTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtShortTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtIntTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtLongTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtFloatTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtDoubleTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtStringTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtByteArrayTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtIntArrayTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val nbtLongArrayTagGetFunction: FunctionDef<Any, Any>

    @get:Internal
    val cbEntityGetHandleFunction: FunctionDef<Any, Any>

    @get:Internal
    val nmsEntitySaveFunction: FunctionDef<Any, Any>

    @get:Internal
    val nmsEntityLoadFunction: FunctionDef<Any, Any>

    //endregion
}

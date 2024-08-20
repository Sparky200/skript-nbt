package dev.sparky200.skriptnbt.reflect

import java.lang.reflect.Method

/**
 * Represents a reflective function definition.
 */
class FunctionDef<ReceiverNullability, ReturnNullability>(
    val receiverClass: Class<*>,
    private val method: Method,
    val params: Array<out Class<*>>,
    val returnType: Class<*>) {

    operator fun invoke(receiver: ReceiverNullability, vararg args: Any?): ReturnNullability {
        @Suppress("UNCHECKED_CAST")
        return method.invoke(receiver, *args) as ReturnNullability
    }

}
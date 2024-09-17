package dev.sparky200.skriptnbt.reflect

fun loadClass(classLoader: ClassLoader, name: String) = ClassDef(classLoader.loadClass(name))

class ClassDef(val cl: Class<*>) {

    fun <ReceiverNullability, ReturnNullability> function(
        name: String,
        returnType: Class<*>,
        vararg params: Class<*>
    ) = FunctionDef<ReceiverNullability, ReturnNullability>(
        cl,
        cl.getDeclaredMethod(name, *params),
        params,
        returnType
    )

    fun constructor(vararg params: Class<*>): (Array<out Any?>) -> Any {
        // Check both declared and non-declared
        val constructor = try {
            cl.getDeclaredConstructor(*params)
        } catch (e: Exception) {
            cl.getConstructor(*params)
        }
        constructor.isAccessible = true
        return constructor::newInstance
    }

    /**
     * Empty constructor call
     */
    operator fun invoke(): Any {
        val constructor = cl.getDeclaredConstructor() ?: cl.getConstructor()
        return constructor.newInstance()
    }

    /**
     * Constructor call - only valid with not-null args
     */
    operator fun invoke(vararg args: Any): Any {
        // Try primitive types over object types
        val params = args.map { it.javaClass.kotlin.javaPrimitiveType ?: it.javaClass.kotlin.javaObjectType }.toTypedArray()
        val constructor = try {
            constructor(*params)
        } catch (e: Exception) {
            // If that doesn't work, try object types
            constructor(*args.map { it.javaClass.kotlin.javaObjectType }.toTypedArray())
        }
        return constructor(args)
    }

}

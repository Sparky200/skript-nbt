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
        val constructor = cl.getDeclaredConstructor(*params) ?: cl.getConstructor(*params)
        constructor.isAccessible = true
        return constructor::newInstance
    }

    /**
     * Empty constructor call
     */
    operator fun invoke(): Any {
        val constructor = cl.getDeclaredConstructor() ?: cl.getConstructor()
        constructor.isAccessible = true
        return constructor.newInstance()
    }

    /**
     * Constructor call - only valid with not-null args
     */
    operator fun invoke(vararg args: Any): Any {
        val params = args.map { it.javaClass }.toTypedArray()
        val constructor = cl.getDeclaredConstructor(*params) ?: cl.getConstructor(*params)
        constructor.isAccessible = true
        return constructor.newInstance(args)
    }

}

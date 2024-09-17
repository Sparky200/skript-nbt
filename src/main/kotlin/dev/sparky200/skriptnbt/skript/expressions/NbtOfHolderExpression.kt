package dev.sparky200.skriptnbt.skript.expressions

import ch.njol.skript.Skript.registerExpression
import ch.njol.skript.classes.Changer
import ch.njol.skript.expressions.base.SimplePropertyExpression
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.ExpressionType
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import dev.sparky200.skriptnbt.SkriptNbt
import dev.sparky200.skriptnbt.skript.types.MutableCompound
import dev.sparky200.skriptnbt.skript.types.wrap
import net.benwoodworth.knbt.NbtCompound
import org.bukkit.entity.Entity
import org.bukkit.event.Event
import kotlin.io.path.Path

/**
 * Expression holding all property access for NBT holders, such as entities, blocks, items, and more.
 */
class NbtOfHolderExpression : SimplePropertyExpression<Any, MutableCompound>() {
    companion object {

        init {
            registerExpression(NbtOfHolderExpression::class.java, MutableCompound::class.java,
                ExpressionType.PROPERTY,
                "nbt [compound] (of|from) %objects%",
                "nbt [compound] (of|from) file[s] %strings%",
                "nbt [compound] (of|from) text %strings%"
            )
        }
    }

    private var isFromFile: Boolean = false
    private var isFromText: Boolean = false
    private val canBeMutated get() = !isFromFile && !isFromText

    private fun rhsError(): Nothing =
        throw IllegalArgumentException("The right-hand side of the effect must be an nbt compound")

    private fun noNbtError(event: Event?): Nothing =
        throw IllegalArgumentException("'${expr.toString(event, false)}' does not have NBT")

    override fun acceptChange(mode: Changer.ChangeMode?): Array<Class<*>?>? {
        if (!canBeMutated) return null
        return when (mode) {
            Changer.ChangeMode.ADD,
            Changer.ChangeMode.SET -> arrayOf(MutableCompound::class.java)
            Changer.ChangeMode.REMOVE -> arrayOf(MutableCompound::class.java, Collection::class.java, String::class.java, Array::class.java)
            else -> {
                super.acceptChange(mode)
            }
        }
    }

    override fun change(event: Event?, delta: Array<out Any?>?, mode: Changer.ChangeMode?) {
        if (!canBeMutated) throw IllegalStateException("Cannot mutate ")
        val theDelta =
            if (delta != null && delta.isNotEmpty())
                delta[0]
            else null

        when (mode) {
            Changer.ChangeMode.SET -> {
                if (theDelta == null) rhsError()
                @Suppress("UNCHECKED_CAST")
                if (expr.isSingle) set(event, expr.getSingle(event), theDelta as MutableCompound)
                else expr.getArray(event).forEach { set(event, it, theDelta as MutableCompound) }
            }
            Changer.ChangeMode.ADD -> {
                if (theDelta == null) rhsError()
                @Suppress("UNCHECKED_CAST")
                if (expr.isSingle) add(event, expr.getSingle(event), theDelta as MutableCompound)
                else expr.getArray(event).forEach { add(event, it, theDelta as MutableCompound) }
            }
            Changer.ChangeMode.REMOVE -> {
                if (theDelta == null) rhsError()
                if (expr.isSingle) remove(event, expr.getSingle(event), theDelta)
                else expr.getArray(event).forEach { remove(event, it, theDelta) }
            }
            else -> super.change(event, delta, mode)
        }
    }

    private fun set(event: Event?, obj: Any?, delta: MutableCompound) = with (SkriptNbt.platform) {
        when (obj) {
            is Entity -> obj.setNbt(nbtTagOf(delta) as NbtCompound)
            is String -> {
                if (isFromFile) nbtToFile(Path(obj), nbtTagOf(delta) as NbtCompound)
            }
            else -> noNbtError(event)
        }
    }

    private fun add(event: Event?, obj: Any?, delta: MutableCompound) = with (SkriptNbt.platform) {
        when (obj) {
            is Entity -> obj.setNbt(nbtTagOf(obj.getNbt().toMap() + delta.map) as NbtCompound)
            // no-op (need to save the string NBT to variable first)
            is String -> {}
            else -> noNbtError(event)
        }
    }

    private fun remove(event: Event?, obj: Any?, delta: Any) = with (SkriptNbt.platform) {
        val theDelta: Collection<*> = when (delta) {
            is MutableMap<*, *> -> delta.keys
            is Collection<*> -> delta
            is String -> listOf(delta)
            else -> throw IllegalArgumentException("delta must be one of MutableMap, Collection, or String")
        }
        if (theDelta.isEmpty()) return@with
        if (theDelta.first() !is String) throw IllegalArgumentException("delta must be or contain only Strings")

        when (obj) {
            is Entity -> obj.setNbt(NbtCompound(obj.getNbt().filterNot { it.key in theDelta }))
            // no-op (need to save the string NBT to variable first)
            is String -> {}
            else -> noNbtError(event)
        }
    }

    override fun init(
        expressions: Array<out Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult
    ): Boolean {
        if (!super.init(expressions, matchedPattern, isDelayed, parseResult)) return false
        isFromFile = matchedPattern == 1
        isFromText = matchedPattern == 2
        return true
    }

    override fun convert(it: Any): MutableCompound = with (SkriptNbt.platform) {
        when (it) {
            is Entity -> it.getNbt()
            is String -> {
                if (isFromFile)
                    nbtFromFile(Path(it))
                else if (isFromText)
                    nbtFromString(it)
                else throw IllegalArgumentException("use 'nbt compound from text %strings%' instead")
            }
            else -> throw IllegalArgumentException("cannot get NBT compound from ${it.javaClass.simpleName}")
        }.toList().associate { it.first to nbtValueOf(it.second) }.toMutableMap().wrap()
    }

    override fun getReturnType(): Class<out MutableCompound> = MutableCompound::class.java
    override fun getPropertyName(): String = "nbt compound"

    override fun toString(event: Event?, debug: Boolean): String =
        "nbt of ${expr.toString(event, debug)}"
}

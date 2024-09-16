package dev.sparky200.skriptnbt.skript.expressions

import ch.njol.skript.Skript.registerExpression
import ch.njol.skript.classes.Changer
import ch.njol.skript.expressions.base.PropertyExpression
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.ExpressionType
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import dev.sparky200.skriptnbt.SkriptNbt
import dev.sparky200.skriptnbt.skript.types.MutableCompound
import org.bukkit.event.Event

class TagFromCompoundExpression : PropertyExpression<MutableCompound, Any?>() {
    companion object {
        init {
            registerExpression(
                TagFromCompoundExpression::class.java,
                Any::class.java,
                ExpressionType.PROPERTY,
                "tag %strings% (of|from) %objects%"
            )
        }
    }

    private lateinit var keys: Expression<String>

    override fun init(
        expressions: Array<out Expression<*>?>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult
    ): Boolean {

        @Suppress("UNCHECKED_CAST")
        keys = expressions[0] as? Expression<String> ?: return false
        @Suppress("UNCHECKED_CAST")
        expr = expressions[1]?.getConvertedExpression(MutableCompound::class.java)
                as? Expression<MutableCompound> ?: return false

        return true
    }

    override fun get(event: Event, source: Array<out MutableCompound>): Array<out Any?> {
        val key = keys.getSingle(event)
        return source.mapNotNull { it.map[key] }.toTypedArray()
    }

    override fun acceptChange(mode: Changer.ChangeMode?): Array<out Class<*>?>? =
        when (mode) {
            // TODO support ADD and REMOVE
            Changer.ChangeMode.SET -> arrayOf(
                String::class.java,
                Byte::class.java,
                Short::class.java,
                Int::class.java,
                Long::class.java,
                Float::class.java,
                Double::class.java,
                Boolean::class.java,
                ByteArray::class.java,
                IntArray::class.java,
                LongArray::class.java,
            )
            Changer.ChangeMode.DELETE,
            Changer.ChangeMode.RESET -> arrayOf()
            else -> super.acceptChange(mode)
        }

    override fun change(event: Event, delta: Array<out Any?>, mode: Changer.ChangeMode) {
        val compound = expr.getSingle(event) ?: return
        val key = keys.getSingle(event) ?: return

        when (mode) {
            Changer.ChangeMode.SET -> {
                if (delta.size == 1) compound.map[key] = with (SkriptNbt.platform) { nbtTagOf(delta[0]) }
                else for (change in delta) {
                    compound.map[key] = with (SkriptNbt.platform) { nbtTagOf(change) }
                }
            }
            Changer.ChangeMode.DELETE, Changer.ChangeMode.RESET -> {
                compound.map.remove(key)
            }
            else -> super.change(event, delta, mode)
        }
    }

    override fun getReturnType(): Class<out Any> = Any::class.java

    override fun toString(event: Event?, debug: Boolean): String? =
        "tag ${keys.toString(event, debug)} of ${expr.toString(event, debug)}"
}
package dev.sparky200.skriptnbt.skript

import ch.njol.skript.Skript
import dev.sparky200.skriptnbt.skript.expressions.NbtOfHolderExpression
import dev.sparky200.skriptnbt.skript.expressions.TagFromCompoundExpression
import dev.sparky200.skriptnbt.skript.types.NbtCompoundType
import org.bukkit.plugin.java.JavaPlugin

object SkriptHook {
    private fun JavaPlugin.isSkriptPresent() = server.pluginManager.isPluginEnabled("Skript")

    fun JavaPlugin.hook() {
        if (!isSkriptPresent()) {
            logger.warning("Skript is not present! Nothing will be registered.")
            return
        }

        val skript = try {
            server.pluginManager.getPlugin("Skript") as Skript
        } catch (e: Exception) {
            logger.severe("Error getting the Skript plugin: ${e.message}")
            return
        }
        logger.info("Skript found at '${skript.javaClass.name}' (version ${skript.description.version}).")

        with (Skript.registerAddon(this)) {
            // TODO remove if not needed
        }

        NbtCompoundType
        NbtOfHolderExpression
        TagFromCompoundExpression
    }
}

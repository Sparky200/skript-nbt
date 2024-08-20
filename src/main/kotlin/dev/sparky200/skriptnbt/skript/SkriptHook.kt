package dev.sparky200.skriptnbt.skript

import ch.njol.skript.Skript
import org.bukkit.plugin.java.JavaPlugin

object SkriptHook {
    fun JavaPlugin.isSkriptPresent() = server.pluginManager.isPluginEnabled("Skript")

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
    }
}
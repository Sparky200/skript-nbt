package dev.sparky200.skriptnbt

/**
 * The SkriptNbt state.
 */
object SkriptNbt {
    var initialized: Boolean = false; private set
    var platform: NbtPlatform = NbtPlatform.Uninitialized; private set

    internal fun enable(platform: NbtPlatform) {
        if (initialized) throw IllegalStateException("SkriptNbt has already been initialized")
        initialized = true
        this.platform = platform
    }

    internal fun disable() {
        if (!initialized) throw IllegalStateException("SkriptNbt has not been initialized")
        initialized = false
        platform = NbtPlatform.Uninitialized
    }
}
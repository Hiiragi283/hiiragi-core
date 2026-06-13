package hiiragi283.lib.entity

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity

@Suppress("NOTHING_TO_INLINE")
inline fun Entity.serverLevel(): ServerLevel? = this.level() as? ServerLevel

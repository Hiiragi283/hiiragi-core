package hiiragi283.lib.entity

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity

fun Entity.serverLevel(): ServerLevel? = this.level() as? ServerLevel

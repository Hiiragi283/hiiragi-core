package hiiragi283.core.api.entity

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3

fun Entity.serverLevel(): ServerLevel? = this.level() as? ServerLevel

fun Entity.spawnAtLocation(stack: ItemStack, offset: Vec3 = Vec3.ZERO): ItemEntity? {
    val level: ServerLevel = this.serverLevel() ?: return null
    return this.spawnAtLocation(level, stack)
}

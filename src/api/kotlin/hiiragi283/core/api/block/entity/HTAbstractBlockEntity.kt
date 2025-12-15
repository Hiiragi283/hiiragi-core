package hiiragi283.core.api.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.server.ServerLifecycleHooks

/**
 * [BlockEntity]を抽象化するインターフェース
 * @see mekanism.common.tile.interfaces.ITileWrapper
 */
interface HTAbstractBlockEntity {
    fun getLevel(): Level?

    fun getServerLevel(): ServerLevel? = this.getLevel() as? ServerLevel

    fun getRegistryAccess(): RegistryAccess? = getLevel()?.registryAccess() ?: ServerLifecycleHooks.getCurrentServer()?.registryAccess()

    fun getBlockPos(): BlockPos
}

package hiiragi283.core.api.block.entity

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [BlockEntity]を抽象化するインターフェース
 * @see mekanism.common.tile.interfaces.ITileWrapper
 */
interface HTAbstractBlockEntity {
    fun getLevel(): Level?

    fun getServerLevel(): ServerLevel? = this.getLevel() as? ServerLevel

    fun getRegistryAccess(): RegistryAccess? = getLevel()?.registryAccess() ?: HiiragiCoreAPI.getActiveAccess()

    fun getBlockPos(): BlockPos
}

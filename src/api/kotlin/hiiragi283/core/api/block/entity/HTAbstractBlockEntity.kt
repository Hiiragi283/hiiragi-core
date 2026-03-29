package hiiragi283.core.api.block.entity

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.core.BlockPos
import net.minecraft.core.RegistryAccess
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [BlockEntity]に実装されるインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.tile.interfaces.ITileWrapper
 */
interface HTAbstractBlockEntity {
    /**
     * [レベル][Level]を取得します。
     * @return レベルがない場合は`null`
     */
    fun getLevel(): Level?

    /**
     * [サーバーレベル][ServerLevel]を取得します。
     * @return レベルがない場合，または[getLevel]の戻り値が[ServerLevel]にキャストできない場合は`null`
     */
    fun getServerLevel(): ServerLevel? = this.getLevel() as? ServerLevel

    /**
     * [レジストリへのアクセス][RegistryAccess]を取得します。
     * @return [getServerLevel]の戻り値がない場合，または[HiiragiCoreAPI.getActiveAccess]の戻り値がない場合は`null`
     */
    fun getRegistryAccess(): RegistryAccess? = getLevel()?.registryAccess() ?: HiiragiCoreAPI.getActiveAccess()

    /**
     * [座標][BlockPos]を取得します。
     */
    fun getBlockPos(): BlockPos
}

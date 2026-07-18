package hiiragi283.core.api.block.entity

import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.flatMap
import hiiragi283.core.api.util.flatMapLeft
import hiiragi283.core.api.util.toTextResult
import hiiragi283.core.util.HTPhysicalSideHelper
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
     * レベルを取得します。
     * @since 21.1.0
     */
    fun getLevelResult(): HTTextResult<Level> = getLevel().toTextResult { "Block entity at ${getBlockPos()} is not bounded to level" }

    /**
     * サーバーレベルを取得します。
     * @since 21.1.0
     */
    fun getServerLevel(): HTTextResult<ServerLevel> = getLevelResult().flatMap { level: Level -> (level as? ServerLevel).toTextResult { "Block entity at ${getBlockPos()} does not exist in server-side" } }

    /**
     * [レジストリへのアクセス][RegistryAccess]を取得します。
     */
    fun getRegistryAccess(): HTTextResult<RegistryAccess> = getLevelResult().map(Level::registryAccess).flatMapLeft { HTPhysicalSideHelper.getRegistryAccess() }

    /**
     * [座標][BlockPos]を取得します。
     */
    fun getBlockPos(): BlockPos
}

package hiiragi283.lib.world

import hiiragi283.lib.entity.serverLevel
import hiiragi283.lib.transfer.item.ItemResourceHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Containers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.item.ItemUtil

/**
 * アイテムのドロップ処理を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTItemDropHelper {
    /**
     * アイテムをインベントリに入れるか，足元にドロップします。
     * @param entity インベントリの所有者
     * @param stack 対象のアイテム
     * @param offset ドロップ時のy座標のオフセット
     */
    @JvmStatic
    fun giveOrDropStack(entity: Entity, stack: ItemStack, offset: Float = 0f) {
        if (entity is Player) {
            giveStackTo(entity, stack)
        } else {
            val level: ServerLevel = entity.serverLevel() ?: return
            val remainStack: ItemStack = entity.getCapability(Capabilities.Item.ENTITY)?.let { handler: ItemResourceHandler ->
                ItemUtil.insertItemReturnRemaining(handler, stack, false, null)
            } ?: stack
            entity.spawnAtLocation(level, remainStack, offset)
        }
    }

    /**
     * アイテムをプレイヤーのインベントリに入れます。
     * @param player インベントリの所有者
     * @param stack 対象のアイテム
     */
    @JvmStatic
    fun giveStackTo(player: Player, stack: ItemStack) {
        val level: ServerLevel = player.serverLevel() ?: return
        if (player.isFakePlayer) {
            player.spawnAtLocation(level, stack)
        } else {
            player.inventory.add(stack)
        }
    }

    /**
     * アイテムをドロップします。
     * @param level ドロップ先のレベル
     * @param pos ドロップ先の座標
     * @param stack 対象のアイテム
     */
    @JvmStatic
    fun dropStackAt(level: Level, pos: BlockPos, stack: ItemStack) {
        Containers.dropItemStack(level, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
    }

    /**
     * アイテムをドロップします。
     * @param level ドロップ先のレベル
     * @param pos ドロップ先の座標
     * @param stack 対象のアイテム
     */
    @JvmStatic
    fun dropStackAt(level: Level, pos: Position, stack: ItemStack) {
        ItemEntity(level, pos.x(), pos.y(), pos.z(), stack).let(level::addFreshEntity)
    }
}

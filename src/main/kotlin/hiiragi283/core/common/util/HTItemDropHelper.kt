package hiiragi283.core.common.util

import hiiragi283.core.api.transfer.ItemResourceHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Containers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.item.ItemUtil

object HTItemDropHelper {
    /**
     * 指定した[stack]を[entity]のインベントリに入れるか，足元にドロップします
     */
    fun giveOrDropStack(entity: Entity, stack: ItemStack, offset: Float = 0f) {
        val level: ServerLevel = entity.level() as? ServerLevel ?: return
        if (entity is Player) {
            giveStackTo(entity, stack)
        } else {
            val remainStack: ItemStack = entity.getCapability(Capabilities.Item.ENTITY)?.let { handler: ItemResourceHandler ->
                ItemUtil.insertItemReturnRemaining(handler, stack, false, null)
            } ?: stack
            entity.spawnAtLocation(level, remainStack, offset)
        }
    }

    /**
     * 指定した[stack]を[player]のインベントリに入れます。
     */
    fun giveStackTo(player: Player, stack: ItemStack) {
        val level: ServerLevel = player.level() as? ServerLevel ?: return
        if (player.isFakePlayer) {
            player.spawnAtLocation(level, stack)
        } else {
            player.inventory.add(stack)
        }
    }

    /**
     * 指定した[stack]を[pos]にドロップします。
     */
    fun dropStackAt(level: Level, pos: BlockPos, stack: ItemStack) {
        dropStackAt(level, Vec3.atCenterOf(pos), stack)
    }

    /**
     * 指定した[stack]を[pos]にドロップします。
     */
    fun dropStackAt(level: Level, pos: Position, stack: ItemStack) {
        Containers.dropItemStack(level, pos.x(), pos.y(), pos.z(), stack)
    }
}

package hiiragi283.core.api.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ThrowableItemProjectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.level.Level

abstract class HTThrowableItem(properties: Properties) :
    Item(properties),
    ProjectileItem {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        level.playSound(
            null,
            player.x,
            player.y,
            player.z,
            SoundEvents.EGG_THROW,
            SoundSource.PLAYERS,
            0.5f,
            0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f),
        )
        if (!level.isClientSide) {
            val projectile: ThrowableItemProjectile = create(level, player)
            projectile.item = stack
            projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0f, 1.5f, 1.0f)
            level.addFreshEntity(projectile)
        }
        player.awardStat(Stats.ITEM_USED.get(this))
        if (player is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack)
        }
        stack.consume(1, player)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    final override fun asProjectile(
        level: Level,
        pos: Position,
        stack: ItemStack,
        direction: Direction,
    ): Projectile {
        val projectile: ThrowableItemProjectile = create(level, pos.x(), pos.y(), pos.z())
        projectile.item = stack
        return projectile
    }

    protected abstract fun create(level: Level, player: Player): ThrowableItemProjectile

    protected abstract fun create(
        level: Level,
        x: Double,
        y: Double,
        z: Double,
    ): ThrowableItemProjectile
}

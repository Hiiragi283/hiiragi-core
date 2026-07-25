package hiiragi283.core.common.item.block

import hiiragi283.core.api.collection.randomOrNull
import hiiragi283.core.common.block.HTWarpedWartBlock
import net.minecraft.core.Holder
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

class HTWarpedWartItem(block: HTWarpedWartBlock, properties: Properties) : BlockItem(block, properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val effects: List<Holder<MobEffect>> = getBadEffects(livingEntity)
        val operation: Int = when {
            livingEntity.isShiftKeyDown -> minOf(effects.size, stack.count)
            else -> 1
        }
        repeat(operation) { effects.randomOrNull(level.random)?.let(livingEntity::removeEffect) }
        stack.consume(operation, livingEntity)
        return super.finishUsingItem(stack, level, livingEntity)
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.EAT

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32

    // デバフがない場合は使用できない
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> = when {
        getBadEffects(player).isEmpty() -> InteractionResultHolder.fail(player.getItemInHand(usedHand))
        else -> ItemUtils.startUsingInstantly(level, player, usedHand)
    }

    private fun getBadEffects(livingEntity: LivingEntity): List<Holder<MobEffect>> = livingEntity.activeEffects
        .map(MobEffectInstance::getEffect)
        .filter { it.value().category == MobEffectCategory.HARMFUL }
}

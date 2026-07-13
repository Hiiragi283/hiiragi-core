package hiiragi283.core.common.item.endgame

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

class HTInfinityPotionItem(properties: Properties) : HTCreativeItem(properties) {
    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        if (livingEntity is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(livingEntity, stack)
            livingEntity.awardStat(Stats.ITEM_USED.get(this))
        }
        // 有限のエフェクトだけを無限化する
        getFiniteEffects(livingEntity)
            .map {
                MobEffectInstance(
                    it.effect,
                    -1,
                    it.amplifier,
                    it.isAmbient,
                    it.isVisible,
                    it.showIcon(),
                )
            }.forEach(livingEntity::addEffect)
        stack.consume(1, livingEntity)
        return super.finishUsingItem(stack, level, livingEntity)
    }

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.DRINK

    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32

    // すべてのエフェクトが永続の場合は使用できない
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> = when {
        getFiniteEffects(player).isEmpty() -> InteractionResultHolder.fail(player.getItemInHand(usedHand))
        else -> ItemUtils.startUsingInstantly(level, player, usedHand)
    }

    private fun getFiniteEffects(livingEntity: LivingEntity): List<MobEffectInstance> = livingEntity.activeEffects.filterNot(MobEffectInstance::isInfiniteDuration)
}

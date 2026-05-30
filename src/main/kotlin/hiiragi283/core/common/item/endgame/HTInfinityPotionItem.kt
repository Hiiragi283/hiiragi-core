package hiiragi283.core.common.item.endgame

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.level.Level

class HTInfinityPotionItem(properties: Properties) : HTCreativeItem(properties) {
    override fun finishUsingItem(itemStack: ItemStack, level: Level, entity: LivingEntity): ItemStack {
        if (entity is ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(entity, itemStack)
            entity.awardStat(Stats.ITEM_USED.get(this))
        }
        // 有限のエフェクトだけを無限化する
        getFiniteEffects(entity)
            .map {
                MobEffectInstance(
                    it.effect,
                    -1,
                    it.amplifier,
                    it.isAmbient,
                    it.isVisible,
                    it.showIcon(),
                )
            }.forEach(entity::addEffect)
        itemStack.consume(1, entity)
        return super.finishUsingItem(itemStack, level, entity)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult = when {
        getFiniteEffects(player).isEmpty() -> InteractionResult.FAIL
        else -> ItemUtils.startUsingInstantly(level, player, hand)
    }

    private fun getFiniteEffects(entity: LivingEntity): List<MobEffectInstance> = entity.activeEffects.filterNot(MobEffectInstance::isInfiniteDuration)
}

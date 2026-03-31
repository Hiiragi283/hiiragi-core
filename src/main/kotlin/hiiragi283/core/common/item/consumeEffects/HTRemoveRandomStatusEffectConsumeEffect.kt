package hiiragi283.core.common.item.consumeEffects

import hiiragi283.core.api.collection.randomOrNull
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.setup.HCConsumables
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.consume_effects.ConsumeEffect
import net.minecraft.world.level.Level

data object HTRemoveRandomStatusEffectConsumeEffect : ConsumeEffect {
    @JvmField
    val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTRemoveRandomStatusEffectConsumeEffect> =
        MapBiCodecs.unit(HTRemoveRandomStatusEffectConsumeEffect)

    @JvmField
    val TYPE: ConsumeEffect.Type<HTRemoveRandomStatusEffectConsumeEffect> = CODEC.toSerializer(HCConsumables::createType)

    override fun getType(): ConsumeEffect.Type<HTRemoveRandomStatusEffectConsumeEffect> = TYPE

    override fun apply(level: Level, stack: ItemStack, user: LivingEntity): Boolean = user.activeEffects
        .map(MobEffectInstance::getEffect)
        .filter { it.value().category == MobEffectCategory.HARMFUL }
        .randomOrNull(level.random)
        ?.let(user::removeEffect)
        ?: false
}

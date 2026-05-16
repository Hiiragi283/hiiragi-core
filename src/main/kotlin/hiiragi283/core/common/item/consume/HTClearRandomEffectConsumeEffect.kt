package hiiragi283.core.common.item.consume

import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.consume_effects.ConsumeEffect
import net.minecraft.world.level.Level

data object HTClearRandomEffectConsumeEffect : ConsumeEffect {
    @JvmField
    val CODEC: MapCodec<HTClearRandomEffectConsumeEffect> = MapCodec.unit { HTClearRandomEffectConsumeEffect }

    @JvmField
    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTClearRandomEffectConsumeEffect> = StreamCodec.unit(HTClearRandomEffectConsumeEffect)

    @JvmField
    val TYPE: ConsumeEffect.Type<HTClearRandomEffectConsumeEffect> = ConsumeEffect.Type(CODEC, STREAM_CODEC)

    override fun getType(): ConsumeEffect.Type<HTClearRandomEffectConsumeEffect> = TYPE

    override fun apply(level: Level, stack: ItemStack, user: LivingEntity): Boolean = user.activeEffects
        .map(MobEffectInstance::getEffect)
        .filter { it.value().category == MobEffectCategory.HARMFUL }
        .let { it.elementAtOrNull(level.random.nextInt(it.size)) }
        ?.let(user::removeEffect) != null
}

package hiiragi283.core.setup

import com.mojang.serialization.MapCodec
import hiiragi283.core.common.item.consumeEffects.HTRemoveRandomStatusEffectConsumeEffect
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.consume_effects.ConsumeEffect

data object HCConsumables {
    @JvmField
    val WARPED_WART: Consumable = Consumables.defaultFood().onConsume(HTRemoveRandomStatusEffectConsumeEffect).build()

    @JvmStatic
    fun <T : ConsumeEffect> createType(codec: MapCodec<T>, streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>): ConsumeEffect.Type<T> =
        ConsumeEffect.Type(codec, streamCodec)
}

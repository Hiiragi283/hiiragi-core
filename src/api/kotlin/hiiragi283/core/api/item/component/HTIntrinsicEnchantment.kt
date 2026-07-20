package hiiragi283.core.api.item.component

import com.mojang.serialization.Codec
import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.registry.getResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.identity
import io.netty.buffer.ByteBuf
import java.util.function.Consumer
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.minecraft.world.item.enchantment.Enchantment

@JvmRecord
data class HTIntrinsicEnchantment(val key: ResourceKey<Enchantment>, val level: Int) : TooltipProvider {
    companion object {
        @JvmField
        val CODEC: Codec<HTIntrinsicEnchantment> = HTCodecs.record { instance ->
            instance
                .group(
                    HTCodecs.resourceKey(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(HTIntrinsicEnchantment::key),
                    Codec.INT.optionalFieldOf("level", 1).forGetter(HTIntrinsicEnchantment::level),
                ).apply(instance, ::HTIntrinsicEnchantment)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTIntrinsicEnchantment> = StreamCodec.composite(
            HTStreamCodecs.resourceKey(Registries.ENCHANTMENT),
            HTIntrinsicEnchantment::key,
            ByteBufCodecs.VAR_INT,
            HTIntrinsicEnchantment::level,
            ::HTIntrinsicEnchantment,
        )
    }

    fun <T : Any> useInstance(getter: HolderGetter<Enchantment>, action: (Holder<Enchantment>, Int) -> T): HTTextResult<T> = getter.getResult(key).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun <T : Any> useInstance(provider: HolderLookup.Provider, action: (Holder<Enchantment>, Int) -> T): HTTextResult<T> = provider.asGetterLookup().getResult(key).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun getFullName(provider: HolderLookup.Provider): HTTextResult<Text> = useInstance(provider, Enchantment::getFullname)

    override fun addToTooltip(context: Item.TooltipContext, tooltipAdder: Consumer<Text>, tooltipFlag: TooltipFlag) {
        when {
            tooltipFlag.hasShiftDown() ->
                context
                    .registries()
                    ?.let(::getFullName)
                    ?.fold(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT::translate, identity())
            else -> HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(HTDefaultColor.YELLOW)
        }?.let(tooltipAdder::accept)
    }
}

package hiiragi283.lib.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.Text
import io.netty.buffer.ByteBuf
import java.util.Optional
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
import java.util.function.Consumer
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponentGetter

@JvmRecord
data class HTIntrinsicEnchantment(val key: ResourceKey<Enchantment>, val level: Int) : TooltipProvider {
    companion object {
        @JvmField
        val CODEC: Codec<HTIntrinsicEnchantment> = RecordCodecBuilder.create { instance ->
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

    fun <T : Any> useInstance(getter: HolderGetter<Enchantment>, action: (Holder<Enchantment>, Int) -> T): Optional<T> = getter.get(key).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun <T : Any> useInstance(provider: HolderLookup.Provider, action: (Holder<Enchantment>, Int) -> T): Optional<T> = provider.holder(key).map { holder: Holder<Enchantment> -> action(holder, level) }

    fun getFullName(provider: HolderLookup.Provider): Optional<Text> = useInstance(provider, Enchantment::getFullname)

    override fun addToTooltip(context: Item.TooltipContext, consumer: Consumer<Text>, flag: TooltipFlag, components: DataComponentGetter) {
        when {
            flag.hasShiftDown() ->
                context
                    .registries()
                    ?.let(::getFullName)
                    ?.map(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT::translate)
                    ?.orElseGet(HTCommonTranslation.MISSING_KEY::translate)
            else -> HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW)
        }?.let(consumer::accept)
    }
}

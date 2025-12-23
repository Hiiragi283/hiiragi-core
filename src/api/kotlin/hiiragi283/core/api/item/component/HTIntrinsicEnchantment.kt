package hiiragi283.core.api.item.component

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.registry.getOrNull
import hiiragi283.core.api.registry.holderOrNull
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import hiiragi283.core.api.text.toTextResult
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import net.minecraft.world.item.enchantment.Enchantment
import java.util.function.Consumer

@JvmRecord
data class HTIntrinsicEnchantment(val key: ResourceKey<Enchantment>, val level: Int) : TooltipProvider {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTIntrinsicEnchantment> = BiCodec.composite(
            VanillaBiCodecs.resourceKey(Registries.ENCHANTMENT).fieldOf("enchantment").forGetter(HTIntrinsicEnchantment::key),
            BiCodec.INT.optionalFieldOf("level", 1).forGetter(HTIntrinsicEnchantment::level),
            ::HTIntrinsicEnchantment,
        )
    }

    fun <T : Any> useInstance(getter: HolderGetter<Enchantment>, action: (Holder<Enchantment>, Int) -> T): HTTextResult<T> = getter
        .getOrNull(key)
        ?.let { holder: Holder<Enchantment> -> action(holder, level) }
        ?.let { Either.left(it) }
        ?: HTCommonTranslation.MISSING_KEY.toTextResult()

    fun <T : Any> useInstance(provider: HolderLookup.Provider?, action: (Holder<Enchantment>, Int) -> T): HTTextResult<T> {
        val provider1: HolderLookup.Provider =
            (provider ?: HiiragiCoreAPI.getActiveAccess()) ?: return HTCommonTranslation.MISSING_SERVER.toTextResult()
        return provider1
            .holderOrNull(key)
            ?.let { holder: Holder<Enchantment> -> action(holder, level) }
            ?.let { Either.left(it) }
            ?: HTCommonTranslation.MISSING_KEY.toTextResult()
    }

    fun getFullName(provider: HolderLookup.Provider?): HTTextResult<Component> = useInstance(provider, Enchantment::getFullname)

    override fun addToTooltip(context: Item.TooltipContext, tooltipAdder: Consumer<Component>, tooltipFlag: TooltipFlag) {
        when {
            tooltipFlag.hasShiftDown() -> getFullName(context.registries())
                .map(
                    { HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT.translate(it) },
                    identity(),
                )
            else -> HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(ChatFormatting.YELLOW)
        }.let(tooltipAdder::accept)
    }
}

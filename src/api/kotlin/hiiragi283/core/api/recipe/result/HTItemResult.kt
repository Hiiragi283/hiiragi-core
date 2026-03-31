package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate

data class HTItemResult(val template: ItemStackTemplate, val chance: Float = 1f) : ItemInstance by template {
    companion object {
        @JvmStatic
        private val TEMPLATE_CODEC: MapBiCodec<RegistryFriendlyByteBuf, ItemStackTemplate> =
            MapBiCodec.of(ItemStackTemplate.MAP_CODEC, ItemStackTemplate.STREAM_CODEC)

        @JvmStatic
        private val CHANCE_CODEC: MapBiCodec<ByteBuf, Float> =
            BiCodec.FLOAT.validate(BiCodecs.checkRange(0f..1f)).optionalFieldOf(HTConst.CHANCE, 1f)

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodec.composite(
            TEMPLATE_CODEC.forGetter(HTItemResult::template),
            CHANCE_CODEC.forGetter(HTItemResult::chance),
            ::HTItemResult,
        )
    }

    fun create(): ItemStack = template.create()

    fun create(random: Float): ItemStack = when {
        random.coerceIn(0f, 1f) <= this.chance -> create()
        else -> ItemStack.EMPTY
    }
}

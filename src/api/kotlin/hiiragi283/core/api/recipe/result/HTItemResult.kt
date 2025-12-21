package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.api.stack.toImmutable
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [アイテム][ImmutableItemStack]の[完成品][HTRecipeResult]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
class HTItemResult(entry: HTHolderOrTagKey<Item>, amount: Int, components: DataComponentPatch) :
    HTBasicRecipeResult<Item, ImmutableItemStack>(entry, amount, components) {
    companion object {
        @JvmStatic
        private val ENTRY_CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTHolderOrTagKey<Item>> = HTHolderOrTagKey
            .codec(Registries.ITEM)
            .validate { entry: HTHolderOrTagKey<Item> ->
                check(entry.getId() != vanillaId("air")) { "Item must not be minecraft:air" }
                entry
            }

        /**
         * [HTItemResult]の[BiCodec]
         */
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodec.composite(
            ENTRY_CODEC.forGetter(HTItemResult::entry),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.COUNT, 1).forGetter(HTItemResult::amount),
            VanillaBiCodecs.COMPONENT_PATCH.forGetter(HTItemResult::components),
            ::HTItemResult,
        )
    }

    override fun createStack(holder: Holder<Item>, amount: Int, components: DataComponentPatch): ImmutableItemStack? =
        ItemStack(holder, amount, components).toImmutable()

    override fun toString(): String = "HTItemResult(entry=$entry, amount=$amount, components=$components)"
}

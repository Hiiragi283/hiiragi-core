package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * [アイテム][ItemStack]の[完成品][HTRecipeResult]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
class HTItemResult(contents: Ior<HTItemResourceType, TagKey<Item>>, amount: Int) :
    HTResourceRecipeResult<Item, HTItemResourceType, ItemStack>(contents, amount) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodec.composite(
            MapBiCodecs
                .ior(
                    HTItemResourceType.CODEC.toOptional().toMap(),
                    VanillaBiCodecs.tagKey(Registries.ITEM, false).optionalFieldOf(HTConst.TAG),
                ).forGetter(HTItemResult::contents),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.COUNT, 1).forGetter(HTItemResult::amount),
            ::HTItemResult,
        )
    }

    override fun getEmptyStack(): ItemStack = ItemStack.EMPTY

    override fun createStack(resource: HTItemResourceType, amount: Int): ItemStack = resource.toStack(amount)

    override fun createStack(holder: Holder<Item>, amount: Int): ItemStack = ItemStack(holder, amount)
}

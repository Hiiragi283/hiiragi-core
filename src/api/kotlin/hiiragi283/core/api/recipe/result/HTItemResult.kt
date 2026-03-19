package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.holderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.unwrap
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate

sealed interface HTItemResult : HTRecipeResult<ItemStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodecs
            .either(Template.CODEC, HolderBased.CODEC, true)
            .xmap(Either<Template, HolderBased>::unwrap) { result: HTItemResult ->
                when (result) {
                    is HolderBased -> Either.Right(result)
                    is Template -> Either.Left(result)
                }
            }
    }

    class Template(private val template: ItemStackTemplate) :
        HTItemResult,
        ItemInstance by template {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, Template> = VanillaBiCodecs.ITEM_STACK_TEMPLATE.xmap(::Template, Template::template)
        }

        override fun create(): ItemStack = template.create()

        override fun getId(): Identifier = this.holderLike().getId()
    }

    class HolderBased(private val holderSet: HolderSet<Item>, private val count: Int) : HTItemResult {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, HolderBased> = BiCodec.composite(
                VanillaBiCodecs.holderSet(Registries.ITEM).fieldOf(HTConst.ITEMS).forGetter(HolderBased::holderSet),
                BiCodecs.POSITIVE_INT.fieldOf(HTConst.COUNT).forGetter(HolderBased::count),
                ::HolderBased,
            )
        }

        override fun create(): ItemStack {
            val holder: Holder<Item> = holderSet.firstOrNull() ?: return ItemStack.EMPTY
            return ItemStack(holder, count)
        }

        override fun getId(): Identifier = holderSet.unwrapKey().map(TagKey<Item>::location).orElseThrow {
            error("Holder Set $holderSet has no tag key")
        }
    }
}

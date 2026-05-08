package hiiragi283.core.api.recipe.result

import com.mojang.serialization.Codec
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.serialization.codec.listOrElement
import hiiragi283.core.api.serialization.network.listOf
import hiiragi283.core.util.HTShapelessRecipeHelper
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

@JvmInline
value class HTListItemResult(val results: List<HTItemResult>) : Iterable<ItemStack> {
    companion object {
        @JvmStatic
        fun codec(maxSize: Int): Codec<HTListItemResult> = HTItemResult.CODEC.listOrElement(1, maxSize).xmap(::HTListItemResult, HTListItemResult::results)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTListItemResult> =
            HTItemResult.STREAM_CODEC.listOf().map(::HTListItemResult, HTListItemResult::results)
    }

    constructor(vararg results: HTItemResult) : this(results.toList())

    constructor(results: Sequence<HTItemResult>) : this(results.toList())

    override fun iterator(): Iterator<ItemStack> = results
        .map { result: HTItemResult ->
            result.get().mapOrElse(identity()) {
                HiiragiCoreAPI.LOGGER.warn(it.string)
                ItemStack.EMPTY
            }
        }.let(HTShapelessRecipeHelper::mergeStacks)
        .iterator()
}

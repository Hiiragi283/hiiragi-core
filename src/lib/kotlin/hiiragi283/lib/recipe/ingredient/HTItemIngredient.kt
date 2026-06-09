package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.unwrap
import net.minecraft.core.TypedInstance
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.DisplayContentsFactory
import net.neoforged.neoforge.common.crafting.SizedIngredient

@JvmInline
value class HTItemIngredient(@PublishedApi internal val delegate: SizedIngredient) : HTIngredient<Item, ItemStack> {
    companion object {
        @JvmStatic
        private val NESTED_CODEC: Codec<HTItemIngredient> = HTCodecs.record { instance ->
            instance.group(
                Ingredient.CODEC.fieldOf(HTConstants.ITEMS).forGetter(HTItemIngredient::unsized),
                HTCodecs.POSITIVE_INT.fieldOf(HTConstants.COUNT).forGetter(HTItemIngredient::count),
            ).apply(instance, ::HTItemIngredient)
        }

        @JvmStatic
        private val SIMPLE_CODEC: Codec<HTItemIngredient> = Ingredient.CODEC.xmap({ HTItemIngredient(it, 1) }, HTItemIngredient::unsized)

        @JvmField
        val CODEC: Codec<HTItemIngredient> = HTCodecs.either(SIMPLE_CODEC, NESTED_CODEC).xmap(
            { it.unwrap() },
            { ingredient: HTItemIngredient ->
                when (ingredient.count) {
                    1 -> Either.Left(ingredient)
                    else -> Either.Right(ingredient)
                }
            },
        )

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemIngredient> = SizedIngredient.STREAM_CODEC.map(::HTItemIngredient, HTItemIngredient::delegate)
    }

    constructor(ingredient: Ingredient, count: Int) : this(SizedIngredient(ingredient, count))

    inline val unsized: Ingredient get() = delegate.ingredient()
    inline val count: Int get() = delegate.count()

    override fun test(instance: TypedInstance<Item>): Boolean = HTIngredientHelper.unwrap(instance).fold(::testOnlyType, delegate::test)

    override fun testOnlyType(instance: TypedInstance<Item>): Boolean = HTIngredientHelper.createStack(instance).let(unsized::test)

    override fun getRequiredAmount(instance: TypedInstance<Item>): Int = when (testOnlyType(instance)) {
        true -> count
        false -> 0
    }

    override fun getPreviewStacks(contextMap: ContextMap): List<ItemStack> = unsized
        .display()
        .resolve(contextMap, DisplayContentsFactory.ForStacks { it.copyWithCount(count) })
        .toList()
}

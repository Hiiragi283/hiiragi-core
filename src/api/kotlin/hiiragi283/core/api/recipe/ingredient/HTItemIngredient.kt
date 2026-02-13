package hiiragi283.core.api.recipe.ingredient

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient

/**
 * [HTItemResourceType]向けに[HTIngredient]を実装したクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
class HTItemIngredient(val unsized: Ingredient, override val amount: Int) : HTIngredient<Item, HTItemResourceType> {
    companion object {
        @JvmField
        val UNSIZED_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> =
            VanillaBiCodecs.INGREDIENT.xmap({ HTItemIngredient(it, 1) }, HTItemIngredient::unsized)

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = BiCodec.composite(
            VanillaBiCodecs.INGREDIENT.fieldOf(HTConst.ITEMS).forGetter(HTItemIngredient::unsized),
            BiCodecs.NON_NEGATIVE_INT.optionalFieldOf(HTConst.AMOUNT, 0).forGetter(HTItemIngredient::amount),
            ::HTItemIngredient,
        )
    }

    fun test(stack: ItemStack): Boolean {
        val resource: HTItemResourceType = stack.toResource() ?: return false
        return test(resource, stack.count)
    }

    fun testOnlyType(stack: ItemStack): Boolean = stack.toResource()?.let(::testOnlyType) ?: false

    //    HTIngredientN    //

    override fun testOnlyType(resource: HTItemResourceType): Boolean = unsized.test(resource.toStack())

    override fun unwrap(): Either<TagKey<Item>, List<HTItemResourceType>> {
        val custom: ICustomIngredient? = unsized.customIngredient
        if (custom != null) {
            return Either.Right(custom.items.toList().mapNotNull(ItemStack::toResource))
        } else {
            val values: Array<Ingredient.Value> = unsized.values
            return when (values.size) {
                0 -> Either.Right(listOf())
                1 -> {
                    when (val value: Ingredient.Value = values[0]) {
                        is Ingredient.TagValue -> Either.Left(value.tag())
                        else -> Either.Right(value.items.mapNotNull(ItemStack::toResource))
                    }
                }
                else -> Either.Right(values.flatMap(Ingredient.Value::getItems).mapNotNull(ItemStack::toResource))
            }
        }
    }
}

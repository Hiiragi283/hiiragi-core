package hiiragi283.core.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.monad.unwrapEither
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
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.function.IntUnaryOperator

/**
 * [HTItemResourceType]向けに[HTIngredient]を実装したクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@JvmInline
value class HTItemIngredient(val delegate: SizedIngredient) : HTIngredient<Item, HTItemResourceType> {
    companion object {
        /**
         * 個数を無視した[HTItemIngredient]の[BiCodec]
         */
        @JvmField
        val UNSIZED_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> =
            VanillaBiCodecs.INGREDIENT.xmap(::HTItemIngredient, HTItemIngredient::ingredient)

        @JvmStatic
        private val NESTED_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = BiCodec.composite(
            VanillaBiCodecs.INGREDIENT.fieldOf(HTConst.ITEMS).forGetter(HTItemIngredient::ingredient),
            BiCodecs.POSITIVE_INT.fieldOf(HTConst.AMOUNT).forGetter(HTItemIngredient::getRequiredAmount),
            ::HTItemIngredient,
        )

        /**
         * [HTItemIngredient]の[BiCodec]
         */
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemIngredient> = BiCodecs
            .xor(UNSIZED_CODEC, NESTED_CODEC)
            .xmap(::unwrapEither) { ingredient: HTItemIngredient ->
                when (ingredient.getRequiredAmount()) {
                    1 -> Either.left(ingredient)
                    else -> Either.right(ingredient)
                }
            }
    }

    constructor(ingredient: Ingredient, count: Int = 1) : this(SizedIngredient(ingredient, count))

    val ingredient: Ingredient get() = delegate.ingredient()

    fun test(stack: ItemStack): Boolean {
        val resource: HTItemResourceType = stack.toResource() ?: return false
        return test(resource, stack.count)
    }

    fun testOnlyType(stack: ItemStack): Boolean = stack.toResource()?.let(::testOnlyType) ?: false

    fun copyWithCount(operator: IntUnaryOperator): HTItemIngredient =
        HTItemIngredient(this.ingredient, operator.applyAsInt(getRequiredAmount()))

    override fun testOnlyType(resource: HTItemResourceType): Boolean = ingredient.test(resource.toStack())

    override fun getRequiredAmount(): Int = delegate.count()

    override fun unwrap(): Either<TagKey<Item>, List<HTItemResourceType>> {
        val custom: ICustomIngredient? = ingredient.customIngredient
        if (custom != null) {
            return Either.right(custom.items.toList().mapNotNull(ItemStack::toResource))
        } else {
            val values: Array<Ingredient.Value> = ingredient.values
            return when (values.size) {
                0 -> Either.right(listOf())
                1 -> {
                    when (val value: Ingredient.Value = values[0]) {
                        is Ingredient.TagValue -> Either.left(value.tag())
                        else -> Either.right(value.items.mapNotNull(ItemStack::toResource))
                    }
                }
                else -> Either.right(values.flatMap(Ingredient.Value::getItems).mapNotNull(ItemStack::toResource))
            }
        }
    }
}

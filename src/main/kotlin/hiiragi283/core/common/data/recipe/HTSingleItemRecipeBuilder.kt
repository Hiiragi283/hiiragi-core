package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTStackRecipeBuilder
import hiiragi283.core.api.math.toFraction
import hiiragi283.core.api.stack.ImmutableItemStack
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTFrostingRecipe
import hiiragi283.core.common.recipe.HTSingleItemRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import org.apache.commons.lang3.math.Fraction
import kotlin.math.max

class HTSingleItemRecipeBuilder(prefix: String, private val factory: Factory<*>, stack: ImmutableItemStack) :
    HTStackRecipeBuilder.Single<HTSingleItemRecipeBuilder>(prefix, stack) {
    companion object {
        @JvmStatic
        fun drying(stack: ImmutableItemStack): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.DRYING, ::HTDryingRecipe, stack)

        @JvmStatic
        fun frosting(stack: ImmutableItemStack): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.FROSTING, ::HTFrostingRecipe, stack)

        @JvmStatic
        fun drying(item: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.DRYING, ::HTDryingRecipe, item, count)

        @JvmStatic
        fun frosting(item: ItemLike, count: Int = 1): HTSingleItemRecipeBuilder =
            HTSingleItemRecipeBuilder(HTConst.FROSTING, ::HTFrostingRecipe, item, count)
    }

    constructor(prefix: String, factory: Factory<*>, item: ItemLike, count: Int) : this(prefix, factory, ImmutableItemStack.of(item, count))

    private var group: String? = null
    private var time: Int = 20 * 10
    private var exp: Fraction = Fraction.ZERO

    fun setGroup(group: String?): HTSingleItemRecipeBuilder = apply {
        this.group = group
    }

    fun setTime(time: Int): HTSingleItemRecipeBuilder = apply {
        this.time = max(0, time)
    }

    fun setExp(exp: Float): HTSingleItemRecipeBuilder = setExp(exp.toFraction())

    fun setExp(exp: Fraction): HTSingleItemRecipeBuilder = apply {
        this.exp = maxOf(Fraction.ZERO, exp)
    }

    override fun createRecipe(output: ItemStack): HTSingleItemRecipe = factory.create(group ?: "", ingredient, output, time, exp)

    fun interface Factory<RECIPE : HTSingleItemRecipe> {
        fun create(
            group: String,
            ingredient: Ingredient,
            result: ItemStack,
            time: Int,
            exp: Fraction,
        ): RECIPE
    }
}

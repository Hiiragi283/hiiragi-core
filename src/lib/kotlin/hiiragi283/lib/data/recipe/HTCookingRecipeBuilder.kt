@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe

/**
 * @see net.minecraft.data.recipes.SimpleCookingRecipeBuilder
 */
class HTCookingRecipeBuilder(
    private val factory: AbstractCookingRecipe.Factory<*>,
    private val timeOperator: Identity<Int>,
    prefix: String,
) : HTRecipeBuilder<AbstractCookingRecipe>(prefix) {
    companion object {
        @JvmStatic
        inline fun smelting(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::SmeltingRecipe, identity(), HTConstants.SMELTING).apply(builderAction)
        }

        @JvmStatic
        inline fun blasting(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::BlastingRecipe, identity(), HTConstants.BLASTING).apply(builderAction)
        }

        @JvmStatic
        inline fun smoking(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::SmokingRecipe, identity(), HTConstants.SMOKING).apply(builderAction)
        }

        @JvmStatic
        inline fun smeltingAndBlasting(builderAction: HTCookingRecipeBuilder.() -> Unit): Sequence<HTCookingRecipeBuilder> = sequenceOf(
            smelting(builderAction),
            HTCookingRecipeBuilder(::BlastingRecipe, { it / 2 }, HTConstants.BLASTING).apply(builderAction),
        )

        @JvmStatic
        inline fun smeltingAndSmoking(builderAction: HTCookingRecipeBuilder.() -> Unit): Sequence<HTCookingRecipeBuilder> = sequenceOf(
            smelting(builderAction),
            HTCookingRecipeBuilder(::SmokingRecipe, { it / 2 }, HTConstants.SMOKING).apply(builderAction),
        )
    }

    var group: String? = null
    var category: CookingBookCategory = CookingBookCategory.MISC

    @PublishedApi internal var ingredient: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: ItemStackTemplate by HTDelegates.onceInitialize()
    var exp: Float = 0f
    var time: Int = 20 * 10

    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    operator fun ItemStackTemplate.unaryPlus() {
        result = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).build()
    }

    //    HTRecipeBuilder    //

    override fun getPrimalId(): Identifier = result.item().getKeyOrThrow().identifier()

    override fun createRecipe(): AbstractCookingRecipe = factory.create(
        commonInfo(true),
        AbstractCookingRecipe.CookingBookInfo(category, group ?: ""),
        ingredient,
        result,
        exp,
        timeOperator(time),
    )
}

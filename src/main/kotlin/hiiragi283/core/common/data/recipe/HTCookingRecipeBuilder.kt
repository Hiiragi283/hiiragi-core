@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.item.ItemInstanceBuilder
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.Identity
import hiiragi283.core.api.util.identity
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.BlastingRecipe
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.SmokingRecipe

/**
 * 精錬レシピ向けの[HTRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - SimpleCookingRecipeBuilder][net.minecraft.data.recipes.SimpleCookingRecipeBuilder]
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
class HTCookingRecipeBuilder(
    private val factory: AbstractCookingRecipe.Factory<*>,
    private val timeOperator: Identity<Int>,
    prefix: String,
) : HTRecipeBuilder<AbstractCookingRecipe>(prefix) {
    companion object {
        /**
         * かまどレシピのみを生成します。
         */
        @JvmStatic
        inline fun smelting(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::SmeltingRecipe, identity(), HTConst.SMELTING).apply(builderAction)
        }

        /**
         * 溶鉱炉レシピのみを生成します。
         */
        @JvmStatic
        inline fun blasting(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::BlastingRecipe, identity(), HTConst.BLASTING).apply(builderAction)
        }

        /**
         * 燻製器レシピのみを生成します。
         */
        @JvmStatic
        inline fun smoking(builderAction: HTCookingRecipeBuilder.() -> Unit): HTCookingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCookingRecipeBuilder(::SmokingRecipe, identity(), HTConst.SMOKING).apply(builderAction)
        }

        /**
         * かまどレシピと溶鉱炉レシピを生成します。後者の処理時間は前者の半分で登録されます。
         */
        @JvmStatic
        inline fun smeltingAndBlasting(builderAction: HTCookingRecipeBuilder.() -> Unit): Sequence<HTCookingRecipeBuilder> = sequenceOf(
            smelting(builderAction),
            HTCookingRecipeBuilder(::BlastingRecipe, { it / 2 }, HTConst.BLASTING).apply(builderAction),
        )

        /**
         * かまどレシピと燻製器レシピを生成します。後者の処理時間は前者の半分で登録されます。
         */
        @JvmStatic
        inline fun smeltingAndSmoking(builderAction: HTCookingRecipeBuilder.() -> Unit): Sequence<HTCookingRecipeBuilder> = sequenceOf(
            smelting(builderAction),
            HTCookingRecipeBuilder(::SmokingRecipe, { it / 2 }, HTConst.SMOKING).apply(builderAction),
        )
    }

    /**
     * レシピ本のカテゴリ
     */
    var category: CookingBookCategory = CookingBookCategory.MISC

    /**
     * レシピ本でのグループ
     */
    var group: String? = null

    @PublishedApi internal var ingredient: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: ItemStack by HTDelegates.onceInitialize()

    /**
     * 精錬時にもらえる経験値量
     */
    var exp: Float = 0f

    /**
     * 精錬に必要な時間
     *
     * デフォルトは200 ticks = 10 sec
     */
    var time: Int = 20 * 10

    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    operator fun ItemStack.unaryPlus() {
        result = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildStack(builderAction)
    }

    //    HTRecipeBuilder    //

    override fun getPrimalId(): ResourceLocation = result.itemHolder.getKeyOrThrow().location()

    override fun createRecipe(): AbstractCookingRecipe = factory.create(
        group ?: "",
        category,
        ingredient,
        result,
        exp,
        timeOperator(time),
    )
}

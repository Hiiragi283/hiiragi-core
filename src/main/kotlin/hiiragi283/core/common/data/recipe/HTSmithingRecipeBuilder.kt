@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.common.data.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.HTRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.item.ItemInstanceBuilder
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SmithingTransformRecipe

class HTSmithingRecipeBuilder : HTRecipeBuilder<SmithingTransformRecipe>(HTConst.SMITHING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTSmithingRecipeBuilder.() -> Unit): HTSmithingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTSmithingRecipeBuilder().apply(builderAction)
        }
    }

    var template: Ingredient by HTDelegates.onceInitialize()
    var base: Ingredient by HTDelegates.onceInitialize()
    var addition: Ingredient by HTDelegates.onceInitialize()
    var result: ItemStack by HTDelegates.onceInitialize()

    operator fun ItemStack.unaryPlus() {
        result = this
    }

    inline fun template(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        template = IngredientBuilder().apply(builderAction).build()
    }

    inline fun base(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        base = IngredientBuilder().apply(builderAction).build()
    }

    inline fun addition(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        addition = IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: ItemInstanceBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = ItemInstanceBuilder.buildStack(builderAction)
    }

    override fun getPrimalId(): ResourceLocation = result.itemHolder.getKeyOrThrow().location()

    override fun createRecipe(): SmithingTransformRecipe = SmithingTransformRecipe(template, base, addition, result)
}

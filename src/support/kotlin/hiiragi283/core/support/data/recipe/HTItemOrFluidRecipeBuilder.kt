@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.support.data.recipe

import hiiragi283.core.api.data.recipe.FluidIngredientBuilder
import hiiragi283.core.api.data.recipe.HTFluidResultBuilder
import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.api.util.Ior
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.some
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTItemOrFluidRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    @PublishedApi internal var itemIngredient: Option<HTItemIngredient> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var fluidIngredient: Option<HTFluidIngredient> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var itemResult: Option<HTItemResult> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var fluidResult: Option<HTFluidResult> by HTDelegates.onceInitialize { Option.none() }

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredient = this.some()
    }

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this.some()
    }

    operator fun HTItemResult.unaryPlus() {
        itemResult = this.some()
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResult = this.some()
    }

    inline fun itemIngredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun fluidIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +FluidIngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder().apply(builderAction).build()
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder().apply(builderAction).build()
    }

    private fun toIorResult(): Ior<HTItemResult, HTFluidResult> = Ior.fromOption(itemResult, fluidResult).getOrNull() ?: error("Either item or fluid result required")

    override fun getPrimalId(): ResourceLocation = toIorResult().swap().map(HTFluidResult::getId, HTItemResult::getId)

    override fun createRecipe(): RECIPE = factory.create(
        Ior.fromOption(itemIngredient, fluidIngredient).getOrNull() ?: error("Either item or fluid ingredient required"),
        toIorResult(),
        progressData,
    )

    //    Factory    //

    fun interface Factory<out RECIPE : Any> {
        fun create(ingredient: Ior<HTItemIngredient, HTFluidIngredient>, result: Ior<HTItemResult, HTFluidResult>, progressData: HTProgressData): RECIPE
    }
}

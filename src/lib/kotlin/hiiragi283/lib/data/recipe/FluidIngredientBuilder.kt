@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.HolderSetBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.identity
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderSet
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.SimpleFluidIngredient

@HTBuilderMarker
class FluidIngredientBuilder {
    private var contents: Either<FluidIngredient, HolderSet<Fluid>> by HTDelegates.onceInitialize()
    var amount: Int = FluidType.BUCKET_VOLUME

    operator fun FluidIngredient.unaryPlus() {
        when (this) {
            is SimpleFluidIngredient -> +this.fluidSet()
            else -> contents = Either.Left(this)
        }
    }

    operator fun HolderSet<Fluid>.unaryPlus() {
        contents = Either.Right(this)
    }

    inline fun holderSet(builderAction: HolderSetBuilder<Fluid>.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderSetBuilder<Fluid>().apply(builderAction).build()
    }

    inline fun fluids(builderAction: FluidSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +FluidSetBuilder().apply(builderAction).build()
    }

    @HTBuilderMarker
    class FluidSetBuilder {
        private val fluids: MutableSet<Fluid> = mutableSetOf()

        operator fun Fluid.unaryPlus() {
            fluids += this
        }

        @Suppress("DEPRECATION")
        fun build(): HolderSet<Fluid> = HolderSet.direct(Fluid::builtInRegistryHolder, fluids)
    }

    fun build(): FluidIngredient = contents.fold(identity(), FluidIngredient::of)

    fun buildSized(): HTFluidIngredient = HTFluidIngredient(build(), amount)
}

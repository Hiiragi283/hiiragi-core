@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.HolderAccepter
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

    inline fun fluids(builderAction: HolderAccepter.FluidSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAccepter.FluidSetBuilder().apply(builderAction).build()
    }

    fun build(): FluidIngredient = contents.fold(identity(), FluidIngredient::of)

    fun buildSized(): HTFluidIngredient = HTFluidIngredient(build(), amount)
}

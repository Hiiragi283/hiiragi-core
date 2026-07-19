@file:OptIn(ExperimentalContracts::class)

package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.data.HolderAcceptor
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.util.HTBuilderMarker
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * [FluidIngredient]および[HTFluidIngredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@HTBuilderMarker
class FluidIngredientBuilder {
    private var ingredient: FluidIngredient by HTDelegates.onceInitialize()
    var amount: Int = FluidType.BUCKET_VOLUME

    operator fun FluidIngredient.unaryPlus() {
        ingredient = this
    }

    operator fun Fluid.unaryPlus() {
        +FluidIngredient.of(this)
    }

    operator fun TagKey<Fluid>.unaryPlus() {
        +FluidIngredient.tag(this)
    }

    operator fun HTFluidContent.unaryPlus() {
        +this.fluidTag
    }

    inline fun fluids(builderAction: HolderAcceptor.FluidSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAcceptor.FluidSetBuilder()
            .apply(builderAction)
            .build()
            .map { it.value() }
            .map(FluidIngredient::of)
            .let { CompoundFluidIngredient(it) }
    }

    fun water() {
        +VanillaFluidContents.WATER
    }

    fun lava() {
        +VanillaFluidContents.LAVA
    }

    fun milk() {
        +VanillaFluidContents.MILK
    }

    fun build(): FluidIngredient = ingredient

    fun buildSized(): HTFluidIngredient = HTFluidIngredient(ingredient, amount)
}

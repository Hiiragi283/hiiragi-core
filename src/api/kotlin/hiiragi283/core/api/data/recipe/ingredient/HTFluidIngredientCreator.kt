package hiiragi283.core.api.data.recipe.ingredient

import hiiragi283.core.api.registry.HTFluidContent
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentExactPredicate
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient
import net.neoforged.neoforge.fluids.crafting.DataComponentFluidIngredient
import net.neoforged.neoforge.fluids.crafting.DifferenceFluidIngredient
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.IntersectionFluidIngredient

interface HTFluidIngredientCreator : HTIngredientCreator.Registered<Fluid, FluidIngredient> {
    fun fromFluid(content: HTFluidContent): FluidIngredient = fromTagKey(content.fluidTag)

    fun water(): FluidIngredient = fromTagKey(Tags.Fluids.WATER)

    fun lava(): FluidIngredient = fromTagKey(Tags.Fluids.LAVA)

    fun milk(): FluidIngredient = fromTagKey(Tags.Fluids.MILK)

    // HolderSet
    override fun fromHolderSet(holderSet: HolderSet<Fluid>): FluidIngredient = FluidIngredient.of(holderSet)

    // Custom
    override fun allOf(ingredients: List<FluidIngredient>): FluidIngredient = IntersectionFluidIngredient(ingredients)

    override fun anyOf(ingredients: List<FluidIngredient>): FluidIngredient = CompoundFluidIngredient(ingredients)

    override fun difference(base: FluidIngredient, subtracted: FluidIngredient): FluidIngredient =
        DifferenceFluidIngredient(base, subtracted)

    fun dataComponents(holderSet: HolderSet<Fluid>, components: DataComponentExactPredicate, strict: Boolean): FluidIngredient =
        DataComponentFluidIngredient(holderSet, components, strict)
}

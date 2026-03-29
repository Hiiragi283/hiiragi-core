package hiiragi283.core.impl.data.recipe.ingredient

import hiiragi283.core.api.data.recipe.ingredient.HTFluidIngredientCreator
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

internal class HTFluidIngredientCreatorImpl(getter: HolderGetter<Fluid>) :
    HTRegisteredIngredientCreatorImpl<Fluid, FluidIngredient>(getter),
    HTFluidIngredientCreator {
    @Suppress("DEPRECATION")
    override fun getHolder(type: Fluid): Holder<Fluid> = type.builtInRegistryHolder()
}

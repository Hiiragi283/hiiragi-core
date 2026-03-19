package hiiragi283.core.api.data.recipe.ingredient

import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

interface HTFluidIngredientCreator : HTIngredientCreator.Registered<Fluid, FluidIngredient> {
    fun water(): FluidIngredient = fromTagKey(Tags.Fluids.WATER)

    fun lava(): FluidIngredient = fromTagKey(Tags.Fluids.LAVA)

    fun milk(): FluidIngredient = fromTagKey(Tags.Fluids.MILK)
}

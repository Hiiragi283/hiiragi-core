package hiiragi283.core.api.recipe.base.factory

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.neoforge.fluids.FluidStack

interface HTItemAndFluidRecipeFactory<INPUT : RecipeInput> : HTRecipeFactory<INPUT, Ior<ItemStack, FluidStack>>

package hiiragi283.core.api.recipe.cache

import hiiragi283.core.impl.recipe.cache.HTDoubleInputRecipeCache
import hiiragi283.core.impl.recipe.cache.HTSingleInputRecipeCache
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.BiPredicate
import java.util.function.Predicate

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 * @see mekanism.common.recipe.lookup.cache.InputRecipeCache
 */
data object HTRecipeCaches {
    //    Single Input    //

    class SingleFluid<RECIPE : Predicate<FluidStack>>(lookup: HTRecipeLookup<RECIPE>) :
        HTSingleInputRecipeCache<FluidStack, RECIPE>(lookup) {
        override fun isEmpty(input: FluidStack): Boolean = input.isEmpty
    }

    class SingleItem<RECIPE : Predicate<ItemStack>>(lookup: HTRecipeLookup<RECIPE>) :
        HTSingleInputRecipeCache<ItemStack, RECIPE>(lookup) {
        override fun isEmpty(input: ItemStack): Boolean = input.isEmpty
    }

    //    Double Input    //

    class ItemAndFluid<RECIPE : BiPredicate<ItemStack, FluidStack>>(lookup: HTRecipeLookup<RECIPE>) :
        HTDoubleInputRecipeCache<ItemStack, FluidStack, RECIPE>(lookup) {
        override fun isEmpty(firstInput: ItemStack, secondInput: FluidStack): Boolean = firstInput.isEmpty || secondInput.isEmpty
    }

    class ItemOrFluid<RECIPE : BiPredicate<ItemStack, FluidStack>>(lookup: HTRecipeLookup<RECIPE>) :
        HTDoubleInputRecipeCache<ItemStack, FluidStack, RECIPE>(lookup) {
        override fun isEmpty(firstInput: ItemStack, secondInput: FluidStack): Boolean = firstInput.isEmpty && secondInput.isEmpty
    }

    class DoubleItem<RECIPE : BiPredicate<ItemStack, ItemStack>>(lookup: HTRecipeLookup<RECIPE>) :
        HTDoubleInputRecipeCache<ItemStack, ItemStack, RECIPE>(lookup) {
        override fun isEmpty(firstInput: ItemStack, secondInput: ItemStack): Boolean = firstInput.isEmpty || secondInput.isEmpty
    }
}

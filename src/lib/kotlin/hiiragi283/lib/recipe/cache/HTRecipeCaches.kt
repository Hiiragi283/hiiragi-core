package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.world.item.ItemInstance
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 * @see mekanism.common.recipe.lookup.cache.InputRecipeCache
 */
data object HTRecipeCaches {
    //    Single Input    //

    class SingleFluid<RECIPE : Predicate<FluidInstance>>(lookup: HTRecipeLookup<RECIPE>) : HTSingleInputRecipeCache<FluidInstance, RECIPE>(lookup) {
        override fun isEmpty(input: FluidInstance): Boolean = HTIngredientHelper.isEmpty(input)
    }

    class SingleItem<RECIPE : Predicate<ItemInstance>>(lookup: HTRecipeLookup<RECIPE>) : HTSingleInputRecipeCache<ItemInstance, RECIPE>(lookup) {
        override fun isEmpty(input: ItemInstance): Boolean = HTIngredientHelper.isEmpty(input)
    }

    //    Double Input    //

    class ItemAndFluid<RECIPE : BiPredicate<ItemInstance, FluidInstance>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<ItemInstance, FluidInstance, RECIPE>(lookup) {
        override fun isEmpty(firstInput: ItemInstance, secondInput: FluidInstance): Boolean = HTIngredientHelper.isEmpty(firstInput) || HTIngredientHelper.isEmpty(secondInput)
    }

    class ItemOrFluid<RECIPE : BiPredicate<ItemInstance, FluidInstance>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<ItemInstance, FluidInstance, RECIPE>(lookup) {
        override fun isEmpty(firstInput: ItemInstance, secondInput: FluidInstance): Boolean = HTIngredientHelper.isEmpty(firstInput) && HTIngredientHelper.isEmpty(secondInput)
    }

    class DoubleItem<RECIPE : BiPredicate<ItemInstance, ItemInstance>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<ItemInstance, ItemInstance, RECIPE>(lookup) {
        override fun isEmpty(firstInput: ItemInstance, secondInput: ItemInstance): Boolean = HTIngredientHelper.isEmpty(firstInput) || HTIngredientHelper.isEmpty(secondInput)
    }
}

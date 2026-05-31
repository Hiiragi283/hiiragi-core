package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

/**
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 * @see mekanism.common.recipe.lookup.cache.InputRecipeCache
 */
data object HTRecipeCaches {
    //    Single Input    //

    class SingleFluid<RECIPE : Predicate<TypedInstance<Fluid>>>(lookup: HTRecipeLookup<RECIPE>) : HTSingleInputRecipeCache<TypedInstance<Fluid>, RECIPE>(lookup) {
        override fun isEmpty(input: TypedInstance<Fluid>): Boolean = HTIngredientHelper.isEmpty(input)
    }

    class SingleItem<RECIPE : Predicate<TypedInstance<Item>>>(lookup: HTRecipeLookup<RECIPE>) : HTSingleInputRecipeCache<TypedInstance<Item>, RECIPE>(lookup) {
        override fun isEmpty(input: TypedInstance<Item>): Boolean = HTIngredientHelper.isEmpty(input)
    }

    //    Double Input    //

    class ItemAndFluid<RECIPE : BiPredicate<TypedInstance<Item>, TypedInstance<Fluid>>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<TypedInstance<Item>, TypedInstance<Fluid>, RECIPE>(lookup) {
        override fun isEmpty(firstInput: TypedInstance<Item>, secondInput: TypedInstance<Fluid>): Boolean = HTIngredientHelper.isEmpty(firstInput) || HTIngredientHelper.isEmpty(secondInput)
    }

    class ItemOrFluid<RECIPE : BiPredicate<TypedInstance<Item>, TypedInstance<Fluid>>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<TypedInstance<Item>, TypedInstance<Fluid>, RECIPE>(lookup) {
        override fun isEmpty(firstInput: TypedInstance<Item>, secondInput: TypedInstance<Fluid>): Boolean = HTIngredientHelper.isEmpty(firstInput) && HTIngredientHelper.isEmpty(secondInput)
    }

    class DoubleItem<RECIPE : BiPredicate<TypedInstance<Item>, TypedInstance<Item>>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<TypedInstance<Item>, TypedInstance<Item>, RECIPE>(lookup) {
        override fun isEmpty(firstInput: TypedInstance<Item>, secondInput: TypedInstance<Item>): Boolean = HTIngredientHelper.isEmpty(firstInput) || HTIngredientHelper.isEmpty(secondInput)
    }
}

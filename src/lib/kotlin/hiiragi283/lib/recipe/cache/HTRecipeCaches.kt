package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import java.util.function.BiPredicate
import java.util.function.Predicate
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

/**
 * Hiiragi Seriesで使用されるレシピキャッシュをまとめたクラスです。
 *
 * 参照 : [Mekanism - InputRecipeCache](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/recipe/lookup/cache/InputRecipeCache.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTRecipeCaches {
    //    Single Input    //

    /**
     * 1種類の液体を入力とするレシピキャッシュのクラスです。
     */
    class SingleFluid<RECIPE : Predicate<TypedInstance<Fluid>>>(lookup: HTRecipeLookup<RECIPE>) : HTSingleInputRecipeCache<TypedInstance<Fluid>, RECIPE>(lookup) {
        override fun isEmpty(input: TypedInstance<Fluid>): Boolean = HTIngredientHelper.isEmpty(input)
    }

    /**
     * 1種類のアイテムを入力とするレシピキャッシュのクラスです。
     */
    class SingleItem<RECIPE : Predicate<TypedInstance<Item>>>(lookup: HTRecipeLookup<RECIPE>) : HTSingleInputRecipeCache<TypedInstance<Item>, RECIPE>(lookup) {
        override fun isEmpty(input: TypedInstance<Item>): Boolean = HTIngredientHelper.isEmpty(input)
    }

    //    Double Input    //

    /**
     * 1種類のアイテムと液体を入力とするレシピキャッシュのクラスです。
     */
    class ItemAndFluid<RECIPE : BiPredicate<TypedInstance<Item>, TypedInstance<Fluid>>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<TypedInstance<Item>, TypedInstance<Fluid>, RECIPE>(lookup) {
        override fun isEmpty(firstInput: TypedInstance<Item>, secondInput: TypedInstance<Fluid>): Boolean = HTIngredientHelper.isEmpty(firstInput) || HTIngredientHelper.isEmpty(secondInput)
    }

    /**
     * 1種類のアイテムと液体を入力とするレシピキャッシュのクラスです。
     *
     * アイテムと液体の両方を要求します。
     */
    class ItemOrFluid<RECIPE : BiPredicate<TypedInstance<Item>, TypedInstance<Fluid>>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<TypedInstance<Item>, TypedInstance<Fluid>, RECIPE>(lookup) {
        override fun isEmpty(firstInput: TypedInstance<Item>, secondInput: TypedInstance<Fluid>): Boolean = HTIngredientHelper.isEmpty(firstInput) && HTIngredientHelper.isEmpty(secondInput)
    }

    /**
     * 2種類のアイテムを入力とするレシピキャッシュのクラスです。
     */
    class DoubleItem<RECIPE : BiPredicate<TypedInstance<Item>, TypedInstance<Item>>>(lookup: HTRecipeLookup<RECIPE>) : HTDoubleInputRecipeCache<TypedInstance<Item>, TypedInstance<Item>, RECIPE>(lookup) {
        override fun isEmpty(firstInput: TypedInstance<Item>, secondInput: TypedInstance<Item>): Boolean = HTIngredientHelper.isEmpty(firstInput) || HTIngredientHelper.isEmpty(secondInput)
    }
}

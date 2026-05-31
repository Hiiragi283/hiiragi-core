package hiiragi283.core.api.recipe

import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid

/**
 * 空の容器に液体を汲み入れるレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankFillingRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<ItemStack> {
    fun testContainer(instance: TypedInstance<Item>): Boolean

    fun testFluid(instance: TypedInstance<Fluid>): Boolean

    override fun test(first: TypedInstance<Item>, second: TypedInstance<Fluid>): Boolean = testContainer(first) && testFluid(second)
}

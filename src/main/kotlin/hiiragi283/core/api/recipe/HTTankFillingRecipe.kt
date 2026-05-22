package hiiragi283.core.api.recipe

import hiiragi283.lib.recipe.base.HTRecipeFactories
import hiiragi283.lib.recipe.base.HTRecipePredicates
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidInstance

/**
 * 空の容器に液体を汲み入れるレシピを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.1
 */
interface HTTankFillingRecipe :
    HTRecipePredicates.ItemAndFluid,
    HTRecipeFactories.ItemAndFluid<ItemStack> {
    fun testContainer(instance: ItemInstance): Boolean

    fun testFluid(instance: FluidInstance): Boolean

    override fun test(first: ItemInstance, second: FluidInstance): Boolean = testContainer(first) && testFluid(second)
}

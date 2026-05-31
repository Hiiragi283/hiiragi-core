package hiiragi283.core.common.recipe.custom

import hiiragi283.core.api.recipe.HTTankFillingRecipe
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.util.flatMap
import hiiragi283.lib.util.right
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidType

data object HTPotionArrowFillingRecipe : HTTankFillingRecipe {
    const val FLUID_AMOUNT: Int = FluidType.BUCKET_VOLUME / 8

    override fun testContainer(instance: TypedInstance<Item>): Boolean = instance.`is`(Items.ARROW)

    override fun testFluid(instance: TypedInstance<Fluid>): Boolean {
        val contents: BottledPotionContents = HTIngredientHelper.createStack(instance).let(HTPotionHelper::getContents) ?: return false
        return !contents.isEmpty && contents.bottleType == HTBottleType.LINGERING
    }

    override fun getRequiredAmount(first: TypedInstance<Item>, second: TypedInstance<Fluid>): Pair<Int, Int> = when {
        test(first, second) -> 1 to FLUID_AMOUNT
        else -> 0 to 0
    }

    override fun assemble(firstInput: ItemInstance, secondInput: FluidInstance): ItemStack = HTPotionHelper.getContents(secondInput)
        ?.right()
        ?.map(BottledPotionContents::contents)
        ?.flatMap { HTPotionHelper.createPotion(Items.TIPPED_ARROW, it) }
        ?.map(ItemStackTemplate::create)
        ?.getOrNull()
        ?: ItemStack.EMPTY
}

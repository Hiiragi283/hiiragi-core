package hiiragi283.core.common.recipe.custom

import hiiragi283.core.api.recipe.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.HTTankFillingRecipe
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.lib.item.alchemy.BottledPotionContents
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.item.createOrEmpty
import hiiragi283.lib.recipe.ingredient.HTIngredientHelper
import hiiragi283.lib.recipe.result.HTItemAndFluidResult
import net.minecraft.core.TypedInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidInstance
import net.neoforged.neoforge.fluids.FluidType

data object HTPotionTankInteraction {
    const val FLUID_AMOUNT: Int = FluidType.BUCKET_VOLUME / 4

    data object Emptying : HTTankEmptyingRecipe {
        override fun test(input: TypedInstance<Item>): Boolean = HTBottleType.getBottleType(input) != null

        override fun getRequiredAmount(input: TypedInstance<Item>): Int = when {
            test(input) -> 1
            else -> 0
        }

        override fun assemble(input: ItemInstance): HTItemAndFluidResult {
            val contents: BottledPotionContents = HTPotionHelper.getContentsFromBottle(input) ?: return HTItemAndFluidResult.EMPTY
            return HTItemAndFluidResult.create(
                ItemStack(Items.GLASS_BOTTLE),
                HCPotionFluidHelper.createFluid(contents, FLUID_AMOUNT),
            )
        }
    }

    data object Filling : HTTankFillingRecipe {
        override fun testContainer(instance: TypedInstance<Item>): Boolean = instance.`is`(Items.GLASS_BOTTLE)

        override fun testFluid(instance: TypedInstance<Fluid>): Boolean = HTIngredientHelper.createStack(instance).let(HTPotionHelper::getContents) != null

        override fun getRequiredAmount(first: TypedInstance<Item>, second: TypedInstance<Fluid>): Pair<Int, Int> = when {
            test(first, second) -> 1 to FLUID_AMOUNT
            else -> 0 to 0
        }

        override fun assemble(firstInput: ItemInstance, secondInput: FluidInstance): ItemStack = HTPotionHelper
            .getContents(secondInput)
            ?.let(HTPotionHelper::createPotion)
            .createOrEmpty()
    }
}

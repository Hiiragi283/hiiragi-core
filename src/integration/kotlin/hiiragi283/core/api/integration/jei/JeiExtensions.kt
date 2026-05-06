package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.text.HTCommonTranslation
import mezz.jei.api.gui.builder.IIngredientConsumer
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

typealias JeiRecipeType<T> = RecipeType<T>

fun <T : IIngredientConsumer> T.addFluidStack(stack: FluidStack): T = apply {
    this.addFluidStack(stack.fluid, stack.amount.toLong(), stack.componentsPatch)
}

fun <T : IIngredientConsumer> T.addFluidStacks(stacks: Iterable<FluidStack>): T = apply {
    this.addIngredients(NeoForgeTypes.FLUID_STACK, stacks.toList())
}

fun <T : IIngredientConsumer> T.addChancedItem(stack: HTRecipeContents.ChancedItemStack?): T {
    val (stack: ItemStack, chance: Float) = stack ?: return this
    this.addItemStack(stack)
    if (this is IRecipeSlotBuilder) {
        if (chance < 1f) {
            this.addRichTooltipCallback { _, builder: ITooltipBuilder ->
                builder.add(HTCommonTranslation.CHANCE_PRODUCE.translateColored(HTDefaultColor.YELLOW, chance * 100))
            }
        }
    }
    return this
}

package hiiragi283.lib.integration.jei

import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.recipe.viewer.display.HTRecipeContents
import hiiragi283.lib.text.HTCommonTranslation
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

fun <T : IIngredientAcceptor<T>> T.add(stack: FluidStack): T = apply {
    this.add(stack.fluid, stack.amount.toLong(), stack.componentsPatch)
}

fun <T : IIngredientAcceptor<T>> T.add(stacks: Iterable<FluidStack>): T = apply {
    this.addIngredients(NeoForgeTypes.FLUID_STACK, stacks.toList())
}

fun <T : IIngredientAcceptor<T>> T.add(stack: HTRecipeContents.ChancedItemStack?): T {
    val (stack: ItemStack, chance: Float) = stack ?: return this
    this.add(stack)
    if (this is IRecipeSlotBuilder) {
        if (chance < 1f) {
            this.addRichTooltipCallback { _, builder: ITooltipBuilder ->
                builder.add(HTCommonTranslation.CHANCE_PRODUCE.translateColored(HTDefaultColor.YELLOW, chance * 100))
            }
        }
    }
    return this
}

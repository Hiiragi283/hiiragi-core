package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.text.HTCommonTranslation
import mezz.jei.api.gui.builder.IIngredientConsumer
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.RecipeType
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.IntSupplier

typealias JeiRecipeType<T> = RecipeType<T>

fun <T : IIngredientConsumer> T.addFluidStack(stack: FluidStack): T = apply {
    this.addFluidStack(stack.fluid, stack.amount.toLong(), stack.componentsPatch)
}

fun <T : IRecipeSlotBuilder> T.setFluidSlotRenderer(): T = apply {
    this.setFluidRenderer(1000, false, 16, 16)
}

fun <T : IRecipeSlotBuilder> T.setTankRenderer(capacity: IntSupplier): T = this.setTankRenderer(capacity.asInt)

fun <T : IRecipeSlotBuilder> T.setTankRenderer(capacity: Int): T = apply {
    this.setFluidRenderer(capacity.toLong(), false, 16, 18 * 3 - 2)
}

fun <T : IIngredientConsumer> T.addFluidStacks(stacks: Iterable<FluidStack>): T = apply {
    this.addIngredients(NeoForgeTypes.FLUID_STACK, stacks.toList())
}

fun <T : IIngredientConsumer> T.addChancedItem(stack: HTRecipeContents.ChancedItemStack): T {
    this.addItemStack(stack.stack)
    if (this is IRecipeSlotBuilder) {
        val chance: Float = stack.chance
        if (chance < 100f) {
            this.addRichTooltipCallback { _, builder: ITooltipBuilder ->
                builder.add(HTCommonTranslation.CHANCE_PRODUCE.translateColored(HTDefaultColor.YELLOW, chance))
            }
        }
    }
    return this
}

package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.unwrap
import hiiragi283.core.api.text.HTCommonTranslation
import mezz.jei.api.gui.builder.IIngredientAcceptor
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import net.minecraft.ChatFormatting
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

// Item
fun <T : IIngredientAcceptor<T>> T.add(stack: FluidStack): T = this.add(stack.fluid, stack.amount.toLong(), stack.componentsPatch)

fun <T : IIngredientAcceptor<T>> T.add(ingredient: SizedIngredient): T {
    val count: Int = ingredient.count()
    return ingredient
        .unwrap()
        .map(
            { holderSet: HolderSet<Item> -> holderSet.map { ItemStack(it, count) } },
            { resources: List<ItemResource> -> resources.map { it.toStack(count) } },
        ).let(this::addItemStacks)
}

fun <T : IIngredientAcceptor<T>> T.add(result: HTItemResult): T {
    if (this is IRecipeSlotBuilder) {
        this.addRichTooltipCallback { _, builder: ITooltipBuilder ->
            builder.add(HTCommonTranslation.CHANCE_PRODUCE.translateColored(ChatFormatting.YELLOW, result.chance * 100))
        }
    }
    return this.add(result.create())
}

// Fluid
fun <T : IIngredientAcceptor<T>> T.addFluidStacks(stacks: List<FluidStack>): T = this.addIngredients(NeoForgeTypes.FLUID_STACK, stacks)

fun <T : IIngredientAcceptor<T>> T.add(ingredient: SizedFluidIngredient): T {
    val amount: Int = ingredient.amount()
    return ingredient
        .unwrap()
        .map(
            { holderSet: HolderSet<Fluid> -> holderSet.map { FluidStack(it, amount) } },
            { resources: List<FluidResource> -> resources.map { it.toStack(amount) } },
        ).let(this::addFluidStacks)
}

fun <T : IIngredientAcceptor<T>> T.add(result: HTFluidResult): T = this.add(result.create())

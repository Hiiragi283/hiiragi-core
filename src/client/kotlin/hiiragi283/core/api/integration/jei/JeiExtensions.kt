package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.compareTo
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.times
import mezz.jei.api.gui.builder.IIngredientConsumer
import mezz.jei.api.gui.builder.IRecipeSlotBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.neoforge.NeoForgeTypes
import mezz.jei.api.recipe.RecipeType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

typealias JeiRecipeType<T> = RecipeType<T>

fun <T : IIngredientConsumer> T.addFluidStack(stack: FluidStack, setRenderer: Boolean = true): T {
    this.addFluidStack(stack.fluid, stack.amount.toLong(), stack.componentsPatch)
    if (setRenderer && this is IRecipeSlotBuilder) {
        this.setFluidRenderer(stack.amount.toLong(), false, 16, 16)
    }
    return this
}

fun <T : IIngredientConsumer> T.addFluidStacks(stacks: Iterable<FluidStack>, setRenderer: Boolean = true): T {
    this.addIngredients(NeoForgeTypes.FLUID_STACK, stacks.toList())
    if (setRenderer && this is IRecipeSlotBuilder) {
        val capacity: Long = (stacks.maxOfOrNull(FluidStack::getAmount) ?: HTConst.DEFAULT_FLUID_AMOUNT).toLong()
        this.setFluidRenderer(capacity, false, 16, 16)
    }
    return this
}

private fun createError(message: Text): ItemStack = createItemStack(
    Items.BARRIER,
    DataComponents.CUSTOM_NAME,
    message,
)

// Item
fun <T : IIngredientConsumer> T.addItemIngredient(ingredient: HTItemIngredient?): T {
    if (ingredient == null) return this
    val amount: Int = when {
        ingredient.isCatalyst -> 1
        else -> ingredient.amount
    }
    ingredient
        .unwrap()
        .map(
            { tagKey: TagKey<Item> ->
                BuiltInRegistries.ITEM
                    .getTagOrEmpty(tagKey)
                    .map { ItemStack(it, amount) }
                    .takeUnless(List<ItemStack>::isEmpty)
                    ?: listOf(createError(HTCommonTranslation.EMPTY_TAG_KEY.translate(tagKey.location)))
            },
            { resources: List<HTItemResourceType> ->
                resources
                    .map { it.toStack(amount) }
                    .takeUnless(List<ItemStack>::isEmpty)
                    ?: listOf(createError(HTCommonTranslation.EMPTY.translate()))
            },
        ).let { this.addItemStacks(it) }
    if (ingredient.isCatalyst && this is IRecipeSlotBuilder) {
        this.addRichTooltipCallback { _, builder: ITooltipBuilder ->
            builder.add(HTCommonTranslation.CHANCE_CONSUME.translateColored(HTDefaultColor.YELLOW, 0))
        }
    }
    return this
}

fun <T : IIngredientConsumer> T.addItemResult(result: HTItemResult?): T {
    this.addItemStacks(listOfNotNull(result?.getStackResult(null, false)?.mapOrElse(identity(), ::createError)))
    if (result != null && this is IRecipeSlotBuilder) {
        val chance: Fraction = result.chance
        if (result.chance < 1f) {
            this.addRichTooltipCallback { _, builder: ITooltipBuilder ->
                builder.add(HTCommonTranslation.CHANCE_PRODUCE.translateColored(HTDefaultColor.YELLOW, chance * 100))
            }
        }
    }
    return this
}

// Fluid
fun <T : IIngredientConsumer> T.addFluidIngredient(ingredient: HTFluidIngredient?, setRenderer: Boolean = true): T {
    if (ingredient == null) return this
    val amount: Int = when {
        ingredient.isCatalyst -> 1
        else -> ingredient.amount
    }
    ingredient
        .unwrap()
        .map(
            { tagKey: TagKey<Fluid> -> BuiltInRegistries.FLUID.getTagOrEmpty(tagKey).map { FluidStack(it, amount) } },
            { resources: List<HTFluidResourceType> -> resources.map { it.toStack(amount) } },
        ).let { this.addFluidStacks(it, setRenderer) }
    if (ingredient.isCatalyst && this is IRecipeSlotBuilder) {
        this.addRichTooltipCallback { _, builder: ITooltipBuilder ->
            builder.add(HTCommonTranslation.CHANCE_CONSUME.translateColored(HTDefaultColor.YELLOW, 0))
        }
    }
    return this
}

fun <T : IIngredientConsumer> T.addFluidResult(result: HTFluidResult?, setRenderer: Boolean = true): T =
    this.addFluidStacks(listOfNotNull(result?.getStackResult(null)?.value()), setRenderer)

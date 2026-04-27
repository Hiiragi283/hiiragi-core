package hiiragi283.core.api.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.times
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

@JvmRecord
data class HTRecipeContents(
    private val inputItems: List<List<ItemStack>>,
    private val inputFluids: List<List<FluidStack>>,
    private val catalysts: List<List<ItemStack>>,
    private val outputItems: List<ChancedItemStack>,
    private val outputFluids: List<FluidStack>,
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRecipeContents> = RecordCodecBuilder.mapCodec { instance ->
            val itemsCodec: Codec<List<ItemStack>> = ItemStack.CODEC.listOf()
            val fluidsCodec: Codec<List<FluidStack>> = FluidStack.CODEC.listOf()

            instance
                .group(
                    itemsCodec.listOf().fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTRecipeContents::inputItems),
                    fluidsCodec.listOf().fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HTRecipeContents::inputFluids),
                    itemsCodec.listOf().fieldOf(HTConst.CATALYST).forGetter(HTRecipeContents::catalysts),
                    ChancedItemStack.LIST_CODEC.fieldOf(HTConst.ITEM_RESULT).forGetter(HTRecipeContents::outputItems),
                    fluidsCodec.fieldOf(HTConst.FLUID_RESULT).forGetter(HTRecipeContents::outputFluids),
                ).apply(instance, ::HTRecipeContents)
        }

        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTRecipeContents = Builder().apply(builderAction).build()
    }

    /**
     * 指定した[インデックス][index]に対応する[材料][List]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[emptyList]
     */
    fun inputItem(index: Int): List<ItemStack> = inputItems.getOrNull(index) ?: emptyList()

    /**
     * 指定した[インデックス][index]に対応する[材料][List]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[emptyList]
     */
    fun inputFluid(index: Int): List<FluidStack> = inputFluids.getOrNull(index) ?: emptyList()

    /**
     * 指定した[インデックス][index]に対応する[触媒][List]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[emptyList]
     */
    fun catalyst(index: Int): List<ItemStack> = catalysts.getOrNull(index) ?: emptyList()

    /**
     * 指定した[インデックス][index]に対応する[完成品のプレビュー][ChancedItemStack]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[ChancedItemStack.EMPTY]
     */
    fun outputItem(index: Int): ChancedItemStack = outputItems.getOrNull(index) ?: ChancedItemStack.EMPTY

    /**
     * 指定した[インデックス][index]に対応する[完成品のプレビュー][FluidStack]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[FluidStack.EMPTY]
     */
    fun outputFluid(index: Int): FluidStack = outputFluids.getOrNull(index) ?: FluidStack.EMPTY

    //    Builder    //

    class Builder {
        private val inputItems: MutableList<List<ItemStack>> = mutableListOf()
        private val inputFluids: MutableList<List<FluidStack>> = mutableListOf()
        private val catalysts: MutableList<List<ItemStack>> = mutableListOf()
        private val outputItems: MutableList<ChancedItemStack> = mutableListOf()
        private val outputFluids: MutableList<FluidStack> = mutableListOf()

        //    Input    //

        // Item
        fun addInput(stack: ItemStack) {
            addInput(listOf(stack))
        }

        @JvmName("addItemInput")
        fun addInput(stacks: List<ItemStack>?) {
            inputItems += stacks?.filterNot(ItemStack::isEmpty) ?: emptyList()
        }

        fun addInput(ingredient: Ingredient?) {
            ingredient?.items?.toList()?.let(::addInput)
        }

        fun addInput(ingredient: HTItemIngredient?) {
            ingredient?.getPreviewStacks()?.let(::addInput)
        }

        // Fluid
        @JvmName("addFluidInput")
        fun addInput(stacks: List<FluidStack>?) {
            inputFluids += stacks?.filterNot(FluidStack::isEmpty) ?: emptyList()
        }

        fun addInput(ingredient: FluidIngredient?) {
            ingredient?.stacks?.toList()?.let(::addInput)
        }

        fun addInput(ingredient: HTFluidIngredient?) {
            ingredient?.getPreviewStacks()?.let(::addInput)
        }

        //    Catalyst    //

        fun addCatalyst(stacks: List<ItemStack>?) {
            catalysts += stacks?.filterNot(ItemStack::isEmpty) ?: emptyList()
        }

        fun addCatalyst(ingredient: Ingredient?) {
            ingredient?.items?.toList()?.let(::addCatalyst)
        }

        fun addCatalyst(ingredient: HTItemIngredient?) {
            ingredient?.getPreviewStacks()?.let(::addCatalyst)
        }

        //    Output    //

        // Item
        @JvmName("addItemOutput")
        fun addOutput(stack: ItemStack, chance: Float = 1f) {
            outputItems += ChancedItemStack(stack, chance)
        }

        fun addOutput(result: HTItemResult) {
            addOutput(result.get(true).valueOrElse(::createError), (result.chance * 100).toFloat())
        }

        private fun createError(message: Text): ItemStack = createItemStack(Items.BARRIER, DataComponents.CUSTOM_NAME, message)

        // Fluid
        @JvmName("addFluidOutput")
        fun addOutput(stack: FluidStack) {
            outputFluids += stack
        }

        fun addOutput(result: HTFluidResult) {
            addOutput(result.getOrEmpty())
        }

        fun build(): HTRecipeContents = HTRecipeContents(inputItems, inputFluids, catalysts, outputItems, outputFluids)
    }

    @JvmRecord
    data class ChancedItemStack(val stack: ItemStack, val chance: Float) {
        companion object {
            @JvmField
            val CODEC: Codec<ChancedItemStack> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        MapCodec.assumeMapUnsafe(ItemStack.OPTIONAL_CODEC).forGetter(ChancedItemStack::stack),
                        Codec.FLOAT.fieldOf(HTConst.CHANCE).forGetter(ChancedItemStack::chance),
                    ).apply(instance, ::ChancedItemStack)
            }

            @JvmField
            val LIST_CODEC: Codec<List<ChancedItemStack>> = CODEC.listOf()

            @JvmField
            val EMPTY = ChancedItemStack(ItemStack.EMPTY, 0f)
        }
    }
}

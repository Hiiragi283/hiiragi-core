package hiiragi283.lib.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.createItemStack
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.ErrorText
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.none
import hiiragi283.lib.util.some
import hiiragi283.lib.util.unwrap
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.SlotDisplayContext
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.display.FluidStackContentsFactory

@JvmRecord
data class HTRecipeContents(
    private val inputItems: List<List<ItemStack>>,
    private val inputFluids: List<FluidInput>,
    private val catalysts: List<List<ItemStack>>,
    private val outputItems: List<Option<ChancedItemStack>>,
    private val outputFluids: List<FluidStack>,
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRecipeContents> = RecordCodecBuilder.mapCodec { instance ->
            val itemsCodec: Codec<List<ItemStack>> = ItemStack.CODEC.listOf()

            instance
                .group(
                    itemsCodec.listOf().fieldOf(HTConstants.ITEM_INGREDIENT).forGetter(HTRecipeContents::inputItems),
                    FluidInput.CODEC
                        .listOf()
                        .fieldOf(HTConstants.FLUID_INGREDIENT)
                        .forGetter(HTRecipeContents::inputFluids),
                    itemsCodec.listOf().fieldOf(HTConstants.CATALYST).forGetter(HTRecipeContents::catalysts),
                    HTCodecs
                        .option(ChancedItemStack.CODEC)
                        .listOf()
                        .fieldOf(HTConstants.ITEM_RESULT)
                        .forGetter(HTRecipeContents::outputItems),
                    FluidStack.CODEC
                        .listOf()
                        .fieldOf(HTConstants.FLUID_RESULT)
                        .forGetter(HTRecipeContents::outputFluids),
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
     * 指定した[インデックス][index]に対応する[材料][FluidInput]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[FluidInput.EMPTY]
     */
    fun inputFluid(index: Int): FluidInput = inputFluids.getOrNull(index) ?: FluidInput.EMPTY

    inline fun inputFluid(index: Int, action: (FluidInput) -> Unit) {
        inputFluid(index).let(action)
    }

    /**
     * 指定した[インデックス][index]に対応する[触媒][List]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[emptyList]
     */
    fun catalyst(index: Int): List<ItemStack> = catalysts.getOrNull(index) ?: emptyList()

    /**
     * 指定した[インデックス][index]に対応する[完成品のプレビュー][ChancedItemStack]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は`null`
     */
    fun outputItem(index: Int): ChancedItemStack? = outputItems.getOrNull(index)?.getOrNull()

    /**
     * 指定した[インデックス][index]に対応する[完成品のプレビュー][FluidStack]を取得します。
     * @return 指定した[インデックス][index]が範囲外の場合は[FluidStack.EMPTY]
     */
    fun outputFluid(index: Int): FluidStack = outputFluids.getOrNull(index) ?: FluidStack.EMPTY

    inline fun outputFluid(index: Int, action: (FluidStack) -> Unit) {
        outputFluid(index).let(action)
    }

    //    Builder    //

    class Builder {
        private val inputItems: MutableList<List<ItemStack>> = mutableListOf()
        private val inputFluids: MutableList<FluidInput> = mutableListOf()
        private val catalysts: MutableList<List<ItemStack>> = mutableListOf()
        private val outputItems: MutableList<Option<ChancedItemStack>> = mutableListOf()
        private val outputFluids: MutableList<FluidStack> = mutableListOf()

        private val contextMap: ContextMap by lazy { Minecraft.getInstance().level?.let(SlotDisplayContext::fromLevel) ?: ContextMap.EMPTY }

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
            ingredient?.display()?.resolveForStacks(contextMap).let(::addInput)
        }

        fun addInput(ingredient: HTItemIngredient?) {
            ingredient?.getPreviewStacks(contextMap)?.let(::addInput)
        }

        // Fluid
        @JvmName("addFluidInput")
        fun addInput(stacks: List<FluidStack>?) {
            inputFluids += stacks?.let(FluidInput::create) ?: FluidInput.EMPTY
        }

        fun addInput(ingredient: FluidIngredient?) {
            ingredient?.display()?.resolve(contextMap, FluidStackContentsFactory.INSTANCE)?.toList().let(::addInput)
        }

        fun addInput(ingredient: HTFluidIngredient?) {
            ingredient?.getPreviewStacks(contextMap)?.let(::addInput)
        }

        //    Catalyst    //

        fun addCatalyst(stacks: List<ItemStack>?) {
            catalysts += stacks?.filterNot(ItemStack::isEmpty) ?: emptyList()
        }

        fun addCatalyst(ingredient: Ingredient?) {
            ingredient?.display()?.resolveForStacks(contextMap).let(::addInput)
        }

        fun addCatalyst(ingredient: HTItemIngredient?) {
            ingredient?.getPreviewStacks(contextMap)?.let(::addCatalyst)
        }

        //    Output    //

        // Item
        @JvmName("addItemOutput")
        fun addOutput(stack: ItemStack, chance: Float = 1f) {
            outputItems += when {
                stack.isEmpty -> none()
                else -> ChancedItemStack(stack, chance).some()
            }
        }

        fun addOutput(result: HTItemResult) {
            result.create()
                .mapLeft(::createError)
                .unwrap()
                .let(::addOutput)
        }

        fun addOutput(result: HTChancedItemResult) {
            result.create(true)
                .mapLeft(::createError)
                .unwrap()
                .let { addOutput(it, result.chance.toFloat()) }
        }

        private fun createError(errorText: ErrorText): ItemStack = createItemStack(Items.BARRIER, DataComponents.CUSTOM_NAME, errorText.getText())

        // Fluid
        @JvmName("addFluidOutput")
        fun addOutput(stack: FluidStack) {
            outputFluids += stack
        }

        fun addOutput(result: HTFluidResult) {
            addOutput(result.create())
        }

        fun build(): HTRecipeContents = HTRecipeContents(inputItems, inputFluids, catalysts, outputItems, outputFluids)
    }

    @ConsistentCopyVisibility
    @JvmRecord
    data class FluidInput private constructor(val stacks: List<FluidStack>, val capacity: Int) {
        companion object {
            @JvmField
            val CODEC: Codec<FluidInput> = FluidStack.CODEC.listOf().xmap(::create, FluidInput::stacks)

            @JvmField
            val EMPTY = FluidInput(emptyList(), FluidType.BUCKET_VOLUME)

            @JvmStatic
            fun create(stacks: List<FluidStack>): FluidInput = when {
                stacks.isEmpty() || stacks.all(FluidStack::isEmpty) -> EMPTY
                else -> FluidInput(stacks, stacks.maxOf(FluidStack::getAmount))
            }
        }
    }

    @JvmRecord
    data class ChancedItemStack(val stack: ItemStack, val chance: Float) {
        companion object {
            @JvmField
            val CODEC: Codec<ChancedItemStack> = RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        MapCodec.assumeMapUnsafe(ItemStack.CODEC).forGetter(ChancedItemStack::stack),
                        Codec.FLOAT.fieldOf(HTConstants.CHANCE).forGetter(ChancedItemStack::chance),
                    ).apply(instance, ::ChancedItemStack)
            }
        }
    }
}

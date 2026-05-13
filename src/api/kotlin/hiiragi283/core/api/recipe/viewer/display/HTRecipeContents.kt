package hiiragi283.core.api.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.text.toText
import net.minecraft.core.component.DataComponents
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@JvmRecord
data class HTRecipeContents(
    private val inputItems: List<List<ItemStack>>,
    private val inputFluids: List<FluidInput>,
    private val catalysts: List<List<ItemStack>>,
    private val outputItems: List<Optional<ChancedItemStack>>,
    private val outputFluids: List<FluidStack>,
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTRecipeContents> = RecordCodecBuilder.mapCodec { instance ->
            val itemsCodec: Codec<List<ItemStack>> = ItemStack.CODEC.listOf()

            instance
                .group(
                    itemsCodec.listOf().fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HTRecipeContents::inputItems),
                    FluidInput.CODEC
                        .listOf()
                        .fieldOf(HTConst.FLUID_INGREDIENT)
                        .forGetter(HTRecipeContents::inputFluids),
                    itemsCodec.listOf().fieldOf(HTConst.CATALYST).forGetter(HTRecipeContents::catalysts),
                    ExtraCodecs
                        .optionalEmptyMap(ChancedItemStack.CODEC)
                        .listOf()
                        .fieldOf(HTConst.ITEM_RESULT)
                        .forGetter(HTRecipeContents::outputItems),
                    FluidStack.CODEC
                        .listOf()
                        .fieldOf(HTConst.FLUID_RESULT)
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
        private val outputItems: MutableList<Optional<ChancedItemStack>> = mutableListOf()
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
            inputFluids += stacks?.let(FluidInput::create) ?: FluidInput.EMPTY
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
            outputItems += when {
                stack.isEmpty -> Optional.empty()
                else -> Optional.of(ChancedItemStack(stack, chance))
            }
        }

        fun addOutput(result: HTItemResult) {
            result.create()
                .mapOrElse(identity()) { error: DataResult.Error<ItemStack> -> error.message().let(::createError) }
                .let { addOutput(it) }
        }

        fun addOutput(result: HTChancedItemResult) {
            result.create(true)
                .mapOrElse(identity()) { error: DataResult.Error<ItemStack> -> error.message().let(::createError) }
                .let { addOutput(it, result.chance.toFloat()) }
        }

        private fun createError(message: String): ItemStack = createItemStack(Items.BARRIER, DataComponents.CUSTOM_NAME, message.toText())

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
            val EMPTY = FluidInput(emptyList(), HTConst.DEFAULT_FLUID_AMOUNT)

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
                        Codec.FLOAT.fieldOf(HTConst.CHANCE).forGetter(ChancedItemStack::chance),
                    ).apply(instance, ::ChancedItemStack)
            }
        }
    }
}

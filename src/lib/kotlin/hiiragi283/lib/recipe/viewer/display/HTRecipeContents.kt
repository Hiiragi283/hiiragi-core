@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.item.ItemInstanceBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTChancedItemResult
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.ErrorText
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.Option
import hiiragi283.lib.util.some
import hiiragi283.lib.util.unwrap
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.SlotDisplayContext
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import net.neoforged.neoforge.fluids.crafting.display.FluidStackContentsFactory

/**
 * レシピビューワーで表示する材料と完成品を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
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
        val CODEC: MapCodec<HTRecipeContents> = HTCodecs.recordMap { instance ->
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

        /**
         * 新しい[HTRecipeContents]のインスタンスを作成します。
         * @param builderAction [HTRecipeContents.Builder]を初期化するブロック
         */
        @JvmStatic
        inline fun create(builderAction: Builder.() -> Unit): HTRecipeContents {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return Builder().apply(builderAction).build()
        }
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
        contract {
            callsInPlace(action, InvocationKind.EXACTLY_ONCE)
        }
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
        contract {
            callsInPlace(action, InvocationKind.EXACTLY_ONCE)
        }
        outputFluid(index).let(action)
    }

    //    Builder    //

    /**
     * [HTRecipeContents]のビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @HTBuilderMarker
    class Builder {
        private val inputItems: MutableList<List<ItemStack>> = mutableListOf()
        private val inputFluids: MutableList<FluidInput> = mutableListOf()
        private val catalysts: MutableList<List<ItemStack>> = mutableListOf()
        private val outputItems: MutableList<Option<ChancedItemStack>> = mutableListOf()
        private val outputFluids: MutableList<FluidStack> = mutableListOf()

        private val contextMap: ContextMap by lazy { Minecraft.getInstance().level?.let(SlotDisplayContext::fromLevel) ?: ContextMap.EMPTY }

        //    Input    //

        // Item
        /**
         * アイテムの材料を追加します。
         */
        fun addInput(stack: ItemStack?) {
            addInput(listOfNotNull(stack))
        }

        /**
         * アイテムの材料を追加します。
         */
        @JvmName("addItemInput")
        fun addInput(stacks: List<ItemStack>?) {
            inputItems += stacks?.filterNot(ItemStack::isEmpty) ?: emptyList()
        }

        /**
         * アイテムの材料を追加します。
         */
        fun addInput(ingredient: Ingredient?) {
            ingredient?.display()?.resolveForStacks(contextMap).let(::addInput)
        }

        /**
         * アイテムの材料を追加します。
         */
        fun addInput(ingredient: HTItemIngredient?) {
            ingredient?.getPreviewStacks(contextMap)?.let(::addInput)
        }

        // Fluid
        /**
         * 液体の材料を追加します。
         * @since 26.1.2
         */
        fun addInput(stack: FluidStack?) {
            addInput(listOfNotNull(stack))
        }

        /**
         * 液体の材料を追加します。
         */
        @JvmName("addFluidInput")
        fun addInput(stacks: List<FluidStack>?) {
            inputFluids += stacks?.let(FluidInput::create) ?: FluidInput.EMPTY
        }

        /**
         * 液体の材料を追加します。
         */
        fun addInput(ingredient: FluidIngredient?) {
            ingredient?.display()?.resolve(contextMap, FluidStackContentsFactory.INSTANCE)?.toList().let(::addInput)
        }

        /**
         * 液体の材料を追加します。
         */
        fun addInput(ingredient: HTFluidIngredient?) {
            ingredient?.getPreviewStacks(contextMap)?.let(::addInput)
        }

        //    Catalyst    //

        /**
         * 触媒を追加します。
         * @since 26.1.2
         */
        fun addCatalyst(stack: ItemStack?) {
            addCatalyst(listOfNotNull(stack))
        }

        /**
         * 触媒を追加します。
         */
        fun addCatalyst(stacks: List<ItemStack>?) {
            catalysts += stacks?.filterNot(ItemStack::isEmpty) ?: emptyList()
        }

        /**
         * 触媒を追加します。
         */
        fun addCatalyst(ingredient: Ingredient?) {
            ingredient?.display()?.resolveForStacks(contextMap).let(::addInput)
        }

        /**
         * 触媒を追加します。
         */
        fun addCatalyst(ingredient: HTItemIngredient?) {
            ingredient?.getPreviewStacks(contextMap)?.let(::addCatalyst)
        }

        //    Output    //

        // Item
        /**
         * アイテムの完成品を追加します。
         */
        @JvmName("addItemOutput")
        fun addOutput(stack: ItemStack, chance: Float = 1f) {
            outputItems += when {
                stack.isEmpty -> Option.none()
                else -> ChancedItemStack(stack, chance).some()
            }
        }

        /**
         * アイテムの完成品を追加します。
         */
        @JvmName("addItemOutput")
        fun addOutput(template: ItemStackTemplate?, chance: Float = 1f) {
            outputItems += when {
                template == null -> Option.none()
                else -> ChancedItemStack(template.create(), chance).some()
            }
        }

        /**
         * アイテムの完成品を追加します。
         */
        fun addOutput(result: HTItemResult) {
            result.create()
                .mapLeft(::createError)
                .unwrap()
                .let(::addOutput)
        }

        /**
         * アイテムの完成品を追加します。
         */
        fun addOutput(result: HTChancedItemResult) {
            result.create(true)
                .mapLeft(::createError)
                .unwrap()
                .let { addOutput(it, result.chance.toFloat()) }
        }

        private fun createError(errorText: ErrorText): ItemStack = ItemInstanceBuilder.buildStack {
            +Items.BARRIER
            components { set(DataComponents.CUSTOM_NAME, errorText.getText()) }
        }

        // Fluid
        /**
         * 液体の完成品を追加します。
         */
        @JvmName("addFluidOutput")
        fun addOutput(stack: FluidStack) {
            outputFluids += stack
        }

        /**
         * 液体の完成品を追加します。
         */
        fun addOutput(result: HTFluidResult) {
            addOutput(result.create())
        }

        fun build(): HTRecipeContents = HTRecipeContents(inputItems, inputFluids, catalysts, outputItems, outputFluids)
    }

    /**
     * 液体の材料を管理するクラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
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

    /**
     * アイテムの完成品を管理するクラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class ChancedItemStack(val stack: ItemStack, val chance: Float) {
        companion object {
            @JvmField
            val CODEC: Codec<ChancedItemStack> = HTCodecs.record { instance ->
                instance
                    .group(
                        MapCodec.assumeMapUnsafe(ItemStack.CODEC).forGetter(ChancedItemStack::stack),
                        Codec.FLOAT.fieldOf(HTConstants.CHANCE).forGetter(ChancedItemStack::chance),
                    ).apply(instance, ::ChancedItemStack)
            }
        }
    }
}

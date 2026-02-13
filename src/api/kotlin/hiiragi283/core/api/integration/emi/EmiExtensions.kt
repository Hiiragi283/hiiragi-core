package hiiragi283.core.api.integration.emi

import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

//    EmiStack    //

// Mutable Stack

/**
 * この[アイテム][this]を[EmiStack]に変換します。
 * @param amount スタックの個数
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ItemLike.toEmi(amount: Int = 1): EmiStack = EmiStack.of(this, amount.toLong())

/**
 * この[ItemStack][this]を[EmiStack]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ItemStack.toEmi(): EmiStack = EmiStack.of(this)

/**
 * この[液体][this]を[EmiStack]に変換します。
 * @param amount スタックの量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun Fluid.toEmi(amount: Int = 0): EmiStack = EmiStack.of(this, amount.toLong())

/**
 * この[FluidStack][this]を[EmiStack]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun FluidStack.toEmi(): EmiStack = NeoForgeEmiStack.of(this)

// TagKey

/**
 * この[タグ][this]を[EmiIngredient]に変換します。
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun TagKey<*>.toEmi(amount: Int = 1): EmiIngredient = EmiIngredient
    .of(this, amount.toLong())
    .takeUnless(EmiIngredient::isEmpty)
    ?: createErrorStack(HTCommonTranslation.EMPTY_TAG_KEY.translate(this))

// Ingredient

/**
 * この[材料][this]を[EmiIngredient]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTItemIngredient.toEmi(): EmiIngredient {
    val ingredient: EmiIngredient = this.unwrap().map(
        { tagKey: TagKey<Item> ->
            when {
                this.isCatalyst -> tagKey.toEmi().setChance(0f)
                else -> tagKey.toEmi(this.amount)
            }
        },
        { resources: List<HTItemResourceType> ->
            when {
                this.isCatalyst -> resources.map(HTItemResourceType::toStack)
                else -> resources.map { it.toStack(this.amount) }
            }.map(ItemStack::toEmi).let(::ingredient)
        },
    )
    if (this.isCatalyst) {
        ingredient.chance = 0f
    }
    return ingredient
}

/**
 * この[材料][this]を[EmiIngredient]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTFluidIngredient.toEmi(): EmiIngredient {
    val ingredient: EmiIngredient = this.unwrap().map(
        { tagKey: TagKey<Fluid> -> tagKey.toEmi(this.amount) },
        { resources: List<HTFluidResourceType> ->
            resources.map { it.toStack(this.amount) }.map(FluidStack::toEmi).let(::ingredient)
        },
    )
    if (this.isCatalyst) {
        ingredient.chance = 0f
    }
    return ingredient
}

private fun ingredient(stacks: List<EmiStack>): EmiIngredient = when {
    stacks.isEmpty() -> createErrorStack(HTCommonTranslation.EMPTY)
    stacks.size == 1 -> stacks[0]
    else -> EmiIngredient.of(stacks)
}

// Result

/**
 * この[完成品][this]を[EmiStack]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTItemResult.toEmi(): EmiStack = this.getStackResult(null).mapOrElse(ItemStack::toEmi, ::createErrorStack)

/**
 * この[完成品][this]を[EmiStack]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTFluidResult.toEmi(): EmiStack = this.getStackResult(null).mapOrElse(FluidStack::toEmi, ::createErrorStack)

/**
 * この[確率付き完成品][this]を[EmiStack]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun HTChancedItemResult.toEmi(): EmiStack = this.result
    .toEmi()
    .setChance(this.chance.toFloat())

// Fluid Content

/**
 * この[液体][this]を液体の[EmiStack]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTHolderLike<Fluid, *>.toFluidEmi(amount: Int = 0): EmiStack = this.get().toEmi(amount)

/**
 * 指定した[翻訳][translation]からエラーを表す[EmiStack]を作成します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun createErrorStack(translation: HTTranslation): EmiStack = createErrorStack(translation.translate())

/**
 * 指定した[テキスト][message]からエラーを表す[EmiStack]を作成します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun createErrorStack(message: Component): EmiStack = createItemStack(Items.BARRIER, DataComponents.CUSTOM_NAME, message).toEmi()

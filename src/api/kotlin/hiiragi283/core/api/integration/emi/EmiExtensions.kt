package hiiragi283.core.api.integration.emi

import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.SlotWidget
import dev.emi.emi.api.widget.TextureWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidWithTag
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

//    EmiStack    //

fun EmiStack.copyAsCatalyst(): EmiStack = copy().setRemainder(this)

// Mutable Stack

/**
 * この[アイテム][this]を[EmiStack]に変換します。
 * @param amount スタックの個数
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun ItemLike.toEmi(amount: Int = 1): EmiStack = EmiStack.of(this, amount.toLong())

/**
 * この[Holder][this]をアイテムの[EmiStack]に変換します。
 * @param amount スタックの個数
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun Holder<out ItemLike>.toItemEmi(amount: Int = 1): EmiStack = this.value().toEmi(amount)

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

/**
 * この[プレフィックス][this]を[HTPrefixLike.createCommonTagKey]に基づいて[EmiIngredient]に変換します。
 * @param T レジストリの要素のクラス
 * @param key レジストリのキー
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> HTPrefixLike.toEmi(key: RegistryKey<T>, amount: Int = 1): EmiIngredient = this.createCommonTagKey(key).toEmi(amount)

/**
 * この[プレフィックス][this]を[HTPrefixLike.createTagKey]に基づいて[EmiIngredient]に変換します。
 * @param T レジストリの要素のクラス
 * @param key レジストリのキー
 * @param material 対象の素材
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> HTPrefixLike.toEmi(key: RegistryKey<T>, material: HTMaterialLike, amount: Int = 1): EmiIngredient =
    this.createTagKey(key, material).toEmi(amount)

/**
 * この[プレフィックス][this]をアイテムの[EmiIngredient]に変換します。
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTPrefixLike.toItemEmi(amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, amount)

/**
 * この[プレフィックス][this]をアイテムの[EmiIngredient]に変換します。
 * @param material 対象の素材
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTPrefixLike.toItemEmi(material: HTMaterialLike, amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, material, amount)

// Ingredient

/**
 * この[材料][this]を[EmiIngredient]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTItemIngredient.toEmi(): EmiIngredient {
    val count: Int = this.getRequiredAmount()
    return this.unwrap().map(
        { tagKey: TagKey<Item> -> tagKey.toEmi(count) },
        { resources: List<HTItemResourceType> ->
            resources.map { it.toStack(count) }.map(ItemStack::toEmi).let(::ingredient)
        },
    )
}

/**
 * この[材料][this]を[EmiIngredient]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTFluidIngredient.toEmi(): EmiIngredient {
    val count: Int = this.getRequiredAmount()
    return this.unwrap().map(
        { tagKey: TagKey<Fluid> -> tagKey.toEmi(count) },
        { resources: List<HTFluidResourceType> ->
            resources.map { it.toStack(count) }.map(FluidStack::toEmi).let(::ingredient)
        },
    )
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

// Fluid Content

/**
 * この[液体][this]を液体の[EmiStack]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTFluidWithTag<*>.toFluidEmi(amount: Int = 0): EmiStack = this.get().toEmi(amount)

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

//    Widget    //

/**
 * この[範囲][this]をEMIの[範囲][Bounds]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTBounds.toEmi(): Bounds = Bounds(this.x, this.y, this.width, this.height)

fun WidgetHolder.addArrow(x: Int, y: Int): TextureWidget = addFillingArrow(x, y, 2000)

fun WidgetHolder.addArrow(x: Int, y: Int, time: Int): TextureWidget = addFillingArrow(x, y, 50 * time)
    .tooltipText(listOf(HTCommonTranslation.SECONDS.translate(time / 20.0f, time)))

fun WidgetHolder.addBurning(x: Int, y: Int, time: Int) {
    addTexture(EmiTexture.EMPTY_FLAME, x + 2, y + 2)
    addAnimatedTexture(
        EmiTexture.FULL_FLAME,
        x + 2,
        y + 2,
        1000 * time / 20,
        false,
        true,
        true,
    )
}

fun WidgetHolder.addPlus(x: Int, y: Int): TextureWidget = addTexture(EmiTexture.PLUS, x + 3, y + 3)

fun WidgetHolder.setShapeless(x: Int, y: Int): TextureWidget = addTexture(EmiTexture.SHAPELESS, x + 1, y)

fun WidgetHolder.addTank(
    ingredient: EmiIngredient,
    x: Int,
    y: Int,
    capacity: Int = ingredient.amount.toInt(),
): SlotWidget = addTank(ingredient, x, y, 18, 18 * 3, capacity).drawBack(false)

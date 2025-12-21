package hiiragi283.core.client.emi

import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.Bounds
import dev.emi.emi.api.widget.FillingArrowWidget
import dev.emi.emi.api.widget.TextureWidget
import dev.emi.emi.api.widget.WidgetHolder
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.math.HTBounds
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTFluidWithTag
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.stack.ImmutableItemStack
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

//    EmiStack    //

fun EmiStack.copyAsCatalyst(): EmiStack = copy().setRemainder(this)

// Mutable Stack
fun ItemLike.toEmi(amount: Int = 1): EmiStack = EmiStack.of(this, amount.toLong())

fun Holder<out ItemLike>.toItemEmi(amount: Int = 1): EmiStack = this.value().toEmi(amount)

fun ItemStack.toEmi(): EmiStack = EmiStack.of(this)

fun Fluid.toEmi(amount: Int = 0): EmiStack = EmiStack.of(this, amount.toLong())

// Immutable Stack
fun ImmutableItemStack?.toEmi(): EmiStack = when (this) {
    null -> EmiStack.EMPTY
    else -> EmiStack.of(this.type(), this.componentsPatch(), this.amount().toLong())
}

fun ImmutableFluidStack?.toEmi(): EmiStack = when (this) {
    null -> EmiStack.EMPTY
    else -> EmiStack.of(this.type(), this.componentsPatch(), this.amount().toLong())
}

// TagKey

fun TagKey<*>.toEmi(amount: Int = 1): EmiIngredient = EmiIngredient
    .of(this, amount.toLong())
    .takeUnless(EmiIngredient::isEmpty)
    ?: createErrorStack(HTCommonTranslation.EMPTY_TAG_KEY.translate(this))

fun <T : Any> HTPrefixLike.toEmi(key: RegistryKey<T>, amount: Int = 1): EmiIngredient = this.createCommonTagKey(key).toEmi(amount)

fun <T : Any> HTPrefixLike.toEmi(key: RegistryKey<T>, material: HTMaterialLike, amount: Int = 1): EmiIngredient =
    this.createTagKey(key, material).toEmi(amount)

fun HTPrefixLike.toItemEmi(amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, amount)

fun HTPrefixLike.toItemEmi(material: HTMaterialLike, amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, material, amount)

// Ingredient
fun HTItemIngredient.toEmi(): EmiIngredient = this
    .unwrap()
    .map(
        { (tagKey: TagKey<Item>, count: Int) -> tagKey.toEmi(count) },
        { stacks: List<ImmutableItemStack> -> stacks.map(ImmutableItemStack::toEmi).let(::ingredient) },
    )

fun HTFluidIngredient.toEmi(): EmiIngredient = this
    .unwrap()
    .map(
        { (tagKey: TagKey<Fluid>, count: Int) -> tagKey.toEmi(count) },
        { stacks: List<ImmutableFluidStack> -> stacks.map(ImmutableFluidStack::toEmi).let(::ingredient) },
    )

private fun ingredient(stacks: List<EmiStack>): EmiIngredient = when {
    stacks.isEmpty() -> createErrorStack(HTCommonTranslation.EMPTY)
    stacks.size == 1 -> stacks[0]
    else -> EmiIngredient.of(stacks)
}

// Result
fun HTItemResult.toEmi(): EmiStack = this.getStackResult(null).map(ImmutableItemStack::toEmi, ::createErrorStack)

fun HTFluidResult.toEmi(): EmiStack = this.getStackResult(null).map(ImmutableFluidStack::toEmi, ::createErrorStack)

// Fluid Content
fun HTFluidWithTag<*>.toFluidEmi(amount: Int = 0): EmiStack = this.get().toEmi(amount)

fun createErrorStack(translation: HTTranslation): EmiStack = createErrorStack(translation.translate())

fun createErrorStack(message: Component): EmiStack {
    val stack = ItemStack(Items.BARRIER)
    stack.set(DataComponents.CUSTOM_NAME, message)
    return stack.toEmi()
}

//    Widget    //

fun HTBounds.toEmi(): Bounds = Bounds(this.x, this.y, this.width, this.height)

fun WidgetHolder.addArrow(x: Int, y: Int): FillingArrowWidget = addFillingArrow(x, y, 2000)

fun WidgetHolder.addPlus(x: Int, y: Int): TextureWidget = addTexture(EmiTexture.PLUS, x + 3, y + 3)

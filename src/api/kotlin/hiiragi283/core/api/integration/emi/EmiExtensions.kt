package hiiragi283.core.api.integration.emi

import dev.emi.emi.api.neoforge.NeoForgeEmiStack
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTranslation
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
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
 * この[プレフィックス][this]を[HTTagPrefix.createCommonTagKey]に基づいて[EmiIngredient]に変換します。
 * @param T レジストリの要素のクラス
 * @param key レジストリのキー
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> HTTagPrefix.toEmi(key: RegistryKey<T>, amount: Int = 1): EmiIngredient = this.createCommonTagKey(key).toEmi(amount)

/**
 * この[プレフィックス][this]を[HTTagPrefix.createTagKey]に基づいて[EmiIngredient]に変換します。
 * @param T レジストリの要素のクラス
 * @param key レジストリのキー
 * @param material 対象の素材
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T : Any> HTTagPrefix.toEmi(key: RegistryKey<T>, material: HTMaterialLike, amount: Int = 1): EmiIngredient =
    this.createTagKey(key, material).toEmi(amount)

/**
 * この[プレフィックス][this]をアイテムの[EmiIngredient]に変換します。
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTTagPrefix.toItemEmi(amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, amount)

/**
 * この[プレフィックス][this]をアイテムの[EmiIngredient]に変換します。
 * @param material 対象の素材
 * @param amount 材料の量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun HTTagPrefix.toItemEmi(material: HTMaterialLike, amount: Int = 1): EmiIngredient = toEmi(Registries.ITEM, material, amount)

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

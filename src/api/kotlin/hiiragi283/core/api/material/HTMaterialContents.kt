package hiiragi283.core.api.material

import hiiragi283.core.api.collection.Table
import hiiragi283.core.api.fluid.FluidStack
import hiiragi283.core.api.fluid.HTSimpleFluidLike
import hiiragi283.core.api.item.HTSimpleItemLike
import hiiragi283.core.api.item.ItemStack
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.material.part.tagPrefix
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.SimpleBlockItemSupplierWithKey
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.HTTextResult
import hiiragi283.core.api.util.toTextResult
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

interface HTMaterialContents<R : Any, out V> : Table<R, HTMaterialKey, V> {
    /**
     * 指定した[row]と[material]から対応する値を返します。
     * @return 対応する値がない場合は`null`
     */
    operator fun get(row: R, material: HTMaterialLike): V? = this[row, material.asMaterialKey()]

    /**
     * 指定した[row]と[material]から対応する値を返します。
     * @since 21.1.0
     */
    fun getResult(row: R, material: HTMaterialLike): HTTextResult<V> = get(row, material).toTextResult { getErrorMessage(row, material.asMaterialKey()) }

    /**
     * 対応する値がない場合のエラーメッセージを作成します。
     */
    fun getErrorMessage(row: R, material: HTMaterialKey): String

    fun column(material: HTMaterialLike): Map<R, V> = this.column(material.asMaterialKey())

    /**
     * @since 21.1.0
     */
    class BlockEntry(delegate: SimpleBlockItemSupplierWithKey, val isBuiltIn: Boolean) :
        SimpleBlockItemSupplierWithKey by delegate,
        HTIdLike.Translatable,
        HTSimpleItemLike {
        override val translationKey: String get() = get().descriptionId

        override fun getText(): Text = get().name

        override fun asItem(): Item = get().asItem()

        override fun toStack(count: Int, patch: DataComponentPatch): ItemStack = ItemStack(this, count, patch)

        operator fun component1(): ResourceKey<Block> = getKey()

        operator fun component2(): Block = get()

        operator fun component3(): Boolean = isBuiltIn

        override fun toString(): String = "BlockEntry(id=${getId()},isBuiltIn=$isBuiltIn)"
    }

    /**
     * @since 21.1.0
     */
    class FluidEntry(delegate: SimpleSupplierWithKey<Fluid>, val isBuiltIn: Boolean) :
        SimpleSupplierWithKey<Fluid> by delegate,
        HTIdLike.Translatable,
        HTSimpleFluidLike {
        fun getBucketSupplier(): ItemEntry = ItemEntry(get().bucket.toLike(), isBuiltIn)

        override val translationKey: String get() = get().fluidType.descriptionId

        override fun getText(): Text = get().fluidType.description

        override fun asFluid(): Fluid = get()

        override fun toStack(amount: Int, patch: DataComponentPatch): FluidStack = FluidStack(this, amount, patch)

        operator fun component1(): ResourceKey<Fluid> = getKey()

        operator fun component2(): Fluid = get()

        operator fun component3(): Boolean = isBuiltIn

        override fun toString(): String = "FluidEntry(id=${getId()},isBuiltIn=$isBuiltIn)"
    }

    /**
     * @since 21.1.0
     */
    class ItemEntry(delegate: SimpleSupplierWithKey<Item>, val isBuiltIn: Boolean) :
        SimpleSupplierWithKey<Item> by delegate,
        HTIdLike.Translatable,
        HTSimpleItemLike {
        override val translationKey: String get() = get().descriptionId

        override fun getText(): Text = get().description

        override fun asItem(): Item = get()

        override fun toStack(count: Int, patch: DataComponentPatch): ItemStack = ItemStack(this, count, patch)

        operator fun component1(): ResourceKey<Item> = getKey()

        operator fun component2(): Item = get()

        operator fun component3(): Boolean = isBuiltIn

        override fun toString(): String = "ItemEntry(id=${getId()},isBuiltIn=$isBuiltIn)"
    }
}

//    Extensions    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
operator fun <V> HTMaterialContents<HTPart, V>.get(part: HTPartLike, material: HTMaterialLike): V? = this[part.asPart(), material]

/**
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
fun <V> HTMaterialContents<HTPart, V>.getResult(part: HTPartLike, material: HTMaterialLike): HTTextResult<V> = this.getResult(part.asPart(), material)

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
val <V> HTMaterialContents<HTPart, V>.prefixEntries: Sequence<Triple<HTTagPrefix, HTMaterialKey, V>>
    get() = this
        .entries
        .asSequence()
        .mapNotNull { (part: HTPart, key: HTMaterialKey, entry: V) ->
            val prefix: HTTagPrefix = part.tagPrefix ?: return@mapNotNull null
            Triple(prefix, key, entry)
        }

package hiiragi283.lib.item.alchemy

import hiiragi283.lib.item.createItemTemplate
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.jvm.optionals.getOrNull
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.material.Fluid

/**
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
data object HTPotionHelper {
    //    DataComponentGetter    //

    /**
     * 指定した[getter]から[PotionContents]を取得します。
     * @return 値を保持していない場合は[PotionContents.EMPTY]
     * @since 0.10.0
     */
    @JvmStatic
    fun getPotion(getter: DataComponentGetter): PotionContents = getter.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)

    /**
     * @since 0.14.0
     */
    @JvmStatic
    fun setPotion(holder: MutableDataComponentHolder, contents: PotionContents?) {
        holder.set(DataComponents.POTION_CONTENTS, contents)
    }

    /**
     * 指定した[getter]からポーションのMod IDを取得します。
     * @since 0.11.0
     */
    @JvmStatic
    fun getPotionModId(getter: DataComponentGetter): String? = getPotion(getter)
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::identifier)
        .map(Identifier::getNamespace)
        .getOrNull()

    //    ItemStack    //

    /**
     * 指定した[contents]からポーションの[ItemStack]を作成します。
     * @since 0.11.0
     */
    @JvmStatic
    fun createPotion(contents: BottledPotionContents): ItemStackTemplate = createPotion(contents.bottleType, contents.contents).getOrThrow()

    /**
     * 指定した引数からポーションの[ItemStack]を作成します。
     * @param item アイテムの種類
     * @param potion ポーションの中身
     * @param count [ItemStack]の個数
     */
    @JvmStatic
    fun createPotion(item: ItemLike, potion: Holder<Potion>, count: Int = 1): Result<ItemStackTemplate> = createPotion(item, PotionContents(potion), count)

    /**
     * 指定した引数からポーションの[ItemStack]を作成します。
     * @param item アイテムの種類
     * @param contents ポーションの中身
     * @param count [ItemStack]の個数
     */
    @JvmStatic
    fun createPotion(item: ItemLike, contents: PotionContents, count: Int = 1): Result<ItemStackTemplate> = createItemTemplate(item, DataComponents.POTION_CONTENTS, contents, count)

    /**
     * 指定した[instance]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmName("getContentsFromItem")
    @JvmStatic
    fun getContents(instance: TypedInstance<Item>): BottledPotionContents? = null // TODO

    /**
     * 指定した[instance]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun <T> getContentsFromBottle(instance: T): BottledPotionContents? where T : TypedInstance<Item>, T : DataComponentGetter {
        val bottleType: HTBottleType = HTBottleType.getBottleType(instance) ?: return null
        val contents: PotionContents = getPotion(instance)
        return BottledPotionContents(contents, bottleType)
    }

    /**
     * 指定した[stack]に[contents]を設定します。
     * @since 0.14.0
     */
    @JvmStatic
    fun setContents(stack: ItemStack, contents: BottledPotionContents): ItemStack {
        // HiiragiCoreAccess.INSTANCE.setContents(stack, contents)
        return stack
    }

    //    FluidStack    //

    /**
     * 指定した[instance]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmName("getContentsFromFluid")
    @JvmStatic
    fun getContents(instance: TypedInstance<Fluid>): BottledPotionContents? = null // TODO

    /**
     * 指定した[stack]に[contents]を設定します。
     * @since 0.11.0
     */
    @JvmStatic
    fun setContents(stack: FluidStack, contents: BottledPotionContents): FluidStack {
        // HiiragiCoreAccess.INSTANCE.setContents(stack, contents)
        return stack
    }
}

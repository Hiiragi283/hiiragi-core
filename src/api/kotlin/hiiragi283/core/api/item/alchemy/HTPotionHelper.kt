package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import hiiragi283.core.api.util.wrapOptional
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.jvm.optionals.getOrNull

/**
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
object HTPotionHelper {
    //    DataComponentHolder    //

    /**
     * 指定した[holder]から[PotionContents]を取得します。
     * @return 値を保持していない場合は[PotionContents.EMPTY]
     * @since 0.10.0
     */
    @JvmStatic
    fun getPotion(holder: DataComponentHolder): PotionContents = holder.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)

    /**
     * @since 0.14.0
     */
    @JvmStatic
    fun setPotion(holder: MutableDataComponentHolder, contents: PotionContents?) {
        holder.set(DataComponents.POTION_CONTENTS, contents)
    }

    /**
     * 指定した[holder]からポーションのMod IDを取得します。
     * @since 0.11.0
     */
    @JvmStatic
    fun getPotionModId(holder: DataComponentHolder): String? = getPotion(holder)
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::location)
        .map(ResourceLocation::getNamespace)
        .getOrNull()

    /**
     * 指定した[holder]からポーションの翻訳キーを取得します。
     * @return [holder]がポーションを保持していない場合は`null`
     * @since 0.11.0
     */
    @JvmStatic
    fun getPotionDescId(holder: DataComponentHolder): String? {
        val contents: BottledPotionContents = when (holder) {
            is FluidStack -> getContents(holder)
            is ItemStack -> getContents(holder)
            is HTFluidResourceType -> getContents(holder)
            is HTItemResourceType -> getContents(holder)
            else -> null
        } ?: return null
        return Potion.getName(contents.potion.wrapOptional(), "${contents.bottleType.asItem().descriptionId}.effect.")
    }

    //    ItemStack    //

    /**
     * 指定した[contents]からポーションの[ItemStack]を作成します。
     * @since 0.11.0
     */
    @JvmStatic
    fun createPotion(contents: BottledPotionContents): ItemStack = createPotion(contents.bottleType, contents.contents)

    /**
     * 指定した引数からポーションの[ItemStack]を作成します。
     * @param item アイテムの種類
     * @param potion ポーションの中身
     * @param count [ItemStack]の個数
     */
    @JvmStatic
    fun createPotion(item: ItemLike, potion: Holder<Potion>, count: Int = 1): ItemStack = createPotion(item, PotionContents(potion), count)

    /**
     * 指定した引数からポーションの[ItemStack]を作成します。
     * @param item アイテムの種類
     * @param contents ポーションの中身
     * @param count [ItemStack]の個数
     */
    @JvmStatic
    fun createPotion(item: ItemLike, contents: PotionContents, count: Int = 1): ItemStack =
        createItemStack(item, DataComponents.POTION_CONTENTS, contents, count)

    /**
     * 指定した[stack]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun getContents(stack: ItemStack): BottledPotionContents? = stack.toResource()?.let(::getContents)

    /**
     * 指定した[resource]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun getContents(resource: HTItemResourceType): BottledPotionContents? = HiiragiCoreAccess.INSTANCE.getContents(resource)

    /**
     * 指定した[stack]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.16.0
     */
    @JvmStatic
    fun getContentsFromBottle(stack: ItemStack): BottledPotionContents? = stack.toResource()?.let(::getContentsFromBottle)

    /**
     * 指定した[resource]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun getContentsFromBottle(resource: HTItemResourceType): BottledPotionContents? {
        val bottleType: HTBottleType = HTBottleType.getBottleType(resource) ?: return null
        val contents: PotionContents = getPotion(resource)
        return BottledPotionContents(contents, bottleType)
    }

    /**
     * 指定した[stack]に[contents]を設定します。
     * @since 0.14.0
     */
    @JvmStatic
    fun setContents(stack: ItemStack, contents: BottledPotionContents): ItemStack {
        HiiragiCoreAccess.INSTANCE.setContents(stack, contents)
        return stack
    }

    //    FluidStack    //

    /**
     * 指定した[stack]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.11.0
     */
    @JvmStatic
    fun getContents(stack: FluidStack): BottledPotionContents? = stack.toResource()?.let(::getContents)

    /**
     * 指定した[resource]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun getContents(resource: HTFluidResourceType): BottledPotionContents? = HiiragiCoreAccess.INSTANCE.getContents(resource)

    /**
     * 指定した[stack]に[contents]を設定します。
     * @since 0.11.0
     */
    @JvmStatic
    fun setContents(stack: FluidStack, contents: BottledPotionContents): FluidStack {
        HiiragiCoreAccess.INSTANCE.setContents(stack, contents)
        return stack
    }
}

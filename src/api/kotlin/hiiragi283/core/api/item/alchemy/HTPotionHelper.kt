package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.buildDataPatch
import hiiragi283.core.api.text.Text
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource
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
    fun getPotion(holder: DataComponentGetter): PotionContents = holder.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)

    @JvmStatic
    fun setPotion(holder: MutableDataComponentHolder, contents: PotionContents?) {
        holder.set(DataComponents.POTION_CONTENTS, contents)
    }

    /**
     * 指定した[holder]からポーションのMod IDを取得します。
     * @since 0.11.0
     */
    @JvmStatic
    fun getPotionModId(holder: DataComponentGetter): String? = getPotion(holder)
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::identifier)
        .map(Identifier::getNamespace)
        .getOrNull()

    /**
     * 指定した[holder]からポーションの名前を取得します。
     * @return [holder]がポーションを保持していない場合は`null`
     * @since 0.11.0
     */
    @JvmStatic
    fun getPotionText(holder: DataComponentGetter): Text? {
        val contents: BottledPotionContents = when (holder) {
            is FluidStack -> getContents(holder)
            is ItemStack -> getContents(holder)
            is ItemResource -> getContents(holder)
            is FluidResource -> getContents(holder)
            else -> null
        } ?: return null
        return contents.getText()
    }

    //    ItemStack    //

    /**
     * 指定した[contents]からポーションの[ItemStackTemplate]を作成します。
     * @since 0.11.0
     */
    @JvmStatic
    fun createPotion(contents: BottledPotionContents): ItemStackTemplate = createPotion(contents.bottleType, contents.contents)

    /**
     * 指定した引数からポーションの[ItemStackTemplate]を作成します。
     * @param item アイテムの種類
     * @param potion ポーションの中身
     * @param count [ItemStackTemplate]の個数
     */
    @JvmStatic
    fun createPotion(item: ItemLike, potion: Holder<Potion>, count: Int = 1): ItemStackTemplate =
        createPotion(item, PotionContents(potion), count)

    /**
     * 指定した引数からポーションの[ItemStackTemplate]を作成します。
     * @param item アイテムの種類
     * @param contents ポーションの中身
     * @param count [ItemStackTemplate]の個数
     */
    @JvmStatic
    fun createPotion(item: ItemLike, contents: PotionContents, count: Int = 1): ItemStackTemplate =
        ItemStackTemplate(item.asItem(), count, buildDataPatch { set(DataComponents.POTION_CONTENTS, contents) })

    /**
     * 指定した[stack]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun getContents(stack: ItemStack): BottledPotionContents? = ItemResource.of(stack).let(::getContents)

    /**
     * 指定した[resource]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun getContents(resource: ItemResource): BottledPotionContents? = HiiragiCoreAccess.INSTANCE.getContents(resource)

    /**
     * 指定した[resource]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun getContentsFromBottle(resource: ItemResource): BottledPotionContents? {
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
    fun getContents(stack: FluidStack): BottledPotionContents? = FluidResource.of(stack).let(::getContents)

    /**
     * 指定した[resource]から[BottledPotionContents]を取得します。
     * @return [BottledPotionContents]を取得できない場合は`null`
     * @since 0.14.0
     */
    @JvmStatic
    fun getContents(resource: FluidResource): BottledPotionContents? = HiiragiCoreAccess.INSTANCE.getContents(resource)

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

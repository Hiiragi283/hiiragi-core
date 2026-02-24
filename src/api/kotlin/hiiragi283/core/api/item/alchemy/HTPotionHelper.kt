package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.item.createItemStack
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
import kotlin.jvm.optionals.getOrNull

/**
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
object HTPotionHelper {
    //    DataComponentHolder    //

    /**
     * 指定した[holder]から[HTPotionContents]を取得します。
     * @return [HTPotionContents]を取得できない場合は`null`
     * @since 0.11.0
     */
    @JvmStatic
    fun getContents(holder: DataComponentHolder): HTPotionContents? = HiiragiCoreAccess.INSTANCE.getContents(holder)

    /**
     * 指定した[holder]に[contents]を設定します。
     * @since 0.11.0
     */
    @JvmStatic
    fun <T : MutableDataComponentHolder> setContents(holder: T, contents: HTPotionContents): T {
        HiiragiCoreAccess.INSTANCE.setContents(holder, contents)
        return holder
    }

    /**
     * 指定した[holder]から[PotionContents]を取得します。
     * @return 値を保持していない場合は[PotionContents.EMPTY]
     * @since 0.10.0
     */
    @JvmStatic
    fun getPotion(holder: DataComponentHolder): PotionContents = holder.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)

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
        val contents: HTPotionContents = getContents(holder) ?: return null
        return Potion.getName(contents.potion.wrapOptional(), "${contents.bottleType.asItem().descriptionId}.effect.")
    }

    //    ItemStack    //

    /**
     * 指定した[contents]からポーションの[ItemStack]を作成します。
     * @since 0.11.0
     */
    @JvmStatic
    fun createPotion(contents: HTPotionContents): ItemStack = createPotion(contents.bottleType, contents.vanilla)

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
}

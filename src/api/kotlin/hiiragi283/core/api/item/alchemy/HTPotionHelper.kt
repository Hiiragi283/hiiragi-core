package hiiragi283.core.api.item.alchemy

import hiiragi283.core.api.item.createItemStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike

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
     * 指定した[holder]と[bottleType]からポーションの翻訳キーを取得します。
     * @since 0.10.0
     */
    @JvmStatic
    fun getPotionName(holder: DataComponentHolder, bottleType: HTBottleType): String =
        Potion.getName(getPotion(holder).potion(), "${bottleType.asItem().descriptionId}.effect.")

    //    ItemStack    //

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

package hiiragi283.lib.item

import hiiragi283.lib.text.MutableText
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.withStyle
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

/**
 * ブロックのクラスを指定できる[BlockItem]の拡張クラスです。
 *
 * 参照 : [Mekanism - ItemBlockMekanism](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/common/item/block/ItemBlockMekanism.java)
 * @param BLOCK [block]のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTBlockItem<BLOCK : Block>(block: BLOCK, properties: Properties) : BlockItem(block, properties) {
    @Suppress("UNCHECKED_CAST")
    override fun getBlock(): BLOCK = super.getBlock() as BLOCK

    /**
     * 表示名の色を取得します。
     * @return 色を付けない場合は`null`
     */
    protected open fun getNameColor(stack: ItemStack): TextColor? = null

    override fun getName(stack: ItemStack): Text {
        var name: MutableText = super.getName(stack).copy()
        val color: TextColor? = getNameColor(stack)
        if (color != null) {
            name = name.withStyle(color)
        }
        return name
    }
}

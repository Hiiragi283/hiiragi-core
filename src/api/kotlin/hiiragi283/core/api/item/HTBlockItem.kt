package hiiragi283.core.api.item

import hiiragi283.core.api.text.translatableText
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

/**
 * ブロックのクラスを指定できる[BlockItem]の拡張クラスです。
 * @param BLOCK [block]のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.item.block.ItemBlockMekanism
 */
open class HTBlockItem<BLOCK : Block>(block: BLOCK, properties: Properties) : BlockItem(block, properties) {
    @Suppress("UNCHECKED_CAST")
    override fun getBlock(): BLOCK = super.getBlock() as BLOCK

    /**
     * 表示名の色を取得します。
     * @return 色を付けない場合は`null`
     */
    protected open fun getNameColor(stack: ItemStack): ChatFormatting? = null

    override fun getName(stack: ItemStack): Component {
        var name: MutableComponent = translatableText(getDescriptionId(stack))
        val color: ChatFormatting? = getNameColor(stack)
        if (color != null) {
            name = name.withStyle(color)
        }
        return name
    }
}

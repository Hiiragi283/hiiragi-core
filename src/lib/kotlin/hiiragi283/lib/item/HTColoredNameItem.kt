package hiiragi283.lib.item

import hiiragi283.lib.text.MutableText
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.withStyle
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * 表示名に色をつける[Item]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTColoredNameItem(properties: Properties) : Item(properties) {
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

package hiiragi283.core.api.item

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.text.translatableText
import hiiragi283.core.api.text.withStyle
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
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
    protected abstract fun getNameColor(stack: ItemStack): HTDefaultColor?

    override fun getName(stack: ItemStack): Component {
        var name: MutableComponent = translatableText(getDescriptionId(stack))
        val color: HTDefaultColor? = getNameColor(stack)
        if (color != null) {
            name = name.withStyle(color)
        }
        return name
    }
}

package hiiragi283.core.api.item

import hiiragi283.core.api.text.translatableText
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

abstract class HTColoredNameItem(properties: Properties) : Item(properties) {
    protected abstract fun getNameColor(stack: ItemStack): ChatFormatting?

    override fun getName(stack: ItemStack): Component {
        var name: MutableComponent = translatableText(getDescriptionId(stack))
        val color: ChatFormatting? = getNameColor(stack)
        if (color != null) {
            name = name.withStyle(color)
        }
        return name
    }
}

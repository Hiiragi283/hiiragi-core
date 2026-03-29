package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.util.Unit
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.ItemAbility

class HTAlmightyPickaxeItem(properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {
    override fun getName(stack: ItemStack): Component = when (stack.has(DataComponents.UNBREAKABLE)) {
        true -> HCTranslation.ETERNAL_PICKAXE.translateColored(ChatFormatting.RED)
        false -> super.getName(stack)
    }

    override fun isFoil(stack: ItemStack): Boolean = super.isFoil(stack) || stack.has(DataComponents.UNBREAKABLE)

    override fun canPerformAction(stack: ItemInstance, itemAbility: ItemAbility): Boolean = itemAbility.name().endsWith("_dig")

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: HTItemHolderLike<*>, context: HTSubCreativeTabContents.Context) {
        val stack: ItemStack = baseItem.toStack()
        stack[DataComponents.UNBREAKABLE] = Unit.INSTANCE
        context(stack)
    }
}

package hiiragi283.core.common.item

import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.text.Text
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.core.setup.HCItems
import io.netty.buffer.ByteBuf
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class HTBlueprintItem(properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {
    companion object {
        const val MAX_NUMBER = 9

        @JvmField
        val RANGE: IntRange = 0..MAX_NUMBER

        @JvmField
        val RANGE_CODEC: BiCodec<ByteBuf, Int> = BiCodecs.intRange(RANGE)
    }

    override fun overrideOtherStackedOnMe(
        stack: ItemStack,
        other: ItemStack,
        slot: Slot,
        action: ClickAction,
        player: Player,
        access: SlotAccess,
    ): Boolean {
        if (action == ClickAction.SECONDARY) {
            stack.update(HCDataComponents.BLUEPRINT_NUMBER, 0) {
                when {
                    it < MAX_NUMBER -> it + 1
                    else -> 0
                }
            }
            return true
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        flag: TooltipFlag,
    ) {
        tooltips += HCTranslation.BLUEPRINT_NUMBER.translate(stack.getOrDefault(HCDataComponents.BLUEPRINT_NUMBER, 0))
    }

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: HTItemHolderLike<*>, context: HTSubCreativeTabContents.Context) {
        RANGE
            .map { createItemStack(HCItems.BLUEPRINT, HCDataComponents.BLUEPRINT_NUMBER, it) }
            .forEach(context)
    }

    override fun shouldAddDefault(): Boolean = false
}

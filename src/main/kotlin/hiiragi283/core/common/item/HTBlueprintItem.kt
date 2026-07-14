package hiiragi283.core.common.item

import com.mojang.serialization.Codec
import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.text.Text
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.setup.HCDataComponents
import net.minecraft.core.Holder
import net.minecraft.world.entity.SlotAccess
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.CreativeModeTab
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
        val CODEC: Codec<Int> = HTCodecs.numberRange(Codec.INT, RANGE)
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

    override fun addItems(baseItem: Holder<Item>, parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output) {
        RANGE
            .map { createItemStack(baseItem.value(), HCDataComponents.BLUEPRINT_NUMBER, it) }
            .forEach(output::accept)
    }

    override fun shouldAddDefault(): Boolean = false
}

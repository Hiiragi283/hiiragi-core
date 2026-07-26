package hiiragi283.core.support.item

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.MutableText
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import net.minecraft.network.chat.TextColor
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block

/**
 * 説明文付きのブロック向けの[BlockItem]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 * @see mekanism.common.item.block.ItemBlockTooltip
 */
open class HTDescriptionBlockItem(block: Block, private val hasDetails: Boolean, properties: Properties) : BlockItem(block, properties) {
    constructor(block: Block, properties: Properties) : this(block, false, properties)

    /**
     * 表示名の色を取得します。
     * @return 色を付けない場合は`null`
     */
    protected open fun getNameColor(stack: ItemStack): TextColor? = null

    override fun getName(stack: ItemStack): Text {
        var name: MutableText = translatableText(getDescriptionId(stack))
        val color: TextColor? = getNameColor(stack)
        if (color != null) {
            name = name.withColor(color.value)
        }
        return name
    }

    final override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        flag: TooltipFlag,
    ) {
        /*if (flag.hasShiftDown()) {
            tooltips.add(block.getDescription().translate())
        } else*/
        if (flag.hasControlDown()) {
            addDetails(stack, context, tooltips, flag)
        } else {
            addStats(stack, context, tooltips, flag)
            if (hasDetails) {
                tooltips.add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS.translateColored(HTDefaultColor.LIGHT_BLUE))
            }
            // tooltips.add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION.translateColored(HTDefaultColor.YELLOW))
        }
    }

    protected open fun addStats(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        flag: TooltipFlag,
    ) {}

    protected open fun addDetails(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Text>,
        flag: TooltipFlag,
    ) {}
}

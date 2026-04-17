package hiiragi283.core.api.fluid

import hiiragi283.core.api.text.MutableText
import hiiragi283.core.api.text.translatableText
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[FluidType]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
open class HTFluidType(properties: Properties) : FluidType(properties) {
    /**
     * 表示名の色を取得します。
     * @return 色を付けない場合は`null`
     * @since 0.15.2
     */
    protected open fun getNameColor(stack: FluidStack): TextColor? = null

    override fun getDescription(stack: FluidStack): Component {
        var name: MutableText = translatableText(getDescriptionId(stack))
        val color: TextColor? = getNameColor(stack)
        if (color != null) {
            name = name.withColor(color.value)
        }
        return name
    }

    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = level.dimensionType().ultraWarm()
}

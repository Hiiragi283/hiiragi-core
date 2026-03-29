package hiiragi283.core.client.util

import net.minecraft.world.level.material.FluidState
import net.neoforged.neoforge.client.fluid.FluidTintSource
import java.awt.Color

data class SimpleFluidTintSource(private val color: Int) : FluidTintSource {
    constructor(color: Color) : this(color.rgb)

    override fun color(state: FluidState): Int = color
}

package hiiragi283.lib.fluid

import hiiragi283.lib.HTConstants
import hiiragi283.lib.resource.toId
import hiiragi283.lib.util.HTDelegates
import java.awt.Color
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.resources.model.sprite.Material
import net.neoforged.neoforge.client.fluid.CustomFluidRenderer
import net.neoforged.neoforge.client.fluid.FluidTintSource
import net.neoforged.neoforge.client.fluid.FluidTintSources

class FluidModelBuilder {
    var still: Material by HTDelegates.onceInitialize()
    var flowing: Material by HTDelegates.onceInitialize()
    var overlay: Material? = null
    var tintSource: FluidTintSource? = null
    var customRenderer: CustomFluidRenderer? = null

    /**
     * @see net.minecraft.client.renderer.block.FluidStateModelSet.WATER_MODEL
     */
    fun setClear() {
        still = Material(HTConstants.MINECRAFT.toId(HTConstants.BLOCK, "water_still"))
        flowing = Material(HTConstants.MINECRAFT.toId(HTConstants.BLOCK, "water_flowing"))
        overlay = Material(HTConstants.MINECRAFT.toId(HTConstants.BLOCK, "water_overlay"))
    }

    fun setDull() {
        still = Material(HTConstants.NEOFORGE.toId(HTConstants.BLOCK, "milk_still"))
        flowing = Material(HTConstants.NEOFORGE.toId(HTConstants.BLOCK, "milk_flowing"))
    }

    fun copyStillToFlowing() {
        flowing = still
    }

    fun colorTint(color: Color) {
        tintSource = FluidTintSources.constant(color.rgb)
    }

    fun build(): FluidModel.Unbaked = FluidModel.Unbaked(still, flowing, overlay, tintSource, customRenderer)
}

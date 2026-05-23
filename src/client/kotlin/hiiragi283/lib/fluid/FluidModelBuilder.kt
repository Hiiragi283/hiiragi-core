package hiiragi283.lib.fluid

import hiiragi283.lib.util.HTDelegates
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.resources.model.sprite.Material
import net.neoforged.neoforge.client.fluid.CustomFluidRenderer
import net.neoforged.neoforge.client.fluid.FluidTintSource

class FluidModelBuilder {
    var still: Material by HTDelegates.onceInitialize()
    var flowing: Material by HTDelegates.onceInitialize()
    var overlay: Material? = null
    var tintSource: FluidTintSource? = null
    var customRenderer: CustomFluidRenderer? = null

    fun copyStillToFlowing() {
        flowing = still
    }

    fun build(): FluidModel.Unbaked = FluidModel.Unbaked(still, flowing, overlay, tintSource, customRenderer)
}

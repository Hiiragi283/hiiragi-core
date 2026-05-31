package hiiragi283.lib.renderer.state

import hiiragi283.lib.transfer.fluid.HTFluidView
import hiiragi283.lib.transfer.fluid.getFluidStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.neoforged.neoforge.transfer.fluid.FluidResource

class HTFluidBlockEntityRenderState : BlockEntityRenderState() {
    var sprite: TextureAtlasSprite? = null
    var color: Int = -1
    var fillingLevel: Float = 0f
    var isLighterThanAir: Boolean = false

    fun extractRenderState(view: HTFluidView) {
        if (view.isEmpty()) {
            sprite = null
            color = -1
            return
        }
        val resource: FluidResource = view.resource

        this.fillingLevel = view.getFilledLevel(view.resource).toFloat()
        val model: FluidModel = Minecraft.getInstance()
            .modelManager
            .fluidStateModelSet
            .get(resource.fluid.defaultFluidState())
        model.stillMaterial().sprite().let(this::sprite::set)
        model.fluidTintSource()?.colorAsStack(view.getFluidStack())?.let(this::color::set)
        this.isLighterThanAir = resource.fluidType.isLighterThanAir
    }
}

package hiiragi283.core.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.lib.renderer.HTBlockEntityRenderer
import hiiragi283.lib.renderer.HTRenderHelper
import hiiragi283.lib.renderer.state.HTFluidBERenderState
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

class HCCopperBasinRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTCopperBasinBlockEntity, HTFluidBERenderState>(context) {
    companion object {
        @JvmStatic
        private val FROM = Vector3f(2 / 16f, 4 / 16f, 2 / 16f)
    }

    override fun createRenderState(): HTFluidBERenderState = HTFluidBERenderState()

    override fun extractRenderState(blockEntity: HTCopperBasinBlockEntity, state: HTFluidBERenderState, partialTicks: Float, cameraPosition: Vec3, breakProgress: ModelFeatureRenderer.CrumblingOverlay?) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress)
        state.extractRenderState(blockEntity.tank)
    }

    override fun submit(state: HTFluidBERenderState, poseStack: PoseStack, submitNodeCollector: SubmitNodeCollector, camera: CameraRenderState) {
        val sprite: TextureAtlasSprite = state.sprite ?: return
        val to = Vector3f(14 / 16f, (7f + state.fillingLevel * 8f) / 16f, 14 / 16f)
        HTRenderHelper.submitCube(submitNodeCollector, poseStack, Sheets.translucentBlockItemSheet(), FROM, to, sprite, state.color, state.lightCoords)
    }
}

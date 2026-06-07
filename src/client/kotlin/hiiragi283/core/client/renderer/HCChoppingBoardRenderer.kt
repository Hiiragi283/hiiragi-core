package hiiragi283.core.client.renderer

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.core.common.block.entity.HTChoppingBoardBlockEntity
import hiiragi283.lib.renderer.HTBlockEntityRenderer
import hiiragi283.lib.renderer.state.HTItemBERenderState
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.phys.Vec3

class HCChoppingBoardRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTChoppingBoardBlockEntity, HTItemBERenderState>(context) {
    private val itemModelResolver: ItemModelResolver = context.itemModelResolver()

    override fun createRenderState(): HTItemBERenderState = HTItemBERenderState()

    override fun extractRenderState(blockEntity: HTChoppingBoardBlockEntity, state: HTItemBERenderState, partialTicks: Float, cameraPosition: Vec3, breakProgress: ModelFeatureRenderer.CrumblingOverlay?) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress)
        state.itemState.clear()
        itemModelResolver.updateForTopItem(
            state.itemState,
            blockEntity.slot.getStack(),
            ItemDisplayContext.GROUND,
            blockEntity.level,
            null,
            blockEntity.blockPos.asLong().toInt(),
        )
    }

    override fun submit(state: HTItemBERenderState, poseStack: PoseStack, submitNodeCollector: SubmitNodeCollector, camera: CameraRenderState) {
        if (state.itemState.isEmpty) return
        poseStack.pushPose()
        poseStack.translate(0.5f, 0.25f, 0.5f)
        poseStack.scale(2f, 2f, 2f)
        state.itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0)
        poseStack.popPose()
    }
}

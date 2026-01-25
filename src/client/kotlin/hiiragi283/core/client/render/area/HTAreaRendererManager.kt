package hiiragi283.core.client.render.area

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.core.api.render.area.HTAreaDefinition
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.GlobalPos
import net.minecraft.world.phys.Vec3
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import org.joml.Quaternionf

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see appeng.client.render.overlay.OverlayManager
 */
@EventBusSubscriber(value = [Dist.CLIENT])
object HTAreaRendererManager {
    @JvmStatic
    private val areaCache: MutableMap<GlobalPos, HTAreaRenderer> = hashMapOf()

    @SubscribeEvent
    fun renderArea(event: RenderLevelStageEvent) {
        if (event.stage != RenderLevelStageEvent.Stage.AFTER_LEVEL) return
        if (areaCache.isEmpty()) return

        val minecraft: Minecraft = Minecraft.getInstance()
        val source: MultiBufferSource.BufferSource = minecraft.renderBuffers().bufferSource()
        val poseStack: PoseStack = event.poseStack

        poseStack.pushPose()

        val projectedView: Vec3 = minecraft.gameRenderer.mainCamera.position
        val rotation = Quaternionf(minecraft.gameRenderer.mainCamera.rotation())
        rotation.invert()
        poseStack.mulPose(rotation)
        poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z)

        for ((pos: GlobalPos, renderer: HTAreaRenderer) in areaCache) {
            if (pos.dimension() == minecraft.level?.dimension()) {
                renderer.render(poseStack, source)
            }
        }

        poseStack.popPose()

        source.endBatch(HTAreaRenderer.RENDER_TYPE)
    }

    fun <AREA : HTAreaDefinition> addArea(area: AREA): AREA {
        val source: GlobalPos = area.getSource() ?: return area
        areaCache[source] = HTAreaRenderer(area)
        return area
    }

    fun removeArea(area: HTAreaDefinition) {
        area.getSource()?.let(::removeArea)
    }

    fun removeArea(pos: GlobalPos) {
        areaCache.remove(pos)
    }
}

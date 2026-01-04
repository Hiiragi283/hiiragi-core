package hiiragi283.core.client.render.area

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.render.area.HTAreaDefinition
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import org.joml.Matrix4f
import java.util.OptionalDouble

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 * @see appeng.client.render.overlay.OverlayRenderer
 */
@JvmInline
value class HTAreaRenderer(val definition: HTAreaDefinition) {
    companion object {
        @Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
        @JvmField
        val RENDER_TYPE: RenderType = RenderType.create(
            HiiragiCoreAPI.id("area_line").toDebugFileName(),
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.LINES,
            256,
            RenderType.CompositeState
                .builder()
                .setShaderState(RenderType.RENDERTYPE_LINES_SHADER)
                .setLineState(RenderStateShard.LineStateShard(OptionalDouble.of(5.0)))
                .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                .setDepthTestState(RenderType.NO_DEPTH_TEST)
                .setOutputState(RenderType.MAIN_TARGET)
                .setCullState(RenderType.NO_CULL)
                .createCompositeState(false),
        )
    }

    fun render(poseStack: PoseStack, source: MultiBufferSource) {
        renderLines(poseStack, source.getBuffer(RENDER_TYPE))
    }

    private fun renderLines(poseStack: PoseStack, consumer: VertexConsumer) {
        for (pos: BlockPos in definition.getArea()) {
            poseStack.pushPose()
            poseStack.translate(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
            val matrix: Matrix4f = poseStack.last().pose()
            addVertices(consumer, matrix, pos, definition.getColor())
            poseStack.popPose()
        }
    }

    private fun addVertices(
        consumer: VertexConsumer,
        matrix: Matrix4f,
        pos: BlockPos,
        color: Int,
    ) {
        val noNorth: Boolean = isOutline(pos, Direction.NORTH)
        val noSouth: Boolean = isOutline(pos, Direction.SOUTH)
        val noWest: Boolean = isOutline(pos, Direction.WEST)
        val noEast: Boolean = isOutline(pos, Direction.EAST)

        // Horizontal Lines
        if (noNorth) {
            consumer.addVertex(matrix, 0f, 0f, 0f).setColor(color).setNormal(1f, 0f, 0f)
            consumer.addVertex(matrix, 1f, 0f, 0f).setColor(color).setNormal(1f, 0f, 0f)
            consumer.addVertex(matrix, 1f, 1f, 0f).setColor(color).setNormal(-1f, 0f, 0f)
            consumer.addVertex(matrix, 0f, 1f, 0f).setColor(color).setNormal(-1f, 0f, 0f)
        }
        if (noSouth) {
            consumer.addVertex(matrix, 1f, 0f, 1f).setColor(color).setNormal(-1f, 0f, 0f)
            consumer.addVertex(matrix, 0f, 0f, 1f).setColor(color).setNormal(-1f, 0f, 0f)
            consumer.addVertex(matrix, 0f, 1f, 1f).setColor(color).setNormal(1f, 0f, 0f)
            consumer.addVertex(matrix, 1f, 1f, 1f).setColor(color).setNormal(1f, 0f, 0f)
        }
        if (noWest) {
            consumer.addVertex(matrix, 0f, 0f, 0f).setColor(color).setNormal(0f, 0f, 1f)
            consumer.addVertex(matrix, 0f, 0f, 1f).setColor(color).setNormal(0f, 0f, 1f)
            consumer.addVertex(matrix, 0f, 1f, 1f).setColor(color).setNormal(0f, 0f, -1f)
            consumer.addVertex(matrix, 0f, 1f, 0f).setColor(color).setNormal(0f, 0f, -1f)
        }
        if (noEast) {
            consumer.addVertex(matrix, 1f, 0f, 1f).setColor(color).setNormal(0f, 0f, -1f)
            consumer.addVertex(matrix, 1f, 0f, 0f).setColor(color).setNormal(0f, 0f, -1f)
            consumer.addVertex(matrix, 1f, 1f, 0f).setColor(color).setNormal(0f, 0f, 1f)
            consumer.addVertex(matrix, 1f, 1f, 1f).setColor(color).setNormal(0f, 0f, 1f)
        }
        // Vertical Lines
        if (noNorth || noWest) {
            consumer.addVertex(matrix, 0f, 0f, 0f).setColor(color).setNormal(0f, 1f, 0f)
            consumer.addVertex(matrix, 0f, 1f, 0f).setColor(color).setNormal(0f, 1f, 0f)
        }
        if (noNorth || noEast) {
            consumer.addVertex(matrix, 1f, 1f, 0f).setColor(color).setNormal(0f, -1f, 0f)
            consumer.addVertex(matrix, 1f, 0f, 0f).setColor(color).setNormal(0f, -1f, 0f)
        }
        if (noSouth || noEast) {
            consumer.addVertex(matrix, 1f, 0f, 1f).setColor(color).setNormal(0f, 1f, 0f)
            consumer.addVertex(matrix, 1f, 1f, 1f).setColor(color).setNormal(0f, 1f, 0f)
        }
        if (noSouth || noWest) {
            consumer.addVertex(matrix, 0f, 1f, 1f).setColor(color).setNormal(0f, -1f, 0f)
            consumer.addVertex(matrix, 0f, 0f, 1f).setColor(color).setNormal(0f, -1f, 0f)
        }
        // Bottom
        // consumer.addVertex(matrix, 0f, 0f, 0f).setColor(color).setNormal(1f, 0f, 0f)
        // consumer.addVertex(matrix, 1f, 0f, 0f).setColor(color).setNormal(1f, 0f, 0f)
        // consumer.addVertex(matrix, 1f, 0f, 1f).setColor(color).setNormal(-1f, 0f, 0f)
        // consumer.addVertex(matrix, 0f, 0f, 1f).setColor(color).setNormal(-1f, 0f, 0f)
    }

    private fun isOutline(pos: BlockPos, side: Direction): Boolean = pos.relative(side) !in definition.getArea()
}

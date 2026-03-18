package hiiragi283.core.client.render.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.HTFluidTank
import hiiragi283.core.api.storage.fluid.getStillTexture
import hiiragi283.core.api.storage.fluid.getTintColor
import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.core.util.HTSpriteRenderHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class HTCopperBasinRenderer(context: BlockEntityRendererProvider.Context) : HTBlockEntityRenderer<HTCopperBasinBlockEntity>(context) {
    override fun render(
        blockEntity: HTCopperBasinBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    ) {
        val tank: HTFluidTank = blockEntity.tank
        val resource: HTFluidResourceType = tank.getResource() ?: return
        val textureId: ResourceLocation = resource.getStillTexture() ?: return
        val sprite: TextureAtlasSprite = Minecraft.getInstance().getTextureAtlas(HTConst.BLOCK_ATLAS).apply(textureId) ?: return

        poseStack.pushPose()
        poseStack.translate(2f / 16f, 4f / 16f, 2f / 16f)
        val level: Float = tank.getLevelAsFloat()
        poseStack.scale(12 / 16f, (3f + level * 8f) / 16f, 12 / 16f)
        HTSpriteRenderHelper.drawFluidBox(
            poseStack,
            bufferSource,
            sprite,
            resource.getTintColor(),
            packedLight,
            packedOverlay,
            listOf(Direction.UP),
        )
        poseStack.popPose()
    }
}

package hiiragi283.core.client.render.block

import com.mojang.blaze3d.vertex.PoseStack
import hiiragi283.core.api.collection.mutableEnumMapOf
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.joml.Quaternionf

/**
 * 参照 : [Mekanism - MekanismTileEntityRenderer](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/render/tileentity/MekanismTileEntityRenderer.java)
 */
@OnlyIn(Dist.CLIENT)
abstract class HTBlockEntityRenderer<BE : BlockEntity>(protected val context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<BE> {
    companion object {
        /**
         * 参照 : [ImmersiveEngineering - IEBlockEntityRenderer.ROTATE_FOR_FACING](https://github.com/BluSunrize/ImmersiveEngineering/blob/1.21.1/src/main/java/blusunrize/immersiveengineering/client/render/tile/IEBlockEntityRenderer.java)
         */
        @JvmField
        val ROTATE_Y: Map<Direction, Quaternionf> = Direction.entries.associateWithTo(mutableEnumMapOf()) { direction: Direction ->
            Quaternionf().rotateY(Mth.DEG_TO_RAD * (180 - direction.toYRot()))
        }

        @JvmStatic
        fun rotateY(poseStack: PoseStack, direction: Direction) {
            poseStack.mulPose(ROTATE_Y[direction]!!)
        }
    }
}

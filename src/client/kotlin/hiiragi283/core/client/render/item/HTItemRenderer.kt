package hiiragi283.core.client.render.item

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.core.Direction
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.RandomSource
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HalfTransparentBlock
import net.minecraft.world.level.block.StainedGlassPaneBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.data.ModelData

/**
 * 参照 : [Mekanism - MekanismISTER](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/render/item/MekanismISTER.java)
 */
abstract class HTItemRenderer :
    BlockEntityWithoutLevelRenderer(
        Minecraft.getInstance().blockEntityRenderDispatcher,
        Minecraft.getInstance().entityModels,
    ) {
    protected fun getBlockEntityRenderDispatcher(): BlockEntityRenderDispatcher = Minecraft.getInstance().blockEntityRenderDispatcher

    protected fun getEntityModels(): EntityModelSet = Minecraft.getInstance().entityModels

    protected fun getClientTicks(): Int = Minecraft.getInstance().levelRenderer.ticks

    protected fun getClientPartialTicks(): Float = Minecraft.getInstance().timer.getGameTimeDeltaPartialTick(false)

    protected fun renderItemAngle(poseStack: PoseStack, action: () -> Unit) {
        poseStack.pushPose()
        poseStack.translate(0.5, 0.5, 0.5)
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f))
        action()
        poseStack.popPose()
    }

    protected fun renderBlockItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
        modelData: ModelData = ModelData.EMPTY,
    ) {
        val blockIn: Block = (stack.item as? BlockItem)?.block ?: return
        val fabulous: Boolean = when {
            displayContext != ItemDisplayContext.GUI && !displayContext.firstPerson() ->
                blockIn !is HalfTransparentBlock && blockIn !is StainedGlassPaneBlock
            else -> true
        }
        val minecraft: Minecraft = Minecraft.getInstance()
        val itemRenderer: ItemRenderer = minecraft.itemRenderer
        val state: BlockState = blockIn.defaultBlockState()
        val baseModel: BakedModel = minecraft.modelManager.blockModelShaper.getBlockModel(state)
        val seed = 42L
        val random: RandomSource = RandomSource.create()
        val hasEffect: Boolean = stack.hasFoil()
        for (model: BakedModel in baseModel.getRenderPasses(stack, fabulous)) {
            for (renderType: RenderType in model.getRenderTypes(stack, fabulous)) {
                val consumer: VertexConsumer = when (fabulous) {
                    true -> ItemRenderer.getFoilBufferDirect(buffer, renderType, true, hasEffect)
                    false -> ItemRenderer.getFoilBuffer(buffer, renderType, true, hasEffect)
                }
                for (direction: Direction? in Direction.entries.plus(null)) {
                    random.setSeed(seed)
                    itemRenderer.renderQuadList(
                        poseStack,
                        consumer,
                        model.getQuads(state, direction, random, modelData, renderType),
                        stack,
                        packedLight,
                        packedOverlay,
                    )
                }
            }
        }
    }

    //    BlockEntityWithoutLevelRenderer    //

    abstract override fun onResourceManagerReload(resourceManager: ResourceManager)

    abstract override fun renderByItem(
        stack: ItemStack,
        displayContext: ItemDisplayContext,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int,
    )
}

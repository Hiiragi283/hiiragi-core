package hiiragi283.lib.renderer

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.world.level.block.entity.BlockEntity

abstract class HTBlockEntityRenderer<BE : BlockEntity, S : BlockEntityRenderState>(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<BE, S>

package hiiragi283.lib.renderer.state

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState

class HTItemBERenderState : BlockEntityRenderState() {
    val itemState: ItemStackRenderState = ItemStackRenderState()
}

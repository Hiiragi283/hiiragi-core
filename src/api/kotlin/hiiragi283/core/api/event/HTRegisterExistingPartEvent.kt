package hiiragi283.core.api.event

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent

sealed class HTRegisterExistingPartEvent :
    Event(),
    IModBusEvent {
    //    BlockEvent    //

    class BlockEvent(private val blockConsumer: (HTTagPrefix, HTMaterialKey, HTBlockHolderLike<*>) -> Unit) :
        HTRegisterExistingPartEvent() {
        fun registerBlock(prefix: HTTagPrefix, material: HTMaterialLike, block: Block) {
            registerBlock(prefix, material, HTBlockHolderLike.of(block))
        }

        fun registerBlock(prefix: HTTagPrefix, material: HTMaterialLike, block: HTBlockHolderLike<*>) {
            blockConsumer(prefix, material.asMaterialKey(), block)
        }
    }

    //    ItemEvent    //

    class ItemEvent(private val itemConsumer: (HTTagPrefix, HTMaterialKey, HTItemHolderLike<*>) -> Unit) : HTRegisterExistingPartEvent() {
        fun registerItem(prefix: HTTagPrefix, material: HTMaterialLike, item: Item) {
            registerItem(prefix, material, HTItemHolderLike.of(item))
        }

        fun registerItem(prefix: HTTagPrefix, material: HTMaterialLike, item: HTItemHolderLike<*>) {
            itemConsumer(prefix, material.asMaterialKey(), item)
        }
    }

    //    ToolEvent    //

    class ToolEvent(private val toolConsumer: (HTToolType, HTMaterialKey, HTItemHolderLike<*>) -> Unit) : HTRegisterExistingPartEvent() {
        fun registerTool(toolType: HTToolType, material: HTMaterialLike, item: Item) {
            registerTool(toolType, material, HTItemHolderLike.of(item))
        }

        fun registerTool(toolType: HTToolType, material: HTMaterialLike, item: HTItemHolderLike<*>) {
            toolConsumer(toolType, material.asMaterialKey(), item)
        }
    }
}

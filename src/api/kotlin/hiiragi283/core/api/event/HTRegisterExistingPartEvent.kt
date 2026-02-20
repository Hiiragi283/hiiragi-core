package hiiragi283.core.api.event

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.Event
import net.neoforged.fml.event.IModBusEvent
import java.util.function.Supplier

sealed class HTRegisterExistingPartEvent :
    Event(),
    IModBusEvent {
    fun interface Consumer<T, V> {
        fun accept(
            type: T,
            key: HTMaterialKey,
            id: ResourceLocation,
            value: Supplier<out V>,
        )
    }

    //    BlockEvent    //

    class BlockEvent(private val blockConsumer: Consumer<HTTagPrefix, Block>) : HTRegisterExistingPartEvent() {
        fun registerBlock(prefix: HTTagPrefix, material: HTMaterialLike, block: Block) {
            registerBlock(prefix, material, block.toLike())
        }

        fun registerBlock(
            prefix: HTTagPrefix,
            material: HTMaterialLike,
            id: ResourceLocation,
            block: Supplier<out Block>,
        ) {
            blockConsumer.accept(prefix, material.asMaterialKey(), id, block)
        }

        fun registerBlock(prefix: HTTagPrefix, material: HTMaterialLike, block: HTBlockHolderLike<*>) {
            blockConsumer.accept(prefix, material.asMaterialKey(), block.getId(), block)
        }
    }

    //    ItemEvent    //

    class ItemEvent(private val itemConsumer: Consumer<HTTagPrefix, Item>) : HTRegisterExistingPartEvent() {
        fun registerItem(prefix: HTTagPrefix, material: HTMaterialLike, item: Item) {
            registerItem(prefix, material, HTItemHolderLike.of(item))
        }

        fun registerItem(
            prefix: HTTagPrefix,
            material: HTMaterialLike,
            id: ResourceLocation,
            item: Supplier<out Item>,
        ) {
            itemConsumer.accept(prefix, material.asMaterialKey(), id, item)
        }

        fun registerItem(prefix: HTTagPrefix, material: HTMaterialLike, item: HTItemHolderLike<*>) {
            itemConsumer.accept(prefix, material.asMaterialKey(), item.getId(), item::asItem)
        }
    }

    //    ToolEvent    //

    class ToolEvent(private val toolConsumer: Consumer<HTToolType, Item>) : HTRegisterExistingPartEvent() {
        fun registerTool(toolType: HTToolType, material: HTMaterialLike, item: Item) {
            registerTool(toolType, material, HTItemHolderLike.of(item))
        }

        fun registerItem(
            toolType: HTToolType,
            material: HTMaterialLike,
            id: ResourceLocation,
            item: Supplier<out Item>,
        ) {
            toolConsumer.accept(toolType, material.asMaterialKey(), id, item)
        }

        fun registerTool(toolType: HTToolType, material: HTMaterialLike, item: HTItemHolderLike<*>) {
            toolConsumer.accept(toolType, material.asMaterialKey(), item.getId(), item::asItem)
        }
    }
}

package hiiragi283.core.api.plugin

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.HTTagPrefix

interface HTMaterialPlugin : HTIdLike {
    val priority: Int

    fun registerExistingBlock(consumer: BlockConsumer) {}

    fun interface BlockConsumer {
        fun accept(prefix: HTTagPrefix, key: HTMaterialKey, holder: HTBlockHolderLike<*>)
    }

    fun registerExistingItem(consumer: ItemConsumer) {}

    fun interface ItemConsumer {
        fun accept(prefix: HTTagPrefix, key: HTMaterialKey, holder: HTItemHolderLike<*>)
    }

    fun registerExistingTool(consumer: ToolConsumer) {}

    fun interface ToolConsumer {
        fun accept(toolType: HTToolType, key: HTMaterialKey, holder: HTItemHolderLike<*>)
    }

    fun onModifyMaterial(builder: MaterialBuilder) {}

    fun interface MaterialBuilder {
        fun getBuilder(key: HTMaterialKey): HTPropertyMap.Mutable
    }
}

package hiiragi283.core.api.plugin

import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.HTPartLike
import hiiragi283.core.api.property.HTBasicPropertyMap
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.HTIdLike

interface HTMaterialPlugin : HTIdLike {
    val priority: Int

    //    Part    //

    fun registerPart(registrar: PartRegistrar) {}

    fun interface PartRegistrar {
        fun register(name: String, idPattern: String, properties: HTPropertyMap): HTPartLike

        fun register(name: String, idPattern: String, builderAction: HTPropertyMap.Mutable.() -> Unit): HTPartLike =
            register(name, idPattern, HTBasicPropertyMap.Mutable().apply(builderAction))
    }

    //    Material    //

    fun registerExistingBlock(consumer: BlockConsumer) {}

    fun interface BlockConsumer {
        fun accept(part: HTPartLike, material: HTMaterialKey, holder: HTBlockHolderLike<*>)
    }

    fun registerExistingItem(consumer: ItemConsumer) {}

    fun interface ItemConsumer {
        fun accept(part: HTPartLike, material: HTMaterialKey, holder: HTItemHolderLike<*>)
    }

    fun registerExistingTool(consumer: ToolConsumer) {}

    fun interface ToolConsumer {
        fun accept(toolType: HTToolType, material: HTMaterialKey, holder: HTItemHolderLike<*>)
    }

    fun modifyMaterial(provider: MaterialProvider) {}

    fun interface MaterialProvider {
        fun getBuilder(key: HTMaterialKey): HTPropertyMap.Mutable
    }
}

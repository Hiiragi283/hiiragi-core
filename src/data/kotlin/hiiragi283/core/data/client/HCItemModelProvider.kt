package hiiragi283.core.data.client

import hiiragi283.core.HiiragiCore
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTItemModelProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.setup.HCItems
import net.minecraft.resources.ResourceLocation

class HCItemModelProvider(context: HTDataGenContext) : HTItemModelProvider(context) {
    override fun registerModels() {
        // Material
        HCItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            val textureId: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, prefix.asPrefixName(), key.asMaterialName())
            if (existTexture(textureId)) {
                basicItemAlt(item, textureId)
            } else {
                HiiragiCore.LOGGER.debug("Missing texture {} for {}", textureId, item.getId())
            }
        }
    }
}

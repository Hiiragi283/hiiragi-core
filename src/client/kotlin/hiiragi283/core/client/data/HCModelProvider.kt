package hiiragi283.core.client.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.model.HTModelProvider
import hiiragi283.core.api.data.model.HTTexturedModels
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.material.part.property.HTPartPropertyKeys
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.toFluidLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager

data object HCModelProvider : HTModelProvider() {
    override fun registerModels(manager: ResourceManager) {
        registerMaterials(manager)
    }

    @JvmStatic
    private fun registerMaterials(manager: ResourceManager) {
        val registered: HTMaterialAccess = HiiragiCoreAccess.INSTANCE.registeredContents
        // Block
        registered.blocks.forEach { (part: HTPart, key: HTMaterialKey, block: HTBlockHolderLike<*>) ->
            if (HTPartPropertyKeys.IS_ORE in part) {
                val stoneTexture: ResourceLocation = part[HTPartPropertyKeys.ORE_STONE_TEX] ?: return@forEach
                addSimpleBlockAndItem(
                    block,
                    HTTexturedModels.layeredBlock(
                        stoneTexture,
                        CommonParts.ORE
                            .createId(key)
                            .withPrefix("block/"),
                    ),
                )
            } else {
                addSimpleBlockAndItem(block)
            }
        }
        // Fluid
        HiiragiCoreAccess.INSTANCE.registeredFluids.forEach { (part: HTFluidPart, _: HTMaterialKey, fluid) ->
            addBucketModel(fluid.toFluidLike(), part == HTFluidPart.MOLTEN)
        }
        // Item
        registered.items.forEach { (part: HTPart, _, item: HTIdLike) ->
            val textureIcon: String = part[HTPartPropertyKeys.TEXTURE_ICON] ?: part.name
            val overlay: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, "${textureIcon}_overlay")
            if (manager.getResource(overlay.withPath { "textures/$it.png" }).isPresent) {
                addItemModel(item, HTTexturedModels.layeredItem(item.itemId, overlay))
            } else {
                addSimpleItemModel(item)
            }
        }
    }
}

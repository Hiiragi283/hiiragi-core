package hiiragi283.core.client.data

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.model.HTModelProvider
import hiiragi283.core.api.data.model.HTTexturedModels
import hiiragi283.core.api.material.HTMaterialAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.fluid.CommonFluidTagPrefixes
import hiiragi283.core.api.tag.fluid.HTFluidTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
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
        registered.blocks.forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTBlockHolderLike<*>) ->
            if (prefix in CommonTagPrefixes.ORES) {
                val stoneTexture: ResourceLocation = prefix[HTTagPropertyKeys.ORE_STONE_TEX] ?: return@forEach
                addSimpleBlockAndItem(
                    block,
                    HTTexturedModels.layeredBlock(
                        stoneTexture,
                        CommonTagPrefixes.ORE.createId(key).withPrefix("block/"),
                    ),
                )
            } else {
                addSimpleBlockAndItem(block)
            }
        }
        // Fluid
        HiiragiCoreAccess.INSTANCE.registeredFluids.forEach { (prefix: HTFluidTagPrefix, _, fluid: HTFluidHolderLike<*>) ->
            addBucketModel(fluid, prefix == CommonFluidTagPrefixes.MOLTEN)
        }
        // Item
        registered.items.forEach { (prefix: HTTagPrefix, _, item: HTIdLike) ->
            val textureIcon: String = prefix[HTTagPropertyKeys.TEXTURE_ICON] ?: prefix.name
            val overlay: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, "${textureIcon}_overlay")
            if (manager.getResource(overlay.withPath { "textures/$it.png" }).isPresent) {
                addItemModel(item, HTTexturedModels.layeredItem(item.itemId, overlay))
            } else {
                addSimpleItemModel(item)
            }
        }
    }
}

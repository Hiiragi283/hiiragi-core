package hiiragi283.core.client.datagen.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.model.HTModelProvider
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.level.block.NetherWartBlock

data object HCModelProvider : HTModelProvider() {
    override fun registerModels(manager: ResourceManager) {
        registerMaterials(manager)

        registerBlocks()
        registerItems()
    }

    @JvmStatic
    private fun registerMaterials(manager: ResourceManager) {
        val contents: HTMaterialContents = HiiragiCoreAccess.INSTANCE.materialContents
        // Block
        contents.getBlockTable().forEach { (prefix: HTTagPrefix, _, block: HTBlockHolderLike<*, *>) ->
            if (prefix in CommonTagPrefixes.ORES) {
            } else {
                addSimpleBlockAndItem(block)
            }
        }
        // Item
        contents.getItemTable().forEach { (prefix: HTTagPrefix, _, item: HTIdLike) ->
            val textureIcon: String = prefix[HTTagPropertyKeys.TEXTURE_ICON] ?: prefix.name
            val overlay: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, "${textureIcon}_overlay.png")
            if (manager.getResource(overlay).isPresent) {
                addLayeredItemModel(item, item.itemId, overlay)
            } else {
                addLayeredItemModel(item, item.itemId)
            }
        }
    }

    //    Block     //

    @JvmStatic
    private fun registerBlocks() {
        addCropBlock(HCBlocks.WARPED_WART, NetherWartBlock.AGE, 0, 1, 1, 2)

        HCFluids.REGISTER.asSequence().forEach(::addLiquidBlock)
    }

    //    Item     //

    @JvmStatic
    private fun registerItems() {
        buildList {
            addAll(HCItems.REGISTER.asSequence())

            remove(HCItems.STEEL_COMPOUND)
        }.forEach(::addSimpleItemModel)

        addLayeredItemModel(
            HCItems.STEEL_COMPOUND,
            HTConst.MINECRAFT.toId(HTConst.ITEM, "iron_ingot"),
            HCItems.STEEL_COMPOUND.itemId,
        )

        registerBuckets()
    }

    @JvmStatic
    private fun registerBuckets() {
        val dripFluids: List<HTFluidContent> = buildList {
            // Vanilla
            addAll(HCFluids.DYE.values)

            add(HCFluids.HONEY)
            // Molten
            add(HCFluids.MOLTEN_GLASS)
            add(HCFluids.MOLTEN_STAINLESS_STEEL)
            add(HCFluids.MOLTEN_PLASTIC)
            add(HCFluids.MOLTEN_RUBBER)
            add(HCFluids.MOLTEN_CRIMSON_CRYSTAL)
            add(HCFluids.MOLTEN_WARPED_CRYSTAL)
            add(HCFluids.MOLTEN_ELDRITCH)
            // add(HCFluids.MOLTEN_OMINOUS_METAL)
        }
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            addBucketModel(content, content in dripFluids)
        }
    }
}

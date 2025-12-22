package hiiragi283.core.data.client.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTItemModelProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder

class HCItemModelProvider(context: HTDataGenContext) : HTItemModelProvider(context) {
    override fun registerModels() {
        buildList {
            addAll(HCItems.REGISTER.asSequence())

            removeAll(HCItems.MATERIALS.values)
        }.forEach { item: HTIdLike -> existTexture(item, ::basicItem) }

        registerMaterials()
        registerBuckets()
    }

    private fun registerMaterials() {
        HCItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            val textureId: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, prefix.asPrefixName(), key.asMaterialName())
            if (prefix == HCMaterialPrefixes.WIRE) {
                existTexture(item, textureId) { itemIn: HTIdLike, layer: ResourceLocation ->
                    layeredItem(itemIn, layer, HiiragiCoreAPI.id(HTConst.ITEM, "wire_overlay"))
                }
            } else {
                existTexture(item, textureId, ::layeredItem)
            }
        }
    }

    private fun registerBuckets() {
        val dripFluids: List<HTSimpleFluidContent> = buildList {
            // Vanilla
            add(HCFluids.HONEY)
            add(HCFluids.MUSHROOM_STEW)
            // Molten
            add(HCFluids.CRIMSON_BLOOD)
            add(HCFluids.DEW_OF_THE_WARP)
            add(HCFluids.ELDRITCH_FLUX)
        }
        for (content: HTFluidContent<*, *, *> in HCFluids.REGISTER.entries) {
            val parent: ResourceLocation = when (content) {
                in dripFluids -> "bucket_drip"
                else -> "bucket"
            }.let { HTConst.NEOFORGE.toId("item", it) }

            withExistingParent(content.bucket.getPath(), parent)
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
                .apply {
                    if (content.getFluidType().isLighterThanAir) {
                        flipGas(true)
                    }
                }
        }
    }
}

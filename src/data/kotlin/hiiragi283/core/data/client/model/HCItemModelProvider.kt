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
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.resources.ResourceLocation

class HCItemModelProvider(context: HTDataGenContext) : HTItemModelProvider(HiiragiCoreAPI.MOD_ID, context) {
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
            existTexture(item, textureId) { itemIn: HTIdLike, layer: ResourceLocation ->
                val overlay: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, "${prefix.name}_overlay")
                if (existingFileHelper.exists(overlay, TEXTURE)) {
                    layeredItem(itemIn, layer, overlay)
                } else {
                    layeredItem(itemIn, layer)
                }
            }
        }
    }

    private fun registerBuckets() {
        val dripFluids: List<HTSimpleFluidContent> = buildList {
            // Vanilla
            add(HCFluids.HONEY)
            // Molten
            add(HCFluids.MOLTEN_GLASS)
            add(HCFluids.MOLTEN_CRIMSON_CRYSTAL)
            add(HCFluids.MOLTEN_WARPED_CRYSTAL)
            add(HCFluids.MOLTEN_ELDRITCH)
        }
        for (content: HTFluidContent<*, *, *> in HCFluids.REGISTER.entries) {
            bucketItem(content, content in dripFluids)
        }
    }
}

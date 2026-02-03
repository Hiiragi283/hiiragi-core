package hiiragi283.core.data.client.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTItemModelProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems

class HCItemModelProvider(context: HTDataGenContext) : HTItemModelProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun registerModels() {
        buildList {
            addAll(HCItems.REGISTER.asSequence())

            remove(HCItems.STEEL_COMPOUND)
        }.forEach { item: HTIdLike -> existTexture(item, ::basicItem) }

        registerMaterials()
        existTexture(HCItems.STEEL_COMPOUND) { item: HTIdLike ->
            layeredItem(item, HTConst.MINECRAFT.toId(HTConst.ITEM, "iron_ingot"), item.itemId)
        }

        registerTools()

        registerBuckets()
    }

    private fun registerBuckets() {
        val dripFluids: List<HTFluidContent<*, *, *>> = buildList {
            // Vanilla
            add(HCFluids.HONEY)
            // Molten
            add(HCFluids.MOLTEN_GLASS)
            add(HCFluids.MOLTEN_PLASTIC)
            add(HCFluids.MOLTEN_RUBBER)
            add(HCFluids.MOLTEN_CRIMSON_CRYSTAL)
            add(HCFluids.MOLTEN_WARPED_CRYSTAL)
            add(HCFluids.MOLTEN_ELDRITCH)
            // add(HCFluids.MOLTEN_OMINOUS_METAL)
        }
        for (content: HTFluidContent<*, *, *> in HCFluids.REGISTER.entries) {
            bucketItem(content, content in dripFluids)
        }
    }
}

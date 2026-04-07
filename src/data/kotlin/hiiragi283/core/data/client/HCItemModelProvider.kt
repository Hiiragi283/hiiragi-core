package hiiragi283.core.data.client

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
        trackItem(HCItems.BAMBOO_CHARCOAL)

        trackItem(HCItems.RAW_RUBBER)
        trackItem(HCItems.POLYMER_RESIN)
        trackItem(HCItems.SYNTHETIC_FEATHER)
        trackItem(HCItems.SYNTHETIC_FIBER)
        trackItem(HCItems.SYNTHETIC_LEATHER)

        trackItem(HCItems.WHEAT_FLOUR)
        trackItem(HCItems.WHEAT_DOUGH)

        trackItem(HCItems.LUMINOUS_PASTE)
        trackItem(HCItems.ELDER_HEART)
        trackItem(HCItems.WITHER_STAR)

        trackItem(HCItems.ELDRITCH_EGG)

        trackItem(HCItems.IRIDESCENT_POWDER)

        buildList {
            addAll(HCItems.REGISTER.asSequence())

            remove(HCItems.STEEL_COMPOUND)

            remove(HCItems.BLUEPRINT)
        }.forEach { item: HTIdLike -> existTexture(item, ::basicItem) }

        existTexture(HCItems.STEEL_COMPOUND) { item: HTIdLike ->
            layeredItem(item, HTConst.MINECRAFT.toId(HTConst.ITEM, "iron_ingot"), item.itemId)
        }

        existTexture(HCItems.BLUEPRINT) { item: HTIdLike ->
            layeredItem(
                item,
                item.itemId,
                HTConst.MINECRAFT.toId(HTConst.ITEM, "filled_map_markings"),
            )
        }

        registerBuckets()
    }

    private fun registerBuckets() {
        val dripFluids: List<HTFluidContent> = buildList {
            // Vanilla
            addAll(HCFluids.DYE.values)

            add(HCFluids.HONEY)
        }
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            bucketItem(content, content in dripFluids)
        }
    }
}

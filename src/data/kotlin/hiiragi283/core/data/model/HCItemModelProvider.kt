package hiiragi283.core.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.model.HTItemModelProvider
import hiiragi283.core.api.data.model.ModelOutput
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.data.PackOutput

class HCItemModelProvider(output: PackOutput) : HTItemModelProvider(output, HiiragiCoreAPI.MOD_ID) {
    override fun registerModels(output: ModelOutput) {
        buildSet {
            addAll(HCItems.REGISTER.asSequence())

            remove(HCItems.STEEL_COMPOUND)

            remove(HCItems.BLUEPRINT)

            remove(HCItems.POTION_OF_INFINITY)
        }.forEach { item: HTIdLike -> basicItem(output, item) }

        HCItems.STEEL_COMPOUND.let { layeredItem(output, it, vanillaId(HTConst.ITEM, "iron_ingot"), it.itemId) }
        HCItems.BLUEPRINT.let { layeredItem(output, it, it.itemId, vanillaId(HTConst.ITEM, "filled_map_markings")) }

        layeredItem(output, HCItems.POTION_OF_INFINITY, vanillaId(HTConst.ITEM, "potion"), vanillaId(HTConst.ITEM, "potion_overlay"))

        registerBuckets(output)
    }

    private fun registerBuckets(output: ModelOutput) {
        val dripFluids: Set<HTFluidContent> = buildSet {
            // Vanilla
            addAll(HCFluids.DYES)

            add(HCFluids.HONEY)
        }
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            bucketItem(output, content, content in dripFluids)
        }
    }
}

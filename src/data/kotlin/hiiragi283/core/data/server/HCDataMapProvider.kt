package hiiragi283.core.data.server

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

class HCDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    override fun gather(provider: HolderLookup.Provider) {
        compostables()
        furnaceFuels()
    }

    private fun compostables() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
            .add(HCBlocks.WARPED_WART.itemHolder, Compostable(0.5f), false)
    }

    private fun furnaceFuels() {
        val furnace: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HTMaterialManager.INSTANCE.entries) {
            if (key.getNamespace() != HiiragiCoreAPI.MOD_ID) continue
            val fuelTime: Int = propertyMap[HTMaterialPropertyKeys.FUEL_TIME] ?: continue
            // Block
            if (HTMaterialContentsAccess.INSTANCE.getBlock(CommonTagPrefixes.BLOCK, key) != null) {
                furnace.add(CommonTagPrefixes.BLOCK.itemTagKey(key), FurnaceFuel(fuelTime * 10), false)
            }
            // Item
            for ((prefix: HTTagPrefix, _) in HTMaterialContentsAccess.INSTANCE.getItemMap(key)) {
                val fuelTime1: Int = when (prefix) {
                    CommonTagPrefixes.NUGGET -> fuelTime / 10
                    else -> prefix.getOrDefault(HTTagPropertyKeys.ITEM_SCALE)(fuelTime, propertyMap)
                }
                furnace.add(prefix.itemTagKey(key), FurnaceFuel(fuelTime1), false)
            }
        }

        furnace.add(HCItems.BAMBOO_CHARCOAL, FurnaceFuel(20 * 10 * 6), false)
    }
}

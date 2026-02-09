package hiiragi283.core.common.data.map

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.map.HTDataMapProvider
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.times
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import org.apache.commons.lang3.math.Fraction

data object HCDataMapProviders {
    data object FurnaceFuels : HTDataMapProvider<FurnaceFuel, Item>(NeoForgeDataMaps.FURNACE_FUELS) {
        override fun gather() {
            val contents: HTMaterialContents = HiiragiCoreAccess.INSTANCE.materialContents
            for (entry: HTMaterialManager.Entry in HiiragiCoreAccess.INSTANCE.materialManager) {
                val baseTime: Int = entry[HTMaterialPropertyKeys.FUEL_TIME] ?: continue
                // Block
                for ((prefix: HTTagPrefix, _) in contents.getBlockMap(entry)) {
                    val fuelScale: Fraction = prefix[HTTagPropertyKeys.FUEL_SCALE] ?: continue
                    val fuelTime: Int = (baseTime * fuelScale).toInt()
                    add(prefix.itemTagKey(entry), FurnaceFuel(fuelTime))
                }
                // Item
                for ((prefix: HTTagPrefix, _) in contents.getItemMap(entry)) {
                    val fuelScale: Fraction = prefix[HTTagPropertyKeys.FUEL_SCALE] ?: continue
                    val fuelTime: Int = (baseTime * fuelScale).toInt()
                    add(prefix.itemTagKey(entry), FurnaceFuel(fuelTime))
                }
            }
        }
    }
}

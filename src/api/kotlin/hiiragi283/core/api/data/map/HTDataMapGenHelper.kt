package hiiragi283.core.api.data.map

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.property.HTTagPropertyKeys
import hiiragi283.core.api.times
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import org.apache.commons.lang3.math.Fraction

object HTDataMapGenHelper {
    @JvmStatic
    fun registerFurnaceFuels(builder: (TagKey<Item>, Int) -> Unit) {
        for (entry: HTMaterialManager.Entry in HiiragiCoreAccess.INSTANCE.materialManager) {
            if (entry.namespace != HiiragiCoreAPI.MOD_ID) continue
            val baseTime: Int = entry[HTMaterialPropertyKeys.FUEL_TIME] ?: continue
            // Block
            for ((prefix: HTTagPrefix, _) in HiiragiCoreAccess.INSTANCE.materialContents.getBlockMap(entry)) {
                val fuelScale: Fraction = prefix[HTTagPropertyKeys.FUEL_SCALE] ?: continue
                builder(prefix.itemTagKey(entry), (baseTime * fuelScale).toInt())
            }
            // Item
            for ((prefix: HTTagPrefix, _) in HiiragiCoreAccess.INSTANCE.materialContents.getItemMap(entry)) {
                val fuelScale: Fraction = prefix[HTTagPropertyKeys.FUEL_SCALE] ?: continue
                builder(prefix.itemTagKey(entry), (baseTime * fuelScale).toInt())
            }
        }
    }
}

package hiiragi283.core.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

class HCDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    override fun gather(provider: HolderLookup.Provider) {
        val furnace: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        val crimson = HCMaterial.Gems.CRIMSON_CRYSTAL
        for ((prefix: HTMaterialPrefix, _) in HCItems.MATERIALS.column(crimson)) {
            furnace.add(prefix.itemTagKey(crimson), FurnaceFuel(20 * 10 * 24), false)
        }
    }
}

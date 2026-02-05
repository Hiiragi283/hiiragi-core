package hiiragi283.core.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.map.HTDataMapGenHelper
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.tags.TagKey
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
        HTDataMapGenHelper.registerFurnaceFuels { tagKey: TagKey<Item>, time: Int ->
            furnace.add(tagKey, FurnaceFuel(time), false)
        }

        furnace.add(HCItems.BAMBOO_CHARCOAL, FurnaceFuel(20 * 10 * 6), false)
    }
}

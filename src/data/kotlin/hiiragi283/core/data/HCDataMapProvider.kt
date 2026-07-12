package hiiragi283.core.data

import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.copper.component1
import hiiragi283.lib.copper.component2
import hiiragi283.lib.copper.component3
import hiiragi283.lib.copper.component4
import hiiragi283.lib.copper.get
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.resource.SimpleSupplierWithKey
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.WeatheringCopperCollection
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable

class HCDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {
        val furnaceFuels: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)
        HCBlocks.getResult(CommonPartKeys.STORAGE_BLOCK, VanillaMaterialKeys.CHARCOAL)
            .onRight { furnaceFuels.add(it.itemHolder, FurnaceFuel(20 * 10 * 80), false) }

        registerOxidizables(HCBlocks.COPPER_BASIN)
        registerWaxed(HCBlocks.COPPER_BASIN)
    }

    private fun registerOxidizables(block: WeatheringCopperCollection<out SimpleSupplierWithKey<Block>>) {
        val builder: Builder<Oxidizable, Block> = builder(NeoForgeDataMaps.OXIDIZABLES)
        val (unaffected: SimpleSupplierWithKey<Block>, exposed: SimpleSupplierWithKey<Block>, weathered: SimpleSupplierWithKey<Block>, oxidized: SimpleSupplierWithKey<Block>) = block.weathering
        builder.add(unaffected.getKey(), Oxidizable(exposed.get()), false)
        builder.add(exposed.getKey(), Oxidizable(weathered.get()), false)
        builder.add(weathered.getKey(), Oxidizable(oxidized.get()), false)
    }

    private fun registerWaxed(block: WeatheringCopperCollection<out SimpleSupplierWithKey<Block>>) {
        val builder: Builder<Waxable, Block> = builder(NeoForgeDataMaps.WAXABLES)
        for (state: WeatheringCopper.WeatherState in WeatheringCopper.WeatherState.entries) {
            val (base: SimpleSupplierWithKey<Block>, waxed: SimpleSupplierWithKey<Block>) = block[state]
            builder.add(base.getKey(), Waxable(waxed.get()), false)
        }
    }
}

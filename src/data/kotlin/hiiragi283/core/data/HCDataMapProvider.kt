package hiiragi283.core.data

import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.registry.HTWeatheringCopperBlocks
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.SupplierWithId
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopper
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable

class HCDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(HCBlocks.CHARCOAL_BLOCK.itemHolder, FurnaceFuel(20 * 10 * 80), false)

        registerOxidizables(HCBlocks.COPPER_BASIN)
        registerWaxed(HCBlocks.COPPER_BASIN)
    }

    private fun registerOxidizables(block: HTWeatheringCopperBlocks<*, *, *>) {
        val builder: Builder<Oxidizable, Block> = builder(NeoForgeDataMaps.OXIDIZABLES)
        val (unaffected: SupplierWithId<Block>, exposed: SupplierWithId<Block>, weathered: SupplierWithId<Block>, oxidized: SupplierWithId<Block>) = block.weathering
        builder.add(unaffected.getId(), Oxidizable(exposed.get()), false)
        builder.add(exposed.getId(), Oxidizable(weathered.get()), false)
        builder.add(weathered.getId(), Oxidizable(oxidized.get()), false)
    }

    private fun registerWaxed(block: HTWeatheringCopperBlocks<*, *, *>) {
        val builder: Builder<Waxable, Block> = builder(NeoForgeDataMaps.WAXABLES)
        for (state: WeatheringCopper.WeatherState in WeatheringCopper.WeatherState.entries) {
            val (base: HTIdLike, waxed: SupplierWithId<Block>) = block[state]
            builder.add(base.getId(), Waxable(waxed.get()), false)
        }
    }
}

package hiiragi283.core.data

import hiiragi283.core.api.copper.HTCopperPhase
import hiiragi283.core.api.copper.HTWeatheringCoppers
import hiiragi283.core.api.data.map.HTDataMapProvider
import hiiragi283.core.api.resource.SimpleSupplierWithKey
import hiiragi283.core.setup.HCBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable
import java.util.concurrent.CompletableFuture

class HCDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTDataMapProvider(packOutput, lookupProvider) {
    override fun gatherInternal() {
        furnaceFuel {
            add(HCBlocks.OIL_SAND, FurnaceFuel(20 * 10 * 4))
            add(HCBlocks.OIL_SHALE, FurnaceFuel(20 * 10 * 4))
        }

        registerOxidizables(HCBlocks.COPPER_BASIN)
        registerWaxed(HCBlocks.COPPER_BASIN)
    }

    private fun registerOxidizables(block: HTWeatheringCoppers<SimpleSupplierWithKey<Block>>) {
        val builder: Builder<Oxidizable, Block> = builder(NeoForgeDataMaps.OXIDIZABLES)
        val (unaffected: SimpleSupplierWithKey<Block>, exposed: SimpleSupplierWithKey<Block>, weathered: SimpleSupplierWithKey<Block>, oxidized: SimpleSupplierWithKey<Block>) = block.weathering
        builder.add(unaffected.getKey(), Oxidizable(exposed.get()), false)
        builder.add(exposed.getKey(), Oxidizable(weathered.get()), false)
        builder.add(weathered.getKey(), Oxidizable(oxidized.get()), false)
    }

    private fun registerWaxed(block: HTWeatheringCoppers<SimpleSupplierWithKey<Block>>) {
        val builder: Builder<Waxable, Block> = builder(NeoForgeDataMaps.WAXABLES)
        for (phase: HTCopperPhase in HTCopperPhase.entries) {
            val (base: SimpleSupplierWithKey<Block>, waxed: SimpleSupplierWithKey<Block>) = block[phase]
            builder.add(base.getKey(), Waxable(waxed.get()), false)
        }
    }
}

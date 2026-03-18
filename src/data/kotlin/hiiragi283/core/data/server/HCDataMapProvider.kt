package hiiragi283.core.data.server

import hiiragi283.core.api.block.HTWeatheringBlockMap
import hiiragi283.core.api.block.HTWeatheringLevel
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.map.HTDataMapProvider
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.setup.HCBlocks
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable

class HCDataMapProvider(context: HTDataGenContext) : HTDataMapProvider(context) {
    override fun gatherInternal() {
        furnaceFuel {
            addHolder(HCBlocks.OIL_SAND, FurnaceFuel(20 * 10 * 4))
            addHolder(HCBlocks.OIL_SHALE, FurnaceFuel(20 * 10 * 4))
        }

        registerOxidizables(HCBlocks.COPPER_BASINS.base)

        registerWaxed(HCBlocks.COPPER_BASINS)
    }

    private fun registerOxidizables(map: Map<HTWeatheringLevel, HTBlockHolderLike<*>>) {
        val builder: Builder<Oxidizable, Block> = builder(NeoForgeDataMaps.OXIDIZABLES)
        for (level: HTWeatheringLevel in HTWeatheringLevel.entries) {
            val previous: HTIdLike = map[level] ?: continue
            val next: HTBlockHolderLike<*> = HTWeatheringLevel.entries.getOrNull(level.ordinal + 1)?.let(map::get) ?: continue
            builder.addHolder(previous, Oxidizable(next.get()))
        }
    }

    private fun registerWaxed(map: HTWeatheringBlockMap) {
        val builder: Builder<Waxable, Block> = builder(NeoForgeDataMaps.WAXABLES)
        for (level: HTWeatheringLevel in HTWeatheringLevel.entries) {
            val (base: HTIdLike, waxed: HTBlockHolderLike<*>) = map[level] ?: continue
            builder.addHolder(base, Waxable(waxed.get()))
        }
    }
}

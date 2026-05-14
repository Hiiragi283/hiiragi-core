package hiiragi283.core.data

import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.registry.HTDeferredBlockAndItem
import hiiragi283.lib.registry.HTWeatheringCopperBlocks
import hiiragi283.lib.resource.HTIdLike
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.neoforged.neoforge.registries.datamaps.builtin.Oxidizable
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable

class HCDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : DataMapProvider(packOutput, lookupProvider) {
    override fun gather(provider: HolderLookup.Provider) {
        registerOxidizables(HCBlocks.COPPER_BASIN)
        registerWaxed(HCBlocks.COPPER_BASIN)
    }

    private fun registerOxidizables(block: HTWeatheringCopperBlocks<*, *, *>) {
        val builder: Builder<Oxidizable, Block> = builder(NeoForgeDataMaps.OXIDIZABLES)
        val blocks: List<HTDeferredBlockAndItem<*, *>> = block.weatheringBlocks
        for (i: Int in blocks.indices) {
            val previous: HTIdLike = blocks[i]
            val next: HTDeferredBlockAndItem<*, *> = blocks.getOrNull(i + 1) ?: continue
            builder.add(previous.getId(), Oxidizable(next.get()), false)
        }
    }

    private fun registerWaxed(block: HTWeatheringCopperBlocks<*, *, *>) {
        val builder: Builder<Waxable, Block> = builder(NeoForgeDataMaps.WAXABLES)
        for ((base: HTIdLike, waxed: HTDeferredBlockAndItem<*, *>) in block.weatheringBlocks.zip(block.waxedBlocks)) {
            builder.add(base.getId(), Waxable(waxed.get()), false)
        }
    }
}

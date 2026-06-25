package hiiragi283.lib.registry

import hiiragi283.lib.copper.HTWeatheringCoppers
import net.minecraft.world.level.block.Block

typealias HTWeatheringCopperBlocks<WAXED, WEATHERING> = HTWeatheringCoppers<HTDeferredBlockAndItem<WAXED, *>, HTDeferredBlockAndItem<WEATHERING, *>>

val HTWeatheringCopperBlocks<Block, Block>.allBlocks: List<HTDeferredBlockAndItem<Block, *>> get() = this.weathering + this.waxed

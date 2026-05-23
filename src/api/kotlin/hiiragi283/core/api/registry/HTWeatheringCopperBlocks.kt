package hiiragi283.core.api.registry

import hiiragi283.core.api.function.Identity
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.item.HTBlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockBehaviour

data class HTWeatheringCopperBlocks<WAXED : Block, WEATHERING, ITEM : Item>(
    val unaffected: HTDeferredBlockAndItem<WEATHERING, ITEM>,
    val exposed: HTDeferredBlockAndItem<WEATHERING, ITEM>,
    val weathered: HTDeferredBlockAndItem<WEATHERING, ITEM>,
    val oxidized: HTDeferredBlockAndItem<WEATHERING, ITEM>,
    val waxed: HTDeferredBlockAndItem<WAXED, ITEM>,
    val waxedExposed: HTDeferredBlockAndItem<WAXED, ITEM>,
    val waxedWeathered: HTDeferredBlockAndItem<WAXED, ITEM>,
    val waxedOxidized: HTDeferredBlockAndItem<WAXED, ITEM>,
) where WEATHERING : Block, WEATHERING : WeatheringCopper {
    companion object {
        @JvmStatic
        fun <WAXED : Block, WEATHERING> createSimple(
            register: HTDeferredBlockAndItemRegister,
            name: String,
            blockProp: (WeatheringCopper.WeatherState) -> BlockBehaviour.Properties,
            waxedFactory: BlockFactory<WAXED>,
            weatheringFactory: BlockWithContextFactory<WeatheringCopper.WeatherState, WEATHERING>,
            itemProp: Identity<Item.Properties> = identity(),
        ): HTWeatheringCopperBlocks<WAXED, WEATHERING, HTBlockItem<Block>> where WEATHERING : Block, WEATHERING : WeatheringCopper = create(register, name, blockProp, waxedFactory, weatheringFactory, ::HTBlockItem, itemProp)

        @JvmStatic
        fun <WAXED : Block, WEATHERING, ITEM : Item> create(
            register: HTDeferredBlockAndItemRegister,
            name: String,
            blockProp: (WeatheringCopper.WeatherState) -> BlockBehaviour.Properties,
            waxedFactory: BlockFactory<WAXED>,
            weatheringFactory: BlockWithContextFactory<WeatheringCopper.WeatherState, WEATHERING>,
            itemFactory: ItemWithContextFactory<Block, ITEM>,
            itemProp: Identity<Item.Properties> = identity(),
        ): HTWeatheringCopperBlocks<WAXED, WEATHERING, ITEM> where WEATHERING : Block, WEATHERING : WeatheringCopper = HTWeatheringCopperBlocks(
            register.register(name, blockProp(WeatheringCopper.WeatherState.UNAFFECTED), { weatheringFactory(WeatheringCopper.WeatherState.UNAFFECTED, it) }, itemFactory, itemProp),
            register.register("exposed_$name", blockProp(WeatheringCopper.WeatherState.EXPOSED), { weatheringFactory(WeatheringCopper.WeatherState.EXPOSED, it) }, itemFactory, itemProp),
            register.register("weathered_$name", blockProp(WeatheringCopper.WeatherState.WEATHERED), { weatheringFactory(WeatheringCopper.WeatherState.WEATHERED, it) }, itemFactory, itemProp),
            register.register("oxidized_$name", blockProp(WeatheringCopper.WeatherState.OXIDIZED), { weatheringFactory(WeatheringCopper.WeatherState.OXIDIZED, it) }, itemFactory, itemProp),
            register.register("waxed_$name", blockProp(WeatheringCopper.WeatherState.UNAFFECTED), waxedFactory, itemFactory, itemProp),
            register.register("waxed_exposed_$name", blockProp(WeatheringCopper.WeatherState.EXPOSED), waxedFactory, itemFactory, itemProp),
            register.register("waxed_weathered_$name", blockProp(WeatheringCopper.WeatherState.WEATHERED), waxedFactory, itemFactory, itemProp),
            register.register("waxed_oxidized_$name", blockProp(WeatheringCopper.WeatherState.OXIDIZED), waxedFactory, itemFactory, itemProp),
        )
    }

    val weatheringBlocks: List<HTDeferredBlockAndItem<WEATHERING, ITEM>> get() = listOf(unaffected, exposed, weathered, oxidized)
    val waxedBlocks: List<HTDeferredBlockAndItem<WAXED, ITEM>> get() = listOf(waxed, waxedExposed, waxedWeathered, waxedOxidized)

    val weatheringMap: Map<WeatheringCopper.WeatherState, HTDeferredBlockAndItem<WEATHERING, ITEM>> = mapOf(
        WeatheringCopper.WeatherState.UNAFFECTED to unaffected,
        WeatheringCopper.WeatherState.EXPOSED to exposed,
        WeatheringCopper.WeatherState.WEATHERED to weathered,
        WeatheringCopper.WeatherState.OXIDIZED to oxidized,
    )

    val waxedMap: Map<WeatheringCopper.WeatherState, HTDeferredBlockAndItem<WAXED, ITEM>> = mapOf(
        WeatheringCopper.WeatherState.UNAFFECTED to waxed,
        WeatheringCopper.WeatherState.EXPOSED to waxedExposed,
        WeatheringCopper.WeatherState.WEATHERED to waxedWeathered,
        WeatheringCopper.WeatherState.OXIDIZED to waxedOxidized,
    )
}

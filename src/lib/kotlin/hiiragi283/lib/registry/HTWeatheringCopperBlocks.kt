package hiiragi283.lib.registry

import hiiragi283.lib.item.HTBlockItem
import java.util.function.UnaryOperator
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockBehaviour

/**
 * @see net.minecraft.world.level.block.WeatheringCopperBlocks
 * @see net.minecraft.world.item.WeatheringCopperItems
 */
@JvmRecord
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
            itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
        ): HTWeatheringCopperBlocks<WAXED, WEATHERING, HTBlockItem<Block>> where WEATHERING : Block, WEATHERING : WeatheringCopper = create(register, name, blockProp, waxedFactory, weatheringFactory, ::HTBlockItem, itemProp)

        @JvmStatic
        fun <WAXED : Block, WEATHERING, ITEM : Item> create(
            register: HTDeferredBlockAndItemRegister,
            name: String,
            blockProp: (WeatheringCopper.WeatherState) -> BlockBehaviour.Properties,
            waxedFactory: BlockFactory<WAXED>,
            weatheringFactory: BlockWithContextFactory<WeatheringCopper.WeatherState, WEATHERING>,
            itemFactory: ItemWithContextFactory<Block, ITEM>,
            itemProp: UnaryOperator<Item.Properties> = UnaryOperator.identity(),
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

    operator fun get(state: WeatheringCopper.WeatherState): Pair<HTDeferredBlockAndItem<WEATHERING, ITEM>, HTDeferredBlockAndItem<WAXED, ITEM>> = when (state) {
        WeatheringCopper.WeatherState.UNAFFECTED -> unaffected to waxed
        WeatheringCopper.WeatherState.EXPOSED -> exposed to waxedExposed
        WeatheringCopper.WeatherState.WEATHERED -> weathered to waxedWeathered
        WeatheringCopper.WeatherState.OXIDIZED -> oxidized to waxedOxidized
    }

    fun asSequence(): Sequence<HTDeferredBlockAndItem<*, ITEM>> = sequence {
        yieldAll(weatheringBlocks)
        yieldAll(waxedBlocks)
    }
}

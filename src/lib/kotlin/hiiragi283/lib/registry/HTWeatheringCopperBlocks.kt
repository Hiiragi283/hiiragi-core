package hiiragi283.lib.registry

import hiiragi283.lib.item.HTBlockItem
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockBehaviour

/**
 * 酸化する銅系ブロックとさび止めされた銅系ブロックを束ねたクラスです。
 * @param WAXED さび止めされた銅系ブロックのクラス
 * @param WEATHERING 酸化する銅系ブロックのクラス
 * @param ITEM 銅系アイテムのクラス
 * @param weathering さび止めされた銅系ブロックの一覧
 * @param waxed 酸化する銅系ブロックの一覧
 * @author Hiiragi Tsubas
 * @since 26.1.0
 */
@JvmRecord
data class HTWeatheringCopperBlocks<WAXED : Block, WEATHERING, ITEM : Item>(
    val weathering: HTCopperMap<HTDeferredBlockAndItem<WEATHERING, ITEM>>,
    val waxed: HTCopperMap<HTDeferredBlockAndItem<WAXED, ITEM>>,
) where WEATHERING : Block, WEATHERING : WeatheringCopper {
    companion object {
        /**
         * 新しい[HTWeatheringCopperBlocks]のインスタンスを作成します。
         * @param WAXED さび止めされた銅系ブロックのクラス
         * @param WEATHERING 酸化する銅系ブロックのクラス
         */
        @JvmStatic
        fun <WAXED : Block, WEATHERING> createSimple(
            register: HTDeferredBlockAndItemRegister,
            name: String,
            blockProp: (WeatheringCopper.WeatherState) -> BlockBehaviour.Properties,
            waxedFactory: BlockFactory<WAXED>,
            weatheringFactory: BlockWithContextFactory<WeatheringCopper.WeatherState, WEATHERING>,
            itemProp: Identity<Item.Properties> = identity(),
        ): HTWeatheringCopperBlocks<WAXED, WEATHERING, HTBlockItem<Block>> where WEATHERING : Block, WEATHERING : WeatheringCopper = create(register, name, blockProp, waxedFactory, weatheringFactory, ::HTBlockItem, itemProp)

        /**
         * 新しい[HTWeatheringCopperBlocks]のインスタンスを作成します。
         * @param WAXED さび止めされた銅系ブロックのクラス
         * @param WEATHERING 酸化する銅系ブロックのクラス
         * @param ITEM 銅系アイテムのクラス
         */
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
            HTCopperMap(
                register.register(name, blockProp(WeatheringCopper.WeatherState.UNAFFECTED), { weatheringFactory(WeatheringCopper.WeatherState.UNAFFECTED, it) }, itemFactory, itemProp),
                register.register("exposed_$name", blockProp(WeatheringCopper.WeatherState.EXPOSED), { weatheringFactory(WeatheringCopper.WeatherState.EXPOSED, it) }, itemFactory, itemProp),
                register.register("weathered_$name", blockProp(WeatheringCopper.WeatherState.WEATHERED), { weatheringFactory(WeatheringCopper.WeatherState.WEATHERED, it) }, itemFactory, itemProp),
                register.register("oxidized_$name", blockProp(WeatheringCopper.WeatherState.OXIDIZED), { weatheringFactory(WeatheringCopper.WeatherState.OXIDIZED, it) }, itemFactory, itemProp),
            ),
            HTCopperMap(
                register.register("waxed_$name", blockProp(WeatheringCopper.WeatherState.UNAFFECTED), waxedFactory, itemFactory, itemProp),
                register.register("waxed_exposed_$name", blockProp(WeatheringCopper.WeatherState.EXPOSED), waxedFactory, itemFactory, itemProp),
                register.register("waxed_weathered_$name", blockProp(WeatheringCopper.WeatherState.WEATHERED), waxedFactory, itemFactory, itemProp),
                register.register("waxed_oxidized_$name", blockProp(WeatheringCopper.WeatherState.OXIDIZED), waxedFactory, itemFactory, itemProp),
            ),
        )
    }

    val allBlocks: List<HTDeferredBlockAndItem<Block, ITEM>> get() = weathering.values + waxed.values

    /**
     * 指定した[WeatheringCopper.WeatherState][state]から対応する銅系ブロックを取得します。
     * @return 対応する酸化する銅系ブロックとさび止めされた銅系ブロック
     */
    operator fun get(state: WeatheringCopper.WeatherState): Pair<HTDeferredBlockAndItem<WEATHERING, ITEM>, HTDeferredBlockAndItem<WAXED, ITEM>> = weathering[state] to waxed[state]
}

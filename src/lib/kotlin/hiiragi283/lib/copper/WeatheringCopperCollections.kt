package hiiragi283.lib.copper

import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.WeatheringCopperCollection

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> WeatheringCopperCollection<T>.get(phase: HTCopperPhase): Pair<T, T> = this[phase.toState()]

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> WeatheringCopperCollection<T>.get(state: WeatheringCopper.WeatherState): Pair<T, T> = when (state) {
    WeatheringCopper.WeatherState.UNAFFECTED -> this.weathering().unaffected() to this.waxed().unaffected()
    WeatheringCopper.WeatherState.EXPOSED -> this.weathering().exposed() to this.waxed().exposed()
    WeatheringCopper.WeatherState.WEATHERED -> this.weathering().weathered() to this.waxed().weathered()
    WeatheringCopper.WeatherState.OXIDIZED -> this.weathering().oxidized() to this.waxed().oxidized()
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
fun <T : Any> WeatheringCopperCollection<T>.base(): T = this.weathering().unaffected()

//    ByState    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> WeatheringCopperCollection.ByState<T>.get(phase: HTCopperPhase): T = this[phase.toState()]

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> WeatheringCopperCollection.ByState<T>.get(state: WeatheringCopper.WeatherState): T = when (state) {
    WeatheringCopper.WeatherState.UNAFFECTED -> this.unaffected()
    WeatheringCopper.WeatherState.EXPOSED -> this.exposed()
    WeatheringCopper.WeatherState.WEATHERED -> this.weathered()
    WeatheringCopper.WeatherState.OXIDIZED -> this.oxidized()
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> WeatheringCopperCollection.ByState<T>.component1(): T = this.unaffected()

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> WeatheringCopperCollection.ByState<T>.component2(): T = this.exposed()

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> WeatheringCopperCollection.ByState<T>.component3(): T = this.weathered()

/**
 * @author Hiiragi Tsubasa
 * @since 26.2.0
 */
operator fun <T : Any> WeatheringCopperCollection.ByState<T>.component4(): T = this.oxidized()

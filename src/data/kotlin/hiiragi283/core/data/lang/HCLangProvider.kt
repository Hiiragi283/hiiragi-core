package hiiragi283.core.data.lang

import hiiragi283.core.setup.HCBlocks
import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangType
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.lib.text.HTTranslation
import java.util.function.BiConsumer
import net.minecraft.world.level.block.WeatheringCopper

interface HCLangProvider {
    fun addCommonTranslations(consumer: BiConsumer<HTTranslation, String>) {
        // API - Constants
        consumer.accept(HTCommonTranslation.TRUE, "True")
        consumer.accept(HTCommonTranslation.FALSE, "False")
        // API - GUI
        consumer.accept(HTCommonTranslation.CAPACITY, $$"Capacity: %1$s")
        consumer.accept(HTCommonTranslation.CAPACITY_MB, $$"Capacity: %1$s mB")
        consumer.accept(HTCommonTranslation.CAPACITY_FE, $$"Capacity: %1$s FE")

        consumer.accept(HTCommonTranslation.STORED, $$"%1$s: %2$s")
        consumer.accept(HTCommonTranslation.STORED_MB, $$"%1$s: %2$s mB")
        consumer.accept(HTCommonTranslation.STORED_FE, $$"%1$s FE")
        consumer.accept(HTCommonTranslation.STORED_EXP, $$"%1$s Exp")

        consumer.accept(HTCommonTranslation.FRACTION, $$"%1$s / %2$s")
        consumer.accept(HTCommonTranslation.PERCENTAGE, $$"%1$s %%")

        consumer.accept(HTCommonTranslation.TICK, $$"%1$s ticks")
    }

    fun addPatternTranslations(provider: HTLangProvider) {
        val langType: HTLangType = provider.langType
        // Block
        val waxedCopper: HTLangPatternProvider = HTLangPatternProvider.create("Waxed %s", "錆止めされた%s")

        val copperBasin: HTLangName = HTLangName.create("Copper Basin", "銅の鉢")
        for ((state: WeatheringCopper.WeatherState, block: HTHasTranslationKey) in HCBlocks.COPPER_BASIN.weathering) {
            val pattern: HTLangPatternProvider = getCopperLangPattern(state)
            provider.add(block, pattern.translate(langType, copperBasin))
        }
        for ((state: WeatheringCopper.WeatherState, block: HTHasTranslationKey) in HCBlocks.COPPER_BASIN.waxed) {
            val pattern: HTLangPatternProvider = getCopperLangPattern(state)
            provider.add(block, waxedCopper.translate(langType, pattern.translate(langType, copperBasin)))
        }
        // Fluid
        /*val dyePattern: HTLangPatternProvider = HTLangPatternProvider.create("%s Dye", "%sの染料")
        for ((color: HTLangName, fluid: HTFluidContent) in HCFluids.DyeContents) {
            provider.addFluid(fluid, dyePattern.translate(langType, color))
        }*/
    }

    private fun getCopperLangPattern(state: WeatheringCopper.WeatherState): HTLangPatternProvider = when (state) {
        WeatheringCopper.WeatherState.UNAFFECTED -> HTLangPatternProvider.IDENTITY
        WeatheringCopper.WeatherState.EXPOSED -> HTLangPatternProvider.create("Exposed %s", "風化した%s")
        WeatheringCopper.WeatherState.WEATHERED -> HTLangPatternProvider.create("Weathered %s", "錆びた%s")
        WeatheringCopper.WeatherState.OXIDIZED -> HTLangPatternProvider.create("Oxidized %s", "酸化した%s")
    }
}

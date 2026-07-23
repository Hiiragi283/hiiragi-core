package hiiragi283.core.data.lang

import hiiragi283.core.api.copper.HTCopperPhase
import hiiragi283.core.api.data.lang.HTLangName
import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangProvider
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
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
        val waxedCopper = HTLangPatternProvider("Waxed %s", "錆止めされた%s")
        val copperBasin = HTLangName("Copper Basin", "銅の鉢")
        for (phase: HTCopperPhase in HTCopperPhase.entries) {
            val (weathering: HTHasTranslationKey, waxed: HTHasTranslationKey) = HCBlocks.COPPER_BASIN[phase]
            provider.add(weathering, phase.translate(langType, copperBasin))
            provider.add(waxed, waxedCopper.translate(langType, phase.translate(langType, copperBasin)))
        }
        // Fluid
        val dyePattern = HTLangPatternProvider("%s Dye", "%sの染料")
        for ((color: HTLangName, content: HTFluidContent) in HCFluids.DYES.asSequenceWithColor()) {
            provider.addFluid(content, dyePattern.translate(langType, color))
        }
    }

    private fun getCopperLangPattern(state: WeatheringCopper.WeatherState): HTLangPatternProvider = when (state) {
        WeatheringCopper.WeatherState.UNAFFECTED -> HTLangPatternProvider.IDENTITY
        WeatheringCopper.WeatherState.EXPOSED -> HTLangPatternProvider("Exposed %s", "風化した%s")
        WeatheringCopper.WeatherState.WEATHERED -> HTLangPatternProvider("Weathered %s", "錆びた%s")
        WeatheringCopper.WeatherState.OXIDIZED -> HTLangPatternProvider("Oxidized %s", "酸化した%s")
    }
}

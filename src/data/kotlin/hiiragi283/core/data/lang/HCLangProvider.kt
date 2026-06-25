package hiiragi283.core.data.lang

import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.lib.copper.HTCopperPhase
import hiiragi283.lib.data.lang.HTLangName
import hiiragi283.lib.data.lang.HTLangPatternProvider
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangType
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTHasTranslationKey
import hiiragi283.lib.text.HTTranslation
import java.util.function.BiConsumer

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
        for (phase: HTCopperPhase in HTCopperPhase.entries) {
            val weathering: HTHasTranslationKey = HCBlocks.COPPER_BASIN.weathering[phase]
            provider.add(weathering, phase.translate(langType, copperBasin))
            val waxed: HTHasTranslationKey = HCBlocks.COPPER_BASIN.waxed[phase]
            provider.add(waxed, waxedCopper.translate(langType, phase.translate(langType, copperBasin)))
        }
        // Fluid
        val dyePattern: HTLangPatternProvider = HTLangPatternProvider.create("%s Dye", "%sの染料")
        for ((color: HTLangName, content: HTFluidContent) in HCFluids.DYES.asSequenceWithColor()) {
            provider.addFluid(content, dyePattern.translate(langType, color))
        }
    }
}

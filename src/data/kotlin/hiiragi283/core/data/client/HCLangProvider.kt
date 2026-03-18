package hiiragi283.core.data.client

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
        val waxed: HTLangPatternProvider = HTLangPatternProvider.create("Waxed %s", "錆止めされた%s")
        
        val copperBasin: HTLangName = HTLangName.create("Copper Basin", "銅の鉢")
        for ((level: HTLangPatternProvider, block: HTHasTranslationKey) in HCBlocks.COPPER_BASINS) {
            provider.add(block, level.translate(langType, copperBasin))
        }
        for ((level: HTLangPatternProvider, block: HTHasTranslationKey) in HCBlocks.WAXED_COPPER_BASINS) {
            provider.add(block, waxed.translate(langType, level.translate(langType, copperBasin)))
        }
        // Fluid
        val dyePattern: HTLangPatternProvider = HTLangPatternProvider.create("%s Dye", "%sの染料")
        for ((color: HTLangName, fluid: HTFluidContent) in HCFluids.DYE) {
            provider.addFluid(fluid, dyePattern.translate(langType, color))
        }
    }
}

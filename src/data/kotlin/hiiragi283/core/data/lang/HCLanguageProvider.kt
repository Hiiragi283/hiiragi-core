package hiiragi283.core.data.lang

import hiiragi283.core.api.data.lang.HTLangPatternProvider
import hiiragi283.core.api.data.lang.HTLangType
import hiiragi283.core.api.data.lang.HTLanguageProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.item.DyeColor
import java.util.function.BiConsumer
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

interface HCLanguageProvider {
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

    fun addPatternTranslations(provider: HTLanguageProvider) {
        val langType: HTLangType = provider.langType
        // Fluid
        val dyePattern: HTLangPatternProvider = HTLangPatternProvider.create("%s Dye", "%sの染料")
        for ((color: DyeColor, fluid: HTFluidContent) in HCFluids.DYE) {
            provider.addFluid(fluid, dyePattern.translate(langType, color.name))
        }
    }
}

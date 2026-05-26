package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) :
    HTLangProvider(output, HiiragiCoreAPI.MOD_ID, HTLangTypes.EN_US),
    HCLangProvider {
    override fun addTranslations() {
        addCommonTranslations(this::add)
        addPatternTranslations(this)

        // Block
        add(HCBlocks.CHARCOAL_BLOCK, "Block of Charcoal")
        add(HCBlocks.ECHO_BLOCK, "Block of Echo")

        add(HCBlocks.WARPED_WART, "Warped Wart")

        add(HCBlocks.CHOPPING_BOARD, "Chopping Board")
        // Fluid
        addFluid(HCFluids.EXPERIENCE, "Liquid Experience")
        addFluid(HCFluids.HONEY, "Honey")
        addFluid(HCFluids.MUSHROOM_STEW, "Mushroom Stew")
        addFluid(HCFluids.DRAGON_BREATH, "Dragon Breath")
        add(HCFluids.POTION.getFluidType().descriptionId, "Invalid Potion Bucket")
        add(HCFluids.POTION.bucketHolder, $$"%1$s Bucket")
        addFluid(HCFluids.OMINOUS_FLUX, "Ominous Flux")

        addFluid(HCFluids.LATEX, "Latex")
        addFluid(HCFluids.MEAT, "Meat")
        // Item
        add(HCItems.NETHERITE_NUGGET, "Netherite Nugget")
        // Recipe
        add(HCRecipeTypes.CHARGING, "Charging")
        add(HCRecipeTypes.CHOPPING, "Chopping")
        add(HCRecipeTypes.EXPLODING, "Exploding")

        add(HCRecipeTypes.EMPTYING, "Emptying Container")
        add(HCRecipeTypes.FILLING, "Filling Container")
    }
}

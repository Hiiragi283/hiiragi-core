package hiiragi283.core.data.lang

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HCTranslation
import hiiragi283.core.common.recipe.VanillaRecipeLookups
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCEnchantments
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import hiiragi283.lib.data.lang.HTLangProvider
import hiiragi283.lib.data.lang.HTLangTypes
import hiiragi283.lib.text.HTCommonTranslation
import net.minecraft.data.PackOutput

class HCEnglishLangProvider(output: PackOutput) :
    HTLangProvider(output, HiiragiCoreAPI.MOD_ID, HTLangTypes.EN_US),
    HCLangProvider {
    override fun addTranslations() {
        addCommonTranslations(this::add)
        addPatternTranslations(this)

        // Block
        addMaterials(HCBlocks.RESOURCES)

        add(HCBlocks.WARPED_WART, "Warped Wart")

        add(HCBlocks.CHOPPING_BOARD, "Chopping Board")
        add(HCBlocks.FORGING_ANVIL, "Forging Anvil")
        // Enchantment
        add(HCEnchantments.HAMMER_OF_JUSTICE, "Hammer of Justice", "Increases damage against raiders.")
        add(HCEnchantments.NOISE_CANCELING, "Noise Canceling", "Increases damage against sculk mobs such as Warden.")
        add(HCEnchantments.PURIFICATION, "Purification", "Increases damage against wither mobs.")

        add(HCEnchantments.SONIC_PROTECTION, "Sonic Protection", "Immune damage from sonic boom.")
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
        addMaterials(HCItems.RESOURCES)

        add(HCItems.ELDER_HEART, "Elder Heart")

        add(HCItems.SYNTHETIC_FEATHER, "Synthetic Feather")
        add(HCItems.SYNTHETIC_FIBER, "Synthetic Fiber")
        add(HCItems.SYNTHETIC_LEATHER, "Synthetic Leather")

        add(HCItems.IRIDESCENT_POWDER, "Iridescent Powder")
        // add(HCItems.ALMIGHTY_PICKAXE, "Almighty Pickaxe")
        add(HCItems.AMBROSIA, "Ambrosia")
        add(HCItems.ETERNAL_UPGRADE, "Eternal Smithing Template")
        add(HCItems.POTION_OF_INFINITY, "Potion of Infinity")
        add(HCItems.RING_OF_HYPERION, "Ring of Hyperion")
        // Recipe
        add(VanillaRecipeLookups.SMELTING, "Smelting")
        add(VanillaRecipeLookups.BLASTING, "Blasting")
        add(VanillaRecipeLookups.SMOKING, "Smoking")
        add(VanillaRecipeLookups.BREWING, "Brewing")

        add(HCRecipeTypes.CHARGING, "Charging")
        add(HCRecipeTypes.CHOPPING, "Chopping")
        add(HCRecipeTypes.CRUSHING, "Crushing")
        add(HCRecipeTypes.EXPLODING, "Exploding")

        add(HCRecipeTypes.EMPTYING, "Emptying Container")
        add(HCRecipeTypes.FILLING, "Filling Container")

        // Text - Hiiragi Series
        add(HTCommonTranslation.ERROR, "Error")
        add(HTCommonTranslation.INFINITE, "Infinite")
        add(HTCommonTranslation.NONE, "None")
        add(HTCommonTranslation.EMPTY, "Empty")

        add(HTCommonTranslation.DOWN, "Down")
        add(HTCommonTranslation.UP, "Up")
        add(HTCommonTranslation.NORTH, "North")
        add(HTCommonTranslation.SOUTH, "South")
        add(HTCommonTranslation.WEST, "West")
        add(HTCommonTranslation.EAST, "East")

        add(HTCommonTranslation.INVALID_PACKET_S2C, $$"Invalid packet received from server side: %1$s")
        add(HTCommonTranslation.INVALID_PACKET_C2S, $$"Invalid packet received from client side: %1$s")

        add(HTCommonTranslation.PROGRESS, $$"Progress: %1$s %%")
        add(HTCommonTranslation.SECONDS, $$"%1$s sec (%2$s ticks)")

        add(HTCommonTranslation.CHANCE_PRODUCE, $$"Production Chance: %1$s %%")

        add(HTCommonTranslation.TOOLTIP_INTRINSIC_ENCHANTMENT, $$"Always has at least %1$s")
        add(HTCommonTranslation.TOOLTIP_SHOW_DESCRIPTION, "Press Shift to show description")
        add(HTCommonTranslation.TOOLTIP_SHOW_DETAILS, "Press Ctrl to show details")

        add(HTCommonTranslation.DATAPACK_WIP, "Enables work in progress contents")
        // Text - Hiiragi Core
        add(HCTranslation.HIIRAGI_CORE, "Hiiragi Core")

        add(HCTranslation.ETERNAL_PICKAXE, "Eternal Pickaxe")

        add(HCTranslation.ETERNAL_UPGRADE_APPLIES_TO, "Any Equipment")
        add(HCTranslation.ETERNAL_UPGRADE_INGREDIENTS, "Iridium Ingot")
        add(HCTranslation.ETERNAL_UPGRADE_BASE_SLOT_DESCRIPTION, "Add any armor, weapon, or tool")
        add(HCTranslation.ETERNAL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, "Add Iridium Ingot")
    }
}

package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.registry.HTDeferredMaterialContents
import hiiragi283.lib.registry.HTDeferredMaterialContentsRegister
import hiiragi283.lib.tag.CommonTagPrefixes
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

data object HCMaterialContents {
    @JvmField
    val REGISTER = HTDeferredMaterialContentsRegister(HiiragiCoreAPI.MOD_ID)

    //    Fuel    //

    // Vanilla
    @JvmField
    val COAL: HTDeferredMaterialContents = REGISTER.registerContents("coal", CommonPartKeys.FUEL) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.COAL_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.FUEL, HTMaterialItemEntry.item(Items.COAL))
    }

    @JvmField
    val CHARCOAL: HTDeferredMaterialContents = REGISTER.registerContents("charcoal", CommonPartKeys.FUEL) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(HCBlocks.CHARCOAL_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.FUEL, HTMaterialItemEntry.item(Items.CHARCOAL))
    }

    // Common

    // Modded

    //    Minerals    //

    // Vanilla
    @JvmField
    val REDSTONE: HTDeferredMaterialContents = REGISTER.registerContents("redstone", CommonPartKeys.DUST) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.REDSTONE_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.DUST, HTMaterialItemEntry.item(Items.REDSTONE), CommonTagPrefixes.DUST)
    }

    @JvmField
    val GLOWSTONE: HTDeferredMaterialContents = REGISTER.registerContents("glowstone", CommonPartKeys.DUST) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.GLOWSTONE), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.DUST, HTMaterialItemEntry.item(Items.GLOWSTONE_DUST), CommonTagPrefixes.DUST)
    }

    // Common

    // Modded

    //    Gem    //

    // Vanilla
    @JvmField
    val LAPIS: HTDeferredMaterialContents = REGISTER.registerContents("lapis", CommonPartKeys.GEM) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.LAPIS_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.GEM, HTMaterialItemEntry.item(Items.LAPIS_LAZULI), CommonTagPrefixes.GEM)
    }

    @JvmField
    val QUARTZ: HTDeferredMaterialContents = REGISTER.registerContents("quartz", CommonPartKeys.GEM) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.QUARTZ_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.GEM, HTMaterialItemEntry.item(Items.QUARTZ), CommonTagPrefixes.GEM)
    }

    @JvmField
    val AMETHYST: HTDeferredMaterialContents = REGISTER.registerContents("amethyst", CommonPartKeys.GEM) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.AMETHYST_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.GEM, HTMaterialItemEntry.item(Items.AMETHYST_SHARD), CommonTagPrefixes.GEM)
    }

    @JvmField
    val DIAMOND: HTDeferredMaterialContents = REGISTER.registerContents("diamond", CommonPartKeys.GEM) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.DIAMOND_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.GEM, HTMaterialItemEntry.item(Items.DIAMOND), CommonTagPrefixes.GEM)
    }

    @JvmField
    val EMERALD: HTDeferredMaterialContents = REGISTER.registerContents("emerald", CommonPartKeys.GEM) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.EMERALD_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.GEM, HTMaterialItemEntry.item(Items.EMERALD), CommonTagPrefixes.GEM)
    }

    @JvmField
    val ECHO: HTDeferredMaterialContents = REGISTER.registerContents("echo", CommonPartKeys.GEM) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(HCBlocks.ECHO_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.GEM, HTMaterialItemEntry.item(Items.ECHO_SHARD), CommonTagPrefixes.GEM)
    }

    // Common

    // Modded

    //    Metal    //

    // Vanilla
    @JvmField
    val COPPER: HTDeferredMaterialContents = REGISTER.registerContents("copper", CommonPartKeys.INGOT) {
        add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.block(Blocks.RAW_COPPER_BLOCK), CommonTagPrefixes.RAW_STORAGE_BLOCK)
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.COPPER_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.DUST, CommonTagPrefixes.DUST)
        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(Items.COPPER_INGOT), CommonTagPrefixes.INGOT)
        add(CommonPartKeys.NUGGET, HTMaterialItemEntry.item(Items.COPPER_NUGGET), CommonTagPrefixes.NUGGET)
        add(CommonPartKeys.RAW, HTMaterialItemEntry.item(Items.RAW_COPPER), CommonTagPrefixes.RAW_MATERIALS)
    }

    @JvmField
    val IRON: HTDeferredMaterialContents = REGISTER.registerContents("iron", CommonPartKeys.INGOT) {
        add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.block(Blocks.RAW_IRON_BLOCK), CommonTagPrefixes.RAW_STORAGE_BLOCK)
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.IRON_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.DUST, CommonTagPrefixes.DUST)
        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(Items.IRON_INGOT), CommonTagPrefixes.INGOT)
        add(CommonPartKeys.NUGGET, HTMaterialItemEntry.item(Items.IRON_NUGGET), CommonTagPrefixes.NUGGET)
        add(CommonPartKeys.RAW, HTMaterialItemEntry.item(Items.RAW_IRON), CommonTagPrefixes.RAW_MATERIALS)
    }

    @JvmField
    val GOLD: HTDeferredMaterialContents = REGISTER.registerContents("gold", CommonPartKeys.INGOT) {
        add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.block(Blocks.RAW_GOLD_BLOCK), CommonTagPrefixes.RAW_STORAGE_BLOCK)
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.GOLD_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.DUST, CommonTagPrefixes.DUST)
        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(Items.GOLD_INGOT), CommonTagPrefixes.INGOT)
        add(CommonPartKeys.NUGGET, HTMaterialItemEntry.item(Items.GOLD_NUGGET), CommonTagPrefixes.NUGGET)
        add(CommonPartKeys.RAW, HTMaterialItemEntry.item(Items.RAW_GOLD), CommonTagPrefixes.RAW_MATERIALS)
    }

    // Common
    @JvmField
    val IRIDIUM: HTDeferredMaterialContents = REGISTER.registerContents("iridium", CommonPartKeys.INGOT) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(HCBlocks.IRIDIUM_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)
        add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.block(HCBlocks.RAW_IRIDIUM_BLOCK), CommonTagPrefixes.RAW_STORAGE_BLOCK)

        add(CommonPartKeys.DUST, HTMaterialItemEntry.item(HCItems.IRIDIUM_DUST), CommonTagPrefixes.DUST)
        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(HCItems.IRIDIUM_INGOT), CommonTagPrefixes.INGOT)
        add(CommonPartKeys.NUGGET, HTMaterialItemEntry.item(HCItems.IRIDIUM_NUGGET), CommonTagPrefixes.NUGGET)
        add(CommonPartKeys.RAW, HTMaterialItemEntry.item(HCItems.RAW_IRIDIUM), CommonTagPrefixes.RAW_MATERIALS)
    }

    // Modded

    //    Alloy    //

    // Vanilla
    @JvmField
    val NETHERITE: HTDeferredMaterialContents = REGISTER.registerContents("netherite", CommonPartKeys.INGOT) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.NETHERITE_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.DUST, CommonTagPrefixes.DUST)
        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(Items.NETHERITE_INGOT), CommonTagPrefixes.INGOT)
        add(CommonPartKeys.NUGGET, HTMaterialItemEntry.item(HCItems.NETHERITE_NUGGET), CommonTagPrefixes.NUGGET)
    }

    // Common

    // Modded

    //    Other    //

    // Vanilla
    @JvmField
    val BONE_MEAL: HTDeferredMaterialContents = REGISTER.registerContents("bone_meal", CommonPartKeys.MISC) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.BONE_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.MISC, HTMaterialItemEntry.item(Items.BONE_MEAL))
    }

    @JvmField
    val ENDER_PEARL: HTDeferredMaterialContents = REGISTER.registerContents("ender_pearl", CommonPartKeys.MISC) {
        add(CommonPartKeys.DUST, HTMaterialItemEntry.item(HCItems.ENDER_PEARL_DUST), CommonTagPrefixes.DUST)
        add(CommonPartKeys.MISC, HTMaterialItemEntry.item(Items.ENDER_PEARL), Tags.Items.ENDER_PEARLS)
    }

    @JvmField
    val OBSIDIAN: HTDeferredMaterialContents = REGISTER.registerContents("obsidian", CommonPartKeys.STORAGE_BLOCK) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.OBSIDIAN), Tags.Items.OBSIDIANS_NORMAL)

        add(CommonPartKeys.DUST, HTMaterialItemEntry.item(HCItems.OBSIDIAN_DUST), CommonTagPrefixes.DUST)
    }

    @JvmField
    val SLIME: HTDeferredMaterialContents = REGISTER.registerContents("slime", CommonPartKeys.MISC) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.SLIME_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.MISC, HTMaterialItemEntry.item(Items.SLIME_BALL), Tags.Items.SLIME_BALLS)
    }

    @JvmField
    val RESIN: HTDeferredMaterialContents = REGISTER.registerContents("resin", CommonPartKeys.MISC) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.RESIN_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

        add(CommonPartKeys.MISC, HTMaterialItemEntry.item(Items.RESIN_CLUMP))
    }

    @JvmField
    val WOOD: HTDeferredMaterialContents = REGISTER.registerContents("wood", CommonPartKeys.MISC) {
        add(CommonPartKeys.DUST, HTMaterialItemEntry.item(HCItems.WOOD_DUST), CommonTagPrefixes.DUST)
        add(CommonPartKeys.MISC, ItemTags.PLANKS)
    }

    // Common

    // Modded
}

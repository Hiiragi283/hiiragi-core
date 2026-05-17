package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.registry.HTDeferredMaterialContentsRegister
import hiiragi283.lib.tag.HTCommonTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

data object HCMaterialContents {
    @JvmField
    val REGISTER = HTDeferredMaterialContentsRegister(HiiragiCoreAPI.MOD_ID)

    //    Fuel    //

    // Vanilla
    @JvmField
    val COAL: HTMaterialContents = REGISTER.registerContents("coal", CommonPartKeys.FUEL) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.COAL_BLOCK), Tags.Items.STORAGE_BLOCKS_COAL)

        add(CommonPartKeys.FUEL, HTMaterialItemEntry.item(Items.COAL))
    }

    @JvmField
    val CHARCOAL: HTMaterialContents = REGISTER.registerContents("charcoal", CommonPartKeys.FUEL) {
        add(CommonPartKeys.STORAGE_BLOCK, HTCommonTags.Items.STORAGE_BLOCKS_CHARCOAL) // TODO

        add(CommonPartKeys.FUEL, HTMaterialItemEntry.item(Items.CHARCOAL))
    }

    // Common

    // Modded

    // Vanilla

    //    Minerals    //

    // Vanilla
    @JvmField
    val REDSTONE: HTMaterialContents = REGISTER.registerContents("redstone", CommonPartKeys.DUST) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.REDSTONE_BLOCK), Tags.Items.STORAGE_BLOCKS_REDSTONE)

        add(CommonPartKeys.DUST, HTMaterialItemEntry.item(Items.REDSTONE), Tags.Items.DUSTS_REDSTONE)
    }

    @JvmField
    val GLOWSTONE: HTMaterialContents = REGISTER.registerContents("glowstone", CommonPartKeys.DUST) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.GLOWSTONE), HTCommonTags.Items.STORAGE_BLOCKS_GLOWSTONE)

        add(CommonPartKeys.DUST, HTMaterialItemEntry.item(Items.GLOWSTONE_DUST), Tags.Items.DUSTS_GLOWSTONE)
    }

    // Common

    // Modded

    //    Gem    //

    // Vanilla
    @JvmField
    val LAPIS: HTMaterialContents = REGISTER.registerContents("lapis", CommonPartKeys.GEM) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.LAPIS_BLOCK), Tags.Items.STORAGE_BLOCKS_LAPIS)

        add(CommonPartKeys.GEM, HTMaterialItemEntry.item(Items.LAPIS_LAZULI), Tags.Items.GEMS_LAPIS)
    }

    @JvmField
    val QUARTZ: HTMaterialContents = REGISTER.registerContents("quartz", CommonPartKeys.GEM) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.QUARTZ_BLOCK), HTCommonTags.Items.STORAGE_BLOCKS_QUARTZ)

        add(CommonPartKeys.GEM, HTMaterialItemEntry.item(Items.QUARTZ), Tags.Items.GEMS_QUARTZ)
    }

    // Common

    // Modded

    //    Metal    //

    // Vanilla
    @JvmField
    val COPPER: HTMaterialContents = REGISTER.registerContents("copper", CommonPartKeys.INGOT) {
        add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.block(Blocks.RAW_COPPER_BLOCK), Tags.Items.STORAGE_BLOCKS_RAW_COPPER)
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.COPPER_BLOCK), Tags.Items.STORAGE_BLOCKS_COPPER)

        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(Items.COPPER_INGOT), Tags.Items.INGOTS_COPPER)
        add(CommonPartKeys.NUGGET, HTMaterialItemEntry.item(Items.COPPER_NUGGET), Tags.Items.NUGGETS_COPPER)
        add(CommonPartKeys.RAW, HTMaterialItemEntry.item(Items.RAW_COPPER), Tags.Items.RAW_MATERIALS_COPPER)
    }

    @JvmField
    val IRON: HTMaterialContents = REGISTER.registerContents("iron", CommonPartKeys.INGOT) {
        add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.block(Blocks.RAW_IRON_BLOCK), Tags.Items.STORAGE_BLOCKS_RAW_IRON)
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.IRON_BLOCK), Tags.Items.STORAGE_BLOCKS_IRON)

        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(Items.IRON_INGOT), Tags.Items.INGOTS_IRON)
        add(CommonPartKeys.NUGGET, HTMaterialItemEntry.item(Items.IRON_NUGGET), Tags.Items.NUGGETS_IRON)
        add(CommonPartKeys.RAW, HTMaterialItemEntry.item(Items.RAW_IRON), Tags.Items.RAW_MATERIALS_IRON)
    }

    @JvmField
    val GOLD: HTMaterialContents = REGISTER.registerContents("gold", CommonPartKeys.INGOT) {
        add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.block(Blocks.RAW_GOLD_BLOCK), Tags.Items.STORAGE_BLOCKS_RAW_GOLD)
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.GOLD_BLOCK), Tags.Items.STORAGE_BLOCKS_GOLD)

        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(Items.GOLD_INGOT), Tags.Items.INGOTS_GOLD)
        add(CommonPartKeys.NUGGET, HTMaterialItemEntry.item(Items.GOLD_NUGGET), Tags.Items.NUGGETS_GOLD)
        add(CommonPartKeys.RAW, HTMaterialItemEntry.item(Items.RAW_GOLD), Tags.Items.RAW_MATERIALS_GOLD)
    }

    // Common

    // Modded

    //    Alloy    //

    // Vanilla
    @JvmField
    val NETHERITE: HTMaterialContents = REGISTER.registerContents("netherite", CommonPartKeys.INGOT) {
        add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.block(Blocks.NETHERITE_BLOCK), Tags.Items.STORAGE_BLOCKS_NETHERITE)

        add(CommonPartKeys.INGOT, HTMaterialItemEntry.item(Items.NETHERITE_INGOT), Tags.Items.INGOTS_NETHERITE)
    }

    // Common

    // Modded
}

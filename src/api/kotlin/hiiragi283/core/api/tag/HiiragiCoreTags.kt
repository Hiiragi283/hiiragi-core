package hiiragi283.core.api.tag

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

/**
 * Hiiragi Coreで使用されるタグをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
object HiiragiCoreTags {
    object Blocks {
        @JvmField
        val ORES_DEEP_STEEL_SCRAP: TagKey<Block> = common("ores", "deep_steel_scrap")

        // Mod
        @JvmField
        val INCORRECT_FOR_ALMIGHTY_PICKAXE: TagKey<Block> = mod("incorrect_for_almighty_pickaxe")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = Registries.BLOCK.createCommonTag(*path)

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(HiiragiCoreAPI.id(*path))
    }

    object Items {
        @JvmField
        val COAL_COKE: TagKey<Item> = common("coal_coke")

        @JvmField
        val HAMMERS: TagKey<Item> = common("hammers")

        @JvmField
        val ORES_DEEP_STEEL_SCRAP: TagKey<Item> = common("ores", "deep_steel_scrap")

        @JvmField
        val TOOLS_HAMMER: TagKey<Item> = common("tools", "hammer")

        // Mod
        @JvmField
        val BYPASS_MENU_VALIDATION: TagKey<Item> = mod("bypass_menu_validation")

        @JvmField
        val ELDRITCH_PEARL_BINDER: TagKey<Item> = mod("eldritch_pearl_binder")

        @JvmField
        val IGNORED_IN_RECIPE_INPUTS: TagKey<Item> = mod("ignored_in_recipe_inputs")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = Registries.ITEM.createCommonTag(*path)

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HiiragiCoreAPI.id(*path))
    }
}

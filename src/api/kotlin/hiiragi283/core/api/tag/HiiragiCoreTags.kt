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
        // Mod
        @JvmField
        val INCORRECT_FOR_ALMIGHTY_PICKAXE: TagKey<Block> = mod("incorrect_for_almighty_pickaxe")

        @JvmField
        val LATEX_DRIPPING_LOGS: TagKey<Block> = mod("latex_dripping_logs")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Block> = Registries.BLOCK.createCommonTag(*path)

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(HiiragiCoreAPI.id(*path))
    }

    object Items {
        /**
         * @since 0.10.0
         */
        @JvmField
        val CROPS_WARPED_WART: TagKey<Item> = common("crops", "warped_wart")

        /**
         * @since 0.9.0
         */
        @JvmField
        val HAMMERS: TagKey<Item> = common("hammers")

        @JvmField
        val PLASTICS: TagKey<Item> = common("plastics")

        @JvmField
        val SILICON: TagKey<Item> = common("silicon")

        @JvmField
        val TOOLS_HAMMER: TagKey<Item> = common("tools", "hammer")

        // Mod
        @JvmField
        val BYPASS_MENU_VALIDATION: TagKey<Item> = mod("bypass_menu_validation")

        @JvmField
        val ELDRITCH_PEARL_BINDER: TagKey<Item> = mod("eldritch_pearl_binder")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = Registries.ITEM.createCommonTag(*path)

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HiiragiCoreAPI.id(*path))
    }
}

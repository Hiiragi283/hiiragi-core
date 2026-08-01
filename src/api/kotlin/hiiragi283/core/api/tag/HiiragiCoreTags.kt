package hiiragi283.core.api.tag

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid

/**
 * Hiiragi Coreで使用されるタグをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
data object HiiragiCoreTags {
    data object Blocks {
        // Mod
        @JvmField
        val INCORRECT_FOR_ALMIGHTY_PICKAXE: TagKey<Block> = mod("incorrect_for_almighty_pickaxe")

        /**
         * @since 0.11.0
         */
        @JvmField
        val LATEX_DRIPPING_LOGS: TagKey<Block> = mod("latex_dripping_logs")

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Block> = Registries.BLOCK.createTagKey(HiiragiCoreAPI.id(*path))
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.15.2
     */
    data object DamageTypes {
        @JvmField
        val IS_SONIC: TagKey<DamageType> = mod("is_sonic")

        @JvmStatic
        private fun mod(path: String): TagKey<DamageType> = Registries.DAMAGE_TYPE.createTagKey(HiiragiCoreAPI.id(path))
    }

    data object EntityTypes {
        /**
         * @since 0.14.0
         */
        @JvmField
        val CAPTURE_BLACKLIST: TagKey<EntityType<*>> = mod("capture_blacklist")

        /**
         * @since 0.15.2
         */
        @JvmField
        val SENSITIVE_TO_HAMMER_OF_JUSTICE: TagKey<EntityType<*>> = mod("sensitive_to_hammer_of_justice")

        /**
         * @since 0.15.2
         */
        @JvmField
        val SENSITIVE_TO_NOISE_CANCELLING: TagKey<EntityType<*>> = mod("sensitive_to_noise_cancelling")

        /**
         * @since 0.15.2
         */
        @JvmField
        val SENSITIVE_TO_PURIFICATION: TagKey<EntityType<*>> = mod("sensitive_to_purification")

        @JvmStatic
        private fun mod(vararg path: String): TagKey<EntityType<*>> = Registries.ENTITY_TYPE.createTagKey(HiiragiCoreAPI.id(*path))
    }

    data object Fluids {
        /**
         * @since 0.13.0
         */
        @JvmField
        val ELDRITCH: TagKey<Fluid> = mod("eldritch")

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Fluid> = Registries.FLUID.createTagKey(HiiragiCoreAPI.id(*path))
    }

    data object Items {
        /**
         * @since 0.10.0
         */
        @JvmField
        val CROPS_WARPED_WART: TagKey<Item> = common("crops", "warped_wart")

        /**
         * @since 0.15.2
         */
        @JvmField
        val FOODS_DOUGH_WHEAT: TagKey<Item> = common("foods", "dough", "wheat")

        /**
         * @since 0.12.0
         */
        @JvmField
        val FLOURS: TagKey<Item> = common("flours")

        /**
         * @since 0.12.0
         */
        @JvmField
        val FLOURS_WHEAT: TagKey<Item> = common("flours", "wheat")

        /**
         * @since 0.9.0
         */
        @JvmField
        val HAMMERS: TagKey<Item> = common("hammers")

        /**
         * @since 0.11.0
         */
        @JvmField
        val PLASTICS: TagKey<Item> = common("plastics")

        /**
         * @since 0.15.2
         */
        @JvmField
        val RUBBERS: TagKey<Item> = common("rubbers")

        /**
         * @since 0.11.0
         */
        @JvmField
        val SILICON: TagKey<Item> = common("silicon")

        /**
         * @since 0.15.2
         */
        @JvmField
        val STICKY_BALLS: TagKey<Item> = common("sticky_balls")

        @JvmField
        val TOOLS_HAMMER: TagKey<Item> = common("tools", "hammer")

        // Mod
        @JvmField
        val BYPASS_MENU_VALIDATION: TagKey<Item> = mod("bypass_menu_validation")

        @JvmField
        val ELDRITCH_PEARL_BINDER: TagKey<Item> = mod("eldritch_pearl_binder")

        /**
         * @since 0.15.0
         */
        @JvmField
        val FORGING_HAMMERS: TagKey<Item> = mod("forging_hammers")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = RawTagKey.common(*path).create(Registries.ITEM)

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HiiragiCoreAPI.id(*path))
    }
}

package hiiragi283.core.api

import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

data object HiiragiCoreTags {
    data object Items {
        @JvmField
        val CROPS_WARPED_WART: TagKey<Item> = common("crops", "warped_wart")

        @JvmField
        val FOODS_DOUGH_WHEAT: TagKey<Item> = common("foods", "dough", "wheat")

        @JvmField
        val FLOURS: TagKey<Item> = common("flours")

        @JvmField
        val FLOURS_WHEAT: TagKey<Item> = common("flours", "wheat")

        @JvmField
        val STICKY_BALLS: TagKey<Item> = common("sticky_balls")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HTConstants.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HiiragiCoreAPI.id(*path))
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.15.2
     */
    object DamageTypes {
        @JvmField
        val IS_SONIC: TagKey<DamageType> = mod("is_sonic")

        @JvmStatic
        private fun mod(path: String): TagKey<DamageType> = Registries.DAMAGE_TYPE.createTagKey(HiiragiCoreAPI.id(path))
    }

    object EntityTypes {
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

    data object MaterialContents {
        @JvmField
        val COALS: TagKey<HTMaterialContents> = common("coals")

        @JvmStatic
        private fun common(vararg path: String): TagKey<HTMaterialContents> = HTRegistries.Keys.MATERIAL_CONTENTS.createTagKey(HTConstants.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<HTMaterialContents> = HTRegistries.Keys.MATERIAL_CONTENTS.createTagKey(HiiragiCoreAPI.id(*path))
    }
}

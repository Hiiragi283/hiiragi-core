package hiiragi283.core.api.tag

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

/**
 * Hiiragi Coreで使用されるタグをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
object HiiragiCoreTags {
    object Items {
        @JvmField
        val DOUGHS: TagKey<Item> = common("doughs")

        @JvmField
        val DOUGHS_WHEAT: TagKey<Item> = common("doughs", "wheat")

        @JvmField
        val FLOURS: TagKey<Item> = common("flours")

        @JvmField
        val FLOURS_WHEAT: TagKey<Item> = common("flours", "wheat")

        // Mod
        @JvmField
        val BYPASS_MENU_VALIDATION: TagKey<Item> = mod("bypass_menu_validation")

        @JvmField
        val ELDRITCH_PEARL_BINDER: TagKey<Item> = mod("eldritch_pearl_binder")

        @JvmField
        val IGNORED_IN_RECIPE_INPUT: TagKey<Item> = mod("ignored_in_recipe_inputs")

        @JvmStatic
        private fun common(vararg path: String): TagKey<Item> = Registries.ITEM.createCommonTag(*path)

        @JvmStatic
        private fun mod(vararg path: String): TagKey<Item> = Registries.ITEM.createTagKey(HiiragiCoreAPI.id(*path))
    }
}

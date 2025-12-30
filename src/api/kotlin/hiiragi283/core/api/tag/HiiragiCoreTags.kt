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
    // Item
    @JvmField
    val BYPASS_MENU_VALIDATION: TagKey<Item> = Registries.ITEM.createTagKey(HiiragiCoreAPI.id("bypass_menu_validation"))

    @JvmField
    val IGNORED_IN_RECIPE_INPUT: TagKey<Item> = Registries.ITEM.createTagKey(HiiragiCoreAPI.id("ignored_in_recipe_inputs"))
}

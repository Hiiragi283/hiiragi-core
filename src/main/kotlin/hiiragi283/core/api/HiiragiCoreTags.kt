package hiiragi283.core.api

import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.resource.toId
import hiiragi283.lib.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

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

    data object MaterialContents {
        @JvmField
        val COALS: TagKey<HTMaterialContents> = common("coals")

        @JvmStatic
        private fun common(vararg path: String): TagKey<HTMaterialContents> = HTRegistries.Keys.MATERIAL_CONTENTS.createTagKey(HTConstants.COMMON.toId(*path))

        @JvmStatic
        private fun mod(vararg path: String): TagKey<HTMaterialContents> = HTRegistries.Keys.MATERIAL_CONTENTS.createTagKey(HiiragiCoreAPI.id(*path))
    }
}

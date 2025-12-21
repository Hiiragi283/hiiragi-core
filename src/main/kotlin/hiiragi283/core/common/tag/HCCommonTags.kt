package hiiragi283.core.common.tag

import hiiragi283.core.api.tag.createCommonTag
import hiiragi283.core.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object HCCommonTags {
    //    Blocks    //

    object Blocks {
        @JvmStatic
        private fun create(path: String): TagKey<Block> = Registries.BLOCK.createCommonTag(path)

        @JvmStatic
        private fun create(vararg path: String): TagKey<Block> = Registries.BLOCK.createCommonTag(*path)

        @JvmStatic
        private fun create(id: ResourceLocation): TagKey<Block> = Registries.BLOCK.createTagKey(id)
    }

    //    Items    //

    object Items {
        @JvmField
        val TOOLS_HAMMER: TagKey<Item> = create("tools", "hammer")

        @JvmStatic
        private fun create(path: String): TagKey<Item> = Registries.ITEM.createCommonTag(path)

        @JvmStatic
        private fun create(vararg path: String): TagKey<Item> = Registries.ITEM.createCommonTag(*path)

        @JvmStatic
        private fun create(id: ResourceLocation): TagKey<Item> = Registries.ITEM.createTagKey(id)
    }
}

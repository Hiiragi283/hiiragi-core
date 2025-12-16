package hiiragi283.core.common.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object HCModTags {
    //    Blocks    //

    object Blocks {
        @JvmField
        val MINEABLE_WITH_HAMMER: TagKey<Block> = create("mineable", "hammer")

        @JvmStatic
        private fun create(path: String): TagKey<Block> = create(HiiragiCoreAPI.id(path))

        @JvmStatic
        private fun create(vararg path: String): TagKey<Block> = create(HiiragiCoreAPI.id(*path))

        @JvmStatic
        private fun create(id: ResourceLocation): TagKey<Block> = Registries.BLOCK.createTagKey(id)
    }

    //    Items    //

    object Items {
        @JvmField
        val ELDRITCH_PEARL_BINDER: TagKey<Item> = create("eldritch_pearl_binder")

        @JvmStatic
        private fun create(path: String): TagKey<Item> = create(HiiragiCoreAPI.id(path))

        @JvmStatic
        private fun create(vararg path: String): TagKey<Item> = create(HiiragiCoreAPI.id(*path))

        @JvmStatic
        private fun create(id: ResourceLocation): TagKey<Item> = Registries.ITEM.createTagKey(id)
    }
}

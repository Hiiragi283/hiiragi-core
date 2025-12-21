package hiiragi283.core.api.data.model

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.vanillaId
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import org.slf4j.Logger

abstract class HTItemModelProvider(context: HTDataGenContext) :
    ItemModelProvider(context.output, HiiragiCoreAPI.MOD_ID, context.fileHelper) {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Extensions    //

    protected inline fun existTexture(item: HTIdLike, action: (ResourceLocation) -> Unit) {
        existTexture(item, item.itemId) { itemIn: HTIdLike, _: ResourceLocation -> action(itemIn.getId()) }
    }

    protected inline fun existTexture(item: HTIdLike, id: ResourceLocation, action: (HTIdLike, ResourceLocation) -> Unit) {
        if (existingFileHelper.exists(id, TEXTURE)) {
            action(item, id)
        } else {
            LOGGER.debug("Missing texture {} for {}", id, item.getId())
        }
    }

    protected fun layeredItem(item: HTIdLike, vararg layers: ResourceLocation): ItemModelBuilder {
        val builder: ItemModelBuilder = withExistingParent(item.getPath(), vanillaId(HTConst.ITEM, "generated"))
        layers.forEachIndexed { index: Int, layer: ResourceLocation ->
            builder.texture("layer$index", layer)
        }
        return builder
    }
}

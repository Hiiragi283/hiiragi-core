package hiiragi283.core.api.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.vanillaId
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider

abstract class HTItemModelProvider(context: HTDataGenContext) :
    ItemModelProvider(context.output, HiiragiCoreAPI.MOD_ID, context.fileHelper) {
    protected fun existTexture(id: ResourceLocation): Boolean = existingFileHelper.exists(id, TEXTURE)

    protected fun basicItemAlt(item: HTIdLike, layer0: ResourceLocation): ItemModelBuilder =
        withExistingParent(item.getPath(), vanillaId(HTConst.ITEM, "generated"))
            .texture("layer0", layer0)
}

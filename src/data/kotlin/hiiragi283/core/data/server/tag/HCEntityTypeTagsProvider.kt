package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.Tags

class HCEntityTypeTagsProvider(context: HTDataGenContext) :
    HTTagsProvider.DataGen<EntityType<*>>(HiiragiCoreAPI.MOD_ID, Registries.ENTITY_TYPE, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<EntityType<*>>) {
        factory.apply(HiiragiCoreTags.EntityTypes.CAPTURE_BLACKLIST)
            .add(HTConst.MINECRAFT.toId("warden"))
            .addTag(Tags.EntityTypes.BOSSES)
            .addTag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)
    }
}

package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.HiiragiCoreTags
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class HCEntityTypeTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
) : HTTagsProvider.DataGen<EntityType<*>>(fileHelper, output, Registries.ENTITY_TYPE, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        builder(HiiragiCoreTags.EntityTypes.CAPTURE_BLACKLIST)
            // .add(HTConst.MINECRAFT.toId("warden"))
            .addTag(Tags.EntityTypes.BOSSES)
            .addTag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)

        builder(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_HAMMER_OF_JUSTICE)
            .addTag(EntityTypeTags.RAIDERS)

        builder(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING)
            .add(EntityType.WARDEN.toLike())

        builder(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_PURIFICATION)
            .add(EntityType.WITHER.toLike())
            .add(EntityType.WITHER_SKELETON.toLike())
    }
}

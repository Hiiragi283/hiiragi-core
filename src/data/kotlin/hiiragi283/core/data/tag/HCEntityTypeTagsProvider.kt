package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.lib.data.tag.HTTagsProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EntityTypeIds
import net.neoforged.neoforge.common.Tags

class HCEntityTypeTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTTagsProvider<EntityType<*>>(output, Registries.ENTITY_TYPE, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        builder(HiiragiCoreTags.EntityTypes.CAPTURE_BLACKLIST)
            .addTag(Tags.EntityTypes.BOSSES)
            .addTag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)

        builder(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_HAMMER_OF_JUSTICE)
            .addTag(EntityTypeTags.RAIDERS)

        builder(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING)
            .add(EntityTypeIds.WARDEN)

        builder(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_PURIFICATION)
            .add(EntityTypeIds.WITHER)
            .add(EntityTypeIds.WITHER_SKELETON)
    }
}

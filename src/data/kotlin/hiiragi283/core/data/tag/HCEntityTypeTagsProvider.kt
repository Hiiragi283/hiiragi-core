package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.lib.data.tag.HTIdLikeTagsProvider
import hiiragi283.lib.registry.toLike
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.Tags

class HCEntityTypeTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTIdLikeTagsProvider<EntityType<*>>(output, Registries.ENTITY_TYPE, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        tag(HiiragiCoreTags.EntityTypes.CAPTURE_BLACKLIST)
            .addTag(Tags.EntityTypes.BOSSES)
            .addTag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)

        tag(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_HAMMER_OF_JUSTICE)
            .addTag(EntityTypeTags.RAIDERS)

        tag(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_NOISE_CANCELLING)
            .addEntity(EntityType.WARDEN)

        tag(HiiragiCoreTags.EntityTypes.SENSITIVE_TO_PURIFICATION)
            .addEntity(EntityType.WITHER)
            .addEntity(EntityType.WITHER_SKELETON)
    }

    private fun IdAppender.addEntity(entityType: EntityType<*>): IdAppender = this.add(entityType.toLike())
}

package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTFluidTagsProvider
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.level.material.Fluid

class HCFluidTagsProvider(context: HTDataGenContext) : HTFluidTagsProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Fluid>) {
        addContents(factory, HCFluids.REGISTER.asSequence())

        factory
            .apply(HiiragiCoreTags.Fluids.ELDRITCH)
            .add("oritech".toId("still_strange_matter"), HTTagDependType.OPTIONAL)
            .addContentTag(HCFluids.OMINOUS_FLUX)
            .addTag(HTFluidPart.MOLTEN.createTagKey(HCMaterialKeys.ELDRITCH), HTTagDependType.OPTIONAL)
    }
}

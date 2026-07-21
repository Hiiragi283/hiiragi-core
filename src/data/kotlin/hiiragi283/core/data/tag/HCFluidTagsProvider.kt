package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.material.part.HTFluidPart
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.support.data.tag.HTFluidTagsProvider
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper

class HCFluidTagsProvider(fileHelper: ExistingFileHelper, output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTFluidTagsProvider(fileHelper, output, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        addContents(HCFluids.REGISTER.asSequence())

        builder(HiiragiCoreTags.Fluids.ELDRITCH)
            .add(createKey("oritech", "still_strange_matter"), HTTagDependType.OPTIONAL)
            .addContentTag(HCFluids.OMINOUS_FLUX)
            .addTag(HTFluidPart.MOLTEN.createTagKey(HCMaterialKeys.ELDRITCH), HTTagDependType.OPTIONAL)
    }
}

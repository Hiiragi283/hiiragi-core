package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.core.setup.HCFluids
import hiiragi283.lib.data.tag.HTFluidTagsProvider
import hiiragi283.lib.data.tag.HTTagDependType
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput

class HCFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTFluidTagsProvider(output, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        addContents(HCFluids.REGISTER.asSequence())

        builder(HiiragiCoreTags.Fluids.ELDRITCH)
            .add(createKey("oritech", "still_strange_matter"), HTTagDependType.OPTIONAL)
            .addContentTag(HCFluids.OMINOUS_FLUX)
    }
}

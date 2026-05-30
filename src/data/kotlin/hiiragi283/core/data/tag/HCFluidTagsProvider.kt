package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreTags
import hiiragi283.core.setup.HCFluids
import hiiragi283.lib.data.tag.HTFluidTagsProvider
import hiiragi283.lib.resource.toId
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput

class HCFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) : HTFluidTagsProvider(output, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        addContents(HCFluids.REGISTER.asSequence())

        tag(HiiragiCoreTags.Fluids.ELDRITCH)
            .addOptional { "oritech".toId("still_strange_matter") }
            .addContentTag(HCFluids.OMINOUS_FLUX)
    }
}

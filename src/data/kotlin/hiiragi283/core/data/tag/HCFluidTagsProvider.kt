package hiiragi283.core.data.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.tag.HTFluidTagsProvider
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.level.material.Fluid
import java.util.concurrent.CompletableFuture

class HCFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTFluidTagsProvider(output, lookupProvider, HiiragiCoreAPI.MOD_ID) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Fluid>) {
        addContents(factory, HCFluids.REGISTER.asSequence())
    }
}

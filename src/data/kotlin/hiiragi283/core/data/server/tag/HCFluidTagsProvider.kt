package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTFluidTagsProvider
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.setup.HCFluids
import net.minecraft.world.level.material.Fluid

class HCFluidTagsProvider(context: HTDataGenContext) : HTFluidTagsProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Fluid>) {
        addContents(factory, HCFluids.REGISTER.asSequence())
    }
}

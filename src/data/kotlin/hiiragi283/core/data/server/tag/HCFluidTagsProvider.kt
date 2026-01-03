package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid

class HCFluidTagsProvider(context: HTDataGenContext) : HTTagsProvider<Fluid>(HiiragiCoreAPI.MOD_ID, Registries.FLUID, context) {
    override fun addTagsInternal(factory: BuilderFactory<Fluid>) {
        for (content: HTFluidContent<*, *, *> in HCFluids.REGISTER.entries) {
            factory
                .apply(content.getFluidTag())
                .addContent(content)
        }
    }

    fun HTTagBuilder<Fluid>.addContent(content: HTFluidContent<*, *, *>): HTTagBuilder<Fluid> =
        this.add(content.stillHolder).add(content.flowingHolder)
}

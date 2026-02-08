package hiiragi283.core.data.server.tag

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid

class HCFluidTagsProvider(context: HTDataGenContext) : HTTagsProvider<Fluid>(HiiragiCoreAPI.MOD_ID, Registries.FLUID, context) {
    override fun addTagsInternal(factory: BuilderFactory<Fluid>) {
        HCFluids.REGISTER.asSequence().forEach(::addContent.partially1(factory))
    }

    fun addContent(factory: BuilderFactory<Fluid>, content: HTFluidContent) {
        val builder: HTTagBuilder<Fluid> = factory.apply(content.fluidTag).add(content)
        content.flowingHolder?.let(builder::add)
    }
}

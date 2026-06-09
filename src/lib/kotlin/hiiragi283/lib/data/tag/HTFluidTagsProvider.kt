package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.HTFluidContent
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

abstract class HTFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, modId: String) : HTTagsProvider<Fluid>(output, Registries.FLUID, lookupProvider, modId) {
    //    Extensions    //

    fun addContents(contents: Sequence<HTFluidContent>) {
        for (content: HTFluidContent in contents) {
            val fluidTag: TagKey<Fluid> = content.fluidTag
            tag(fluidTag).addContent(content)
            if (content.getFluidType().isLighterThanAir) {
                tag(Tags.Fluids.GASEOUS).addTag(fluidTag)
            }
        }
    }

    protected fun HTTagBuilder<Fluid>.addContent(content: HTFluidContent): HTTagBuilder<Fluid> {
        this.add(content)
        if (content is HTFluidContent.Flowing) {
            this.add(content.flowingHolder)
        }
        return this
    }

    protected fun HTTagBuilder<Fluid>.addContentTag(content: HTFluidContent): HTTagBuilder<Fluid> = this.addTag(content.fluidTag)
}

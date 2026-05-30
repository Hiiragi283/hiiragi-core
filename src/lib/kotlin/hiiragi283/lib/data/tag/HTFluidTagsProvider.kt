package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.HTFluidContent
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

abstract class HTFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, modId: String) : HTIdLikeTagsProvider<Fluid>(output, Registries.FLUID, lookupProvider, modId) {
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

    protected fun IdAppender.addContent(content: HTFluidContent): IdAppender {
        this.add(content)
        if (content is HTFluidContent.Flowing) {
            this.add(content.flowingHolder)
        }
        return this
    }

    protected fun IdAppender.addContentTag(content: HTFluidContent): IdAppender = this.addTag(content.fluidTag)
}

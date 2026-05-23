package hiiragi283.core.api.data.tag

import hiiragi283.core.api.registry.HTFluidContent
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

/**
 * [液体][Fluid]向けの[HTTagsProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
abstract class HTFluidTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    modId: String,
) : HTTagsProvider.DataGen<Fluid>(fileHelper, output, Registries.FLUID, lookupProvider, modId) {
    //    Extensions    //

    fun addContents(factory: HTTagsProvider.BuilderFactory<Fluid>, contents: Sequence<HTFluidContent>) {
        for (content: HTFluidContent in contents) {
            factory.apply(content.fluidTag).addContent(content)
            if (content.getFluidType().isLighterThanAir) {
                factory.apply(Tags.Fluids.GASEOUS).addTag(content.fluidTag)
            }
        }
    }

    fun HTTagBuilder<Fluid>.addContent(content: HTFluidContent): HTTagBuilder<Fluid> {
        this.add(content)
        if (content is HTFluidContent.Flowing) {
            this.add(content.flowingHolder)
        }
        return this
    }

    /**
     * [HTFluidContent.fluidTag]に基づいてタグの値を追加します。
     * @since 0.12.0
     */
    fun HTTagBuilder<Fluid>.addContentTag(content: HTFluidContent): HTTagBuilder<Fluid> = this.addTag(content.fluidTag)
}

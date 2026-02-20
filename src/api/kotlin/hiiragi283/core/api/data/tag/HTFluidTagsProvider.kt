package hiiragi283.core.api.data.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.getFluidType
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

/**
 * [液体][Fluid]向けの[HTTagsProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
abstract class HTFluidTagsProvider(modId: String, context: HTDataGenContext) :
    HTTagsProvider.DataGen<Fluid>(modId, Registries.FLUID, context) {
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
        content.flowingHolder?.let(this::add)
        return this
    }
}

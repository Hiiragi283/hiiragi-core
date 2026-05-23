package hiiragi283.lib.fluid

import hiiragi283.lib.registry.HTFluidContent
import net.minecraft.client.renderer.block.FluidModel
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent

class HTFluidModelRegister(private val event: RegisterFluidModelsEvent) {
    inline fun register(content: HTFluidContent, builderAction: FluidModelBuilder.() -> Unit) {
        register(content, FluidModelBuilder().apply(builderAction).build())
    }

    fun register(content: HTFluidContent, model: FluidModel.Unbaked) {
        when (content) {
            is HTFluidContent.Flowing -> event.register(model, content.get(), content.flowingHolder.get())
            is HTFluidContent.Virtual -> event.register(model, content.get())
        }
    }
}

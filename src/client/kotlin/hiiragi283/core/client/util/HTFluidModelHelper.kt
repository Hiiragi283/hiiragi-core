package hiiragi283.core.client.util

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.toId
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent
import net.neoforged.neoforge.client.fluid.FluidTintSource
import java.awt.Color

data object HTFluidModelHelper {
    @JvmStatic
    fun registerClear(event: RegisterFluidModelsEvent, content: HTFluidContent, color: Color) {
        register(
            event,
            HTConst.MINECRAFT.toId(HTConst.BLOCK, "water_still"),
            content,
            SimpleFluidTintSource(color),
            HTConst.MINECRAFT.toId(HTConst.BLOCK, "water_flow"),
            HTConst.MINECRAFT.toId(HTConst.BLOCK, "water_overlay"),
        )
    }

    @JvmStatic
    fun registerDull(event: RegisterFluidModelsEvent, content: HTFluidContent, color: Color) {
        register(
            event,
            HTConst.NEOFORGE.toId(HTConst.BLOCK, "milk_still"),
            content,
            SimpleFluidTintSource(color),
            HTConst.NEOFORGE.toId(HTConst.BLOCK, "milk_flowing"),
        )
    }

    @JvmStatic
    fun registerMolten(event: RegisterFluidModelsEvent, content: HTFluidContent, color: Color) {
        register(
            event,
            HiiragiCoreAPI.id(HTConst.BLOCK, "molten_still"),
            content,
            SimpleFluidTintSource(color),
            HiiragiCoreAPI.id(HTConst.BLOCK, "molten_flow"),
        )
    }

    @JvmStatic
    fun register(
        event: RegisterFluidModelsEvent,
        still: Identifier,
        content: HTFluidContent,
        tintSource: FluidTintSource? = null,
        flowing: Identifier = still,
        overlay: Identifier? = null,
    ) {
        register(
            event,
            Material(still),
            content,
            tintSource,
            Material(flowing),
            overlay?.let(::Material),
        )
    }

    @JvmStatic
    fun register(
        event: RegisterFluidModelsEvent,
        still: Material,
        content: HTFluidContent,
        tintSource: FluidTintSource? = null,
        flowing: Material = still,
        overlay: Material? = null,
    ) {
        register(
            event,
            FluidModel.Unbaked(
                still,
                flowing,
                overlay,
                tintSource,
            ),
            content = content,
        )
    }

    @JvmStatic
    fun register(event: RegisterFluidModelsEvent, model: FluidModel.Unbaked, content: HTFluidContent) {
        val still: Fluid = content.get()
        val flowing: Fluid? = content.flowingHolder?.get()
        if (flowing == null) {
            event.register(model, still)
        } else {
            event.register(model, still, flowing)
        }
    }
}

package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.Text
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTDeferredFluidType<TYPE : FluidType> :
    HTDeferredHolder<FluidType, TYPE>,
    HTIdLike.Translatable {
    constructor(key: ResourceKey<FluidType>) : super(key)

    constructor(id: ResourceLocation) : super(NeoForgeRegistries.Keys.FLUID_TYPES, id)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().description
}

package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.api.registry.IdToFunction
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.common.registry.HTDeferredFluid
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import java.util.function.Supplier

class HTDeferredFluidRegister(namespace: String) : HTDeferredRegister<Fluid>(Registries.FLUID, namespace) {
    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredFluid<*>> = super.asSequence().filterIsInstance<HTDeferredFluid<*>>()

    override fun <I : Fluid> register(name: String, func: IdToFunction<out I>): HTDeferredFluid<I> =
        super.register(name, func) as HTDeferredFluid<I>

    override fun <I : Fluid> register(name: String, sup: Supplier<out I>): HTDeferredFluid<I> =
        super.register(name, sup) as HTDeferredFluid<I>

    override fun <I : Fluid> createHolder(registryKey: RegistryKey<Fluid>, key: ResourceLocation): HTDeferredFluid<I> =
        HTDeferredFluid(registryKey.createKey(key))
}

package hiiragi283.lib.registry

import java.util.function.Function
import java.util.function.Supplier
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries

class HTDeferredFluidTypeRegister(namespace: String) : HTDeferredRegister<FluidType>(NeoForgeRegistries.Keys.FLUID_TYPES, namespace) {
    fun registerSimpleType(name: String, properties: FluidType.Properties): HTDeferredFluidType<FluidType> = registerType(name, properties, ::FluidType)

    fun <TYPE : FluidType> registerType(name: String, properties: FluidType.Properties, factory: (FluidType.Properties) -> TYPE): HTDeferredFluidType<TYPE> = this.register(name) { _ -> properties.let(factory) }

    //    HTDeferredRegister    //

    override fun <I : FluidType> createHolder(registryKey: RegistryKey<FluidType>, key: Identifier): HTDeferredFluidType<I> = HTDeferredFluidType(key)

    override fun <I : FluidType> register(name: String, sup: Supplier<out I>): HTDeferredFluidType<I> = super.register(name, sup) as HTDeferredFluidType<I>

    override fun <I : FluidType> register(name: String, func: Function<Identifier, out I>): HTDeferredFluidType<I> = super.register(name, func) as HTDeferredFluidType<I>

    override fun asSequence(): Sequence<HTDeferredFluidType<*>> = super.asSequence().filterIsInstance<HTDeferredFluidType<*>>()
}

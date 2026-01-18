package hiiragi283.core.api.gui.sync

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.DataBindingBuilder
import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.function.compose
import hiiragi283.core.api.storage.fluid.HTFluidResourceFactory
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.storage.item.HTItemResourceFactory
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.core.api.storage.item.toResource
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

/**
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object HTDataBindingBuilder {
    //    Fluid    //

    @JvmStatic
    private fun fluidSupplier(getter: () -> HTFluidResourceType?): Supplier<FluidStack> = Supplier(
        getter.andThen(HTFluidResourceFactory::createStack),
    )

    @JvmStatic
    private fun fluidConsumer(setter: (HTFluidResourceType?) -> Unit): Consumer<FluidStack> =
        Consumer(setter.compose(FluidStack::toResource))

    @JvmStatic
    fun fluid(getter: () -> HTFluidResourceType?, setter: (HTFluidResourceType?) -> Unit): DataBindingBuilder<FluidStack> =
        DataBindingBuilder.fluidStack(fluidSupplier(getter), fluidConsumer(setter))

    @JvmStatic
    fun fluid(property: KMutableProperty0<HTFluidResourceType?>): DataBindingBuilder<FluidStack> = fluid(property::get, property::set)

    @JvmStatic
    fun fluidS2C(getter: () -> HTFluidResourceType?): DataBindingBuilder<FluidStack> =
        DataBindingBuilder.fluidStackS2C(fluidSupplier(getter))

    @JvmStatic
    fun fluidS2C(property: KProperty0<HTFluidResourceType?>): DataBindingBuilder<FluidStack> = fluidS2C(property::get)

    @JvmStatic
    fun fluidC2S(setter: (HTFluidResourceType?) -> Unit): DataBindingBuilder<FluidStack> =
        DataBindingBuilder.fluidStackC2S(fluidConsumer(setter))

    @JvmStatic
    fun fluidC2S(property: KMutableProperty0<HTFluidResourceType?>): DataBindingBuilder<FluidStack> = fluidC2S(property::set)

    //    Item    //

    @JvmStatic
    private fun itemSupplier(getter: () -> HTItemResourceType?): Supplier<ItemStack> =
        Supplier(getter.andThen(HTItemResourceFactory::createStack))

    @JvmStatic
    private fun itemConsumer(setter: (HTItemResourceType?) -> Unit): Consumer<ItemStack> = Consumer(setter.compose(ItemStack::toResource))

    @JvmStatic
    fun item(getter: () -> HTItemResourceType?, setter: (HTItemResourceType?) -> Unit): DataBindingBuilder<ItemStack> =
        DataBindingBuilder.itemStack(itemSupplier(getter), itemConsumer(setter))

    @JvmStatic
    fun item(property: KMutableProperty0<HTItemResourceType?>): DataBindingBuilder<ItemStack> = item(property::get, property::set)

    @JvmStatic
    fun itemS2C(getter: () -> HTItemResourceType?): DataBindingBuilder<ItemStack> = DataBindingBuilder.itemStackS2C(itemSupplier(getter))

    @JvmStatic
    fun itemS2C(property: KProperty0<HTItemResourceType?>): DataBindingBuilder<ItemStack> = itemS2C(property::get)

    @JvmStatic
    fun itemC2S(setter: (HTItemResourceType?) -> Unit): DataBindingBuilder<ItemStack> =
        DataBindingBuilder.itemStackC2S(itemConsumer(setter))

    @JvmStatic
    fun itemC2S(property: KMutableProperty0<HTItemResourceType?>): DataBindingBuilder<ItemStack> = itemC2S(property::set)
}

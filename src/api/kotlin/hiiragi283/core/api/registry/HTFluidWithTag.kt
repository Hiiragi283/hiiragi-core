package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import net.minecraft.core.Holder
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType

/**
 * [液体][Fluid]のインスタンスと[TagKey]を保持する[HTHolderLike]の拡張インターフェースです。
 * @param FLUID 液体のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTFluidContent
 */
interface HTFluidWithTag<FLUID : Fluid> :
    HTHolderLike<Fluid, FLUID>,
    HTKeyLike.HolderDelegate<Fluid> {
    companion object {
        /**
         * 「水」の[HTFluidWithTag]のインスタンス
         */
        @Suppress("DEPRECATION")
        @JvmField
        val WATER: HTFluidWithTag<FlowingFluid> = object : HTFluidWithTag<FlowingFluid> {
            override fun getFluidTag(): TagKey<Fluid> = Tags.Fluids.WATER

            override fun getFluidType(): FluidType = NeoForgeMod.WATER_TYPE.value()

            override fun get(): FlowingFluid = Fluids.WATER

            override fun getHolder(): Holder<Fluid> = get().builtInRegistryHolder()
        }

        /**
         * 「溶岩」の[HTFluidWithTag]のインスタンス
         */
        @Suppress("DEPRECATION")
        @JvmField
        val LAVA: HTFluidWithTag<FlowingFluid> = object : HTFluidWithTag<FlowingFluid> {
            override fun getFluidTag(): TagKey<Fluid> = Tags.Fluids.LAVA

            override fun getFluidType(): FluidType = NeoForgeMod.LAVA_TYPE.value()

            override fun get(): FlowingFluid = Fluids.LAVA

            override fun getHolder(): Holder<Fluid> = get().builtInRegistryHolder()
        }

        /**
         * 「牛乳」の[HTFluidWithTag]のインスタンス
         */
        @JvmField
        val MILK: HTFluidWithTag<Fluid> = object : HTFluidWithTag<Fluid> {
            override fun getFluidTag(): TagKey<Fluid> = Tags.Fluids.MILK

            override fun getFluidType(): FluidType = NeoForgeMod.MILK_TYPE.get()

            override fun get(): Fluid = getHolder().value()

            override fun getHolder(): Holder<Fluid> = NeoForgeMod.MILK.delegate
        }
    }

    /**
     * 保持している液体の[TagKey]を返します。
     */
    fun getFluidTag(): TagKey<Fluid>

    /**
     * 保持している液体の[FluidType]を返します。
     */
    fun getFluidType(): FluidType

    fun isOf(fluid: Fluid): Boolean = get() == fluid

    fun isOf(tagKey: TagKey<Fluid>): Boolean = getFluidTag() == tagKey && getHolder().`is`(tagKey)

    fun isOf(resource: HTFluidResourceType): Boolean = resource.isOf(get())
}

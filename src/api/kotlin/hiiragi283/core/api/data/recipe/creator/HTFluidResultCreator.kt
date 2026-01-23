package hiiragi283.core.api.data.recipe.creator

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.property.HTFluidMaterialProperty
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.getDefaultFluidAmount
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.property.HTPropertyKey
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrThrow
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import hiiragi283.core.api.storage.fluid.HTFluidResourceFactory
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.IntUnaryOperator

/**
 * [HTFluidResult]向けの[HTResultCreator]の実装クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
data object HTFluidResultCreator : HTResultCreator<Fluid, HTFluidResourceType, FluidStack, HTFluidResult>() {
    fun create(content: HTFluidContent<*, *, *>, amount: Int = defaultAmount()): HTFluidResult =
        create(content.get(), content.fluidTag, amount)

    /**
     * @since 0.6.0
     */
    fun water(amount: Int = defaultAmount()): HTFluidResult = create(VanillaFluidContents.WATER, amount)

    /**
     * @since 0.6.0
     */
    fun lava(amount: Int = defaultAmount()): HTFluidResult = create(VanillaFluidContents.LAVA, amount)

    /**
     * @since 0.6.0
     */
    fun milk(amount: Int = defaultAmount()): HTFluidResult = create(VanillaFluidContents.MILK, amount)

    /**
     * @since 0.8.0
     */
    fun molten(material: HTMaterialLike, operator: IntUnaryOperator = IntUnaryOperator.identity()): HTFluidResult =
        create(material, HTMaterialPropertyKeys.MOLTEN_FLUID, operator)

    /**
     * @since 0.8.0
     */
    fun create(
        material: HTMaterialLike,
        propertyKey: HTPropertyKey<HTFluidMaterialProperty?>,
        operator: IntUnaryOperator = IntUnaryOperator.identity(),
    ): HTFluidResult {
        val propertyMap: HTPropertyMap = HiiragiCoreAccess.INSTANCE.materialManager.getOrEmpty(material)
        val fluid: HTFluidContent<*, *, *> = propertyMap.getOrThrow(propertyKey).fluid
        return create(fluid, operator.applyAsInt(propertyMap.getDefaultFluidAmount()))
    }

    //    HTResultCreator    //

    override fun resourceFactory(): HTFluidResourceFactory = HTFluidResourceFactory

    override fun create(contents: Ior<HTFluidResourceType, TagKey<Fluid>>, amount: Int): HTFluidResult = HTFluidResult(contents, amount)
}

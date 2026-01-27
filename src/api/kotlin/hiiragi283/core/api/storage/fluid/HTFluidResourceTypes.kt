package hiiragi283.core.api.storage.fluid

import hiiragi283.core.api.fluid.createFluidStack
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

/**
 * この[Fluid][this]を[HTFluidResourceType]に変換します。
 * @param patch コンポーネントの差分
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun Fluid?.toResource(patch: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResourceType? =
    createFluidStack(this, patch = patch).toResource()

/**
 * この[FluidStack][this]を[HTFluidResourceType]に変換します。
 * @return [FluidStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun FluidStack.toResource(): HTFluidResourceType? = HTFluidResourceType.of(this)

/**
 * この[FluidStack][this]を[HTFluidResourceType]と数量に展開します。
 * @return [FluidStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
fun FluidStack.toResourcePair(): Pair<HTFluidResourceType, Int>? {
    val resource: HTFluidResourceType = this.toResource() ?: return null
    return resource to this.amount
}

/**
 * この[HTFluidResourceType][this]を[FluidStack]に変換します
 * @return この[HTFluidResourceType]が`null`の場合は[FluidStack.EMPTY]
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
fun HTFluidResourceType?.toStackOrEmpty(amount: Int): FluidStack = this?.toStack(amount) ?: FluidStack.EMPTY

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
@OnlyIn(Dist.CLIENT)
fun HTFluidResourceType.getClientExtensions(): IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this.fluidType())

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
@OnlyIn(Dist.CLIENT)
fun HTFluidResourceType.getStillTexture(): ResourceLocation? = this.getClientExtensions().getStillTexture(this.toStack(1))

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
@OnlyIn(Dist.CLIENT)
fun HTFluidResourceType.getTintColor(): Int = this.getClientExtensions().getTintColor(this.toStack(1))

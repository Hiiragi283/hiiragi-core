package hiiragi283.core.api.storage.fluid

import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

/**
 * この[FluidStack][this]を[HTFluidResourceType]に変換します。
 * @return [FluidStack.isEmpty]の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
fun FluidStack.toResource(): HTFluidResourceType? = HTFluidResourceType.of(this)

@OnlyIn(Dist.CLIENT)
fun HTFluidResourceType.getClientExtensions(): IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this.fluidType())

@OnlyIn(Dist.CLIENT)
fun HTFluidResourceType.getStillTexture(): ResourceLocation? = this.getClientExtensions().getStillTexture(this.toStack(1))

@OnlyIn(Dist.CLIENT)
fun HTFluidResourceType.getTintColor(): Int = this.getClientExtensions().getTintColor(this.toStack(1))

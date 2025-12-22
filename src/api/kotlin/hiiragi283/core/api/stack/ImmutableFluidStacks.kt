package hiiragi283.core.api.stack

import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

/**
 * この[スタック][this]を[ImmutableFluidStack]に変換します。
 * @return [FluidStack.isEmpty]が`true`の場合は`null`
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun FluidStack.toImmutable(): ImmutableFluidStack? = ImmutableFluidStack.of(this)

/**
 * この[スタック][this]を[ImmutableFluidStack]に変換します。
 * @throws IllegalStateException [FluidStack.isEmpty]が`true`の場合
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun FluidStack.toImmutableOrThrow(): ImmutableFluidStack = this.toImmutable() ?: error("FluidStack must not be empty")

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getClientExtensions(): IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this.fluidType())

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getStillTexture(): ResourceLocation? = this.getClientExtensions().getStillTexture(this.unwrap())

@OnlyIn(Dist.CLIENT)
fun ImmutableFluidStack.getTintColor(): Int = this.getClientExtensions().getTintColor(this.unwrap())

@file:OnlyIn(Dist.CLIENT)

package hiiragi283.core.api.storage.fluid

import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
fun HTFluidResourceType.getClientExtensions(): IClientFluidTypeExtensions = IClientFluidTypeExtensions.of(this.fluidType())

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
fun HTFluidResourceType.getStillTexture(): ResourceLocation? = this.getClientExtensions().getStillTexture(this.toStack(1))

/**
 * @author Hiiragi Tsubasa
 * @since 0.5.0
 */
fun HTFluidResourceType.getTintColor(): Int = this.getClientExtensions().getTintColor(this.toStack(1))

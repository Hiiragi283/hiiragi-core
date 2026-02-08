package hiiragi283.core.api.material.property

import hiiragi283.core.api.registry.HTFluidContent

/**
 * [液体][HTFluidContent]を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.6.0
 */
@JvmRecord
data class HTFluidMaterialProperty(val fluid: HTFluidContent)

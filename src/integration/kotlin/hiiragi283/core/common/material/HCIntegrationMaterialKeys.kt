package hiiragi283.core.common.material

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.integration.HCIConstants

data object HCIntegrationMaterialKeys {
    //    AE2    //

    // Gem
    @JvmStatic
    val CERTUS_QUARTZ: HTMaterialKey = create(HCIConstants.AE2, "certus_quartz")

    @JvmStatic
    val FLUIX: HTMaterialKey = create(HCIConstants.AE2, "fluix")

    // Other
    @JvmStatic
    val SKY_STONE: HTMaterialKey = create(HCIConstants.AE2, "sky_stone")

    //    Create    //

    //    Ender IO    //

    //    Immersive Engineering    //

    //    Just Dire Things    //

    //    Mekanism    //

    //    Oritech    //

    //    Replication    //

    @JvmStatic
    private fun create(namespace: String, path: String): HTMaterialKey = HTMaterialKey.of(namespace.toId(path))
}

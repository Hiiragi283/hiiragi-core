package hiiragi283.core.common.material

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.material.HTMaterial
import hiiragi283.core.impl.registry.HTDeferredMaterial
import hiiragi283.core.impl.registry.HTDeferredMaterialRegister

data object VanillaMaterials {
    @JvmField
    val REGISTER = HTDeferredMaterialRegister(HTConst.MINECRAFT)

    //    Fuel    //

    @JvmField
    val COAL: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("coal")

    //    Mineral    //

    @JvmField
    val REDSTONE: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("redstone")

    //    Gem    //

    @JvmField
    val LAPIS: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("lapis")

    @JvmField
    val QUARTZ: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("quartz")

    @JvmField
    val AMETHYST: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("amethyst")

    @JvmField
    val DIAMOND: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("diamond")

    @JvmField
    val EMERALD: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("emerald")

    @JvmField
    val ECHO: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("echo")

    @JvmField
    val PRISMARINE: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("prismarine")

    //    Metal    //

    @JvmField
    val COPPER: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("copper")

    @JvmField
    val IRON: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("iron")

    @JvmField
    val GOLD: HTDeferredMaterial<HTMaterial> = REGISTER.registerSimpleMaterial("gold")
}

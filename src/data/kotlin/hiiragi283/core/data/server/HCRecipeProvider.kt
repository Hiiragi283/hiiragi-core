package hiiragi283.core.data.server

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.get
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.common.data.recipe.HTMaterialRecipeProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.VanillaMaterialItems
import hiiragi283.core.data.server.recipe.HCChargingRecipeProvider
import hiiragi283.core.data.server.recipe.HCCrushingRecipeProvider
import hiiragi283.core.data.server.recipe.HCDryingRecipeProvider
import hiiragi283.core.data.server.recipe.HCExplodingRecipeProvider
import hiiragi283.core.data.server.recipe.HCMaterialRecipeProvider
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import java.util.function.Consumer

class HCRecipeProvider(context: HTDataGenContext) : HTRecipeProvider(context) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(
            HTMaterialRecipeProvider(
                HiiragiCoreAPI.MOD_ID,
                HCMaterial.entries,
                HCBlocks.MATERIALS,
                HCItems.MATERIALS,
            ) { prefix: HTPrefixLike, material: HTMaterialLike ->
                HCItems.MATERIALS[prefix, material] ?: VanillaMaterialItems.MATERIALS[prefix, material]
            },
        )
        consumer.accept(HCMaterialRecipeProvider)

        consumer.accept(HCChargingRecipeProvider)
        consumer.accept(HCCrushingRecipeProvider)
        consumer.accept(HCDryingRecipeProvider)
        consumer.accept(HCExplodingRecipeProvider)
    }
}

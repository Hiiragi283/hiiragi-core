package hiiragi283.core.data.server

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.data.recipe.HTMaterialRecipeProvider
import hiiragi283.core.data.server.recipe.HCChargingRecipeProvider
import hiiragi283.core.data.server.recipe.HCCommonRecipeProvider
import hiiragi283.core.data.server.recipe.HCCrushingRecipeProvider
import hiiragi283.core.data.server.recipe.HCExplodingRecipeProvider
import java.util.function.Consumer

class HCRecipeProvider(context: HTDataGenContext) : HTRecipeProvider(context) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(HTMaterialRecipeProvider(HiiragiCoreAPI.MOD_ID))
        consumer.accept(HCCommonRecipeProvider)

        consumer.accept(HCChargingRecipeProvider)
        consumer.accept(HCCrushingRecipeProvider)
        consumer.accept(HCExplodingRecipeProvider)
    }
}

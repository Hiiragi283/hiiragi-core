package hiiragi283.core.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.data.server.recipe.HCChargingRecipeProvider
import hiiragi283.core.data.server.recipe.HCCrushingRecipeProvider
import hiiragi283.core.data.server.recipe.HCDryingRecipeProvider
import hiiragi283.core.data.server.recipe.HCExplodingRecipeProvider
import hiiragi283.core.data.server.recipe.HCMaterialRecipeProvider
import hiiragi283.core.data.server.recipe.HCToolRecipeProvider
import java.util.function.Consumer

class HCRecipeProvider(context: HTDataGenContext) : HTRecipeProvider(context) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(HCMaterialRecipeProvider)
        consumer.accept(HCToolRecipeProvider)

        consumer.accept(HCChargingRecipeProvider)
        consumer.accept(HCCrushingRecipeProvider)
        consumer.accept(HCDryingRecipeProvider)
        consumer.accept(HCExplodingRecipeProvider)
    }
}

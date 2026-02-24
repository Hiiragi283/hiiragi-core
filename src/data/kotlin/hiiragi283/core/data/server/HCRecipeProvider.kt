package hiiragi283.core.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.data.server.recipe.HCBasicRecipeProvider
import hiiragi283.core.data.server.recipe.HCChargingRecipeProvider
import hiiragi283.core.data.server.recipe.HCCommonRecipeProvider
import hiiragi283.core.data.server.recipe.HCExplodingRecipeProvider
import java.util.function.Consumer

class HCRecipeProvider(context: HTDataGenContext) : HTRecipeProvider(context) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(HCCommonRecipeProvider)

        consumer.accept(HCBasicRecipeProvider)
        consumer.accept(HCChargingRecipeProvider)
        consumer.accept(HCExplodingRecipeProvider)
    }
}

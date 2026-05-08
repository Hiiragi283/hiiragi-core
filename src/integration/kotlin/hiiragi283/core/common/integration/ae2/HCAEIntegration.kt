package hiiragi283.core.common.integration.ae2

import appeng.api.AECapabilities
import appeng.recipes.handlers.ChargerRecipe
import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.event.HTRegisterRuntimeRecipeEvent
import hiiragi283.core.common.integration.ae2.storage.HTFluidTankMEStorage
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCRecipeLookups
import hiiragi283.core.setup.HCBlockEntityTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.common.NeoForge

data object HCAEIntegration : HTRecipeProviderContext.Delegated() {
    //    Setup    //

    @JvmStatic
    internal fun init(eventBus: IEventBus) {
        eventBus.addListener(::commonSetup)
        eventBus.addListener(::registerMeStorage)

        NeoForge.EVENT_BUS.addListener(::registerRuntimeRecipe)
    }

    @JvmStatic
    private fun commonSetup(event: FMLCommonSetupEvent) {}

    @JvmStatic
    private fun registerMeStorage(event: RegisterCapabilitiesEvent) {
        event.registerBlockEntity(AECapabilities.ME_STORAGE, HCBlockEntityTypes.COPPER_BASIN.get()) { blockEntity, _ ->
            HTFluidTankMEStorage(blockEntity.tank, blockEntity.name)
        }
    }

    //    Game    //

    override lateinit var delegated: HTRecipeProviderContext

    @JvmStatic
    private fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        this.delegated = event.context
        // Convert HC Charging recipes into AE2 Charger recipes
        for ((id: ResourceLocation, recipe: HCChargingRecipe) in event.getAllRecipes(HCRecipeLookups.CHARGING)) {
            val result: ItemStack = recipe.result.get(true).value() ?: continue
            output.accept(id.withPrefix("charger/"), ChargerRecipe(recipe.ingredient, result), null)
        }
    }
}

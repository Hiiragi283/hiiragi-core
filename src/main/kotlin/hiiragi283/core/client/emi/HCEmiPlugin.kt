package hiiragi283.core.client.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.integration.emi.HTEmiPlugin
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.integration.emi.toItemEmi
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.client.emi.recipe.HTChargingEmiRecipe
import hiiragi283.core.client.emi.recipe.HTSingleItemEmiRecipe
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.Unbreakable

@EmiEntrypoint
class HCEmiPlugin : HTEmiPlugin(HiiragiCoreAPI.MOD_ID) {
    override fun register(registry: EmiRegistry) {
        // Category
        listOf(
            HCEmiRecipeCategories.CHARGING,
            HCEmiRecipeCategories.ANVIL_CRUSHING,
            HCEmiRecipeCategories.EXPLODING,
        ).forEach(::addCategory.partially1(registry))

        // Recipes
        addCustomRecipes(registry)

        addRegistryRecipes(registry, HCRecipeTypes.ANVIL_CRUSHING, HTSingleItemEmiRecipe.Companion::crushing)
        addRegistryRecipes(registry, HCRecipeTypes.CHARGING, ::HTChargingEmiRecipe)
        addRegistryRecipes(registry, HCRecipeTypes.EXPLODING, HTSingleItemEmiRecipe.Companion::exploding)

        // Misc
        registry.addGenericStackProvider(HCEmiStackProvider)
    }

    private fun addCustomRecipes(registry: EmiRegistry) {
        // Eternal Upgrade
        ITEM_LOOKUP
            .filterElements { item: Item -> item.defaultInstance.isDamageableItem }
            .listElements()
            .forEach { holder: Holder<Item> ->
                addRecipeSafe(
                    registry,
                    holder.toLike().getId().withPrefix("/${HTConst.SHAPELESS}/${HiiragiCoreAPI.MOD_ID}/eternal_upgrade/"),
                ) { id: ResourceLocation ->
                    EmiCraftingRecipe(
                        listOf(
                            holder.toItemEmi(),
                            HCItems.ETERNAL_TICKET.toEmi(),
                        ),
                        createItemStack(holder.value(), DataComponents.UNBREAKABLE, Unbreakable(true)).toEmi(),
                        id,
                        true,
                    )
                }
            }
    }
}

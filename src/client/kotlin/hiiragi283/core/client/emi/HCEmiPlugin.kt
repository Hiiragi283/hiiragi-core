package hiiragi283.core.client.emi

import dev.emi.emi.api.EmiDragDropHandler
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.EmiStackProvider
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.stack.EmiStackInteraction
import dev.emi.emi.recipe.EmiSmithingRecipe
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fluid.createFluidStack
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.integration.emi.HTEmiPlugin
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.integration.emi.widget.HTGhostWidget
import hiiragi283.core.api.integration.emi.widget.HTIngredientWidget
import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.asItemSequence
import hiiragi283.core.client.gui.screen.HTWidgetContainerScreen
import hiiragi283.core.common.crafting.HTEternalSmithingRecipe
import hiiragi283.core.setup.HCItems
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

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

        addRegistryRecipes(registry, HCRecipeTypes.ANVIL_CRUSHING, ::HCAnvilCrushingEmiRecipe)
        addRegistryRecipes(registry, HCRecipeTypes.CHARGING, HCSingleItemEmiRecipe.Companion::charging)
        addRegistryRecipes(registry, HCRecipeTypes.EXPLODING, HCSingleItemEmiRecipe.Companion::exploding)

        // Misc
        registry.setDefaultComparison(
            HCItems.ALMIGHTY_PICKAXE.asItem(),
            Comparison.compareData { stack: EmiStack -> stack.get(DataComponents.UNBREAKABLE) },
        )

        registry.addGenericDragDropHandler(DragDropHandler)
        registry.addGenericStackProvider(StackProvider)
    }

    private fun addCustomRecipes(registry: EmiRegistry) {
        // Eternal Upgrade
        for (holder: HTItemHolderLike<*> in ITEM_LOOKUP.asItemSequence()) {
            val item: Item = holder.asItem()
            if (!item.defaultInstance.isDamageableItem) continue
            addRecipeSafe(
                registry,
                holder.getId().withPrefix("/${HTConst.SMITHING}/${HiiragiCoreAPI.MOD_ID}/eternal_upgrade/"),
            ) { id: ResourceLocation ->
                EmiSmithingRecipe(
                    HCItems.ETERNAL_UPGRADE.toEmi(),
                    item.toEmi(),
                    EmiIngredient.of(HTEternalSmithingRecipe.ADDITIONAL_TAG),
                    createItemStack(item, DataComponents.UNBREAKABLE, Unbreakable(true)).toEmi(),
                    id,
                )
            }
        }
    }

    //    Handlers    //

    companion object {
        @JvmStatic
        private fun getWidgets(screen: Screen): List<HTWidgetContainerScreen.WidgetWrapper<*>> = screen
            .children()
            .filterIsInstance<HTWidgetContainerScreen.WidgetWrapper<*>>()
    }

    data object DragDropHandler : EmiDragDropHandler<Screen> {
        override fun dropStack(
            screen: Screen,
            stack: EmiIngredient,
            x: Int,
            y: Int,
        ): Boolean {
            for (wrapper: HTWidgetContainerScreen.WidgetWrapper<*> in getWidgets(screen)) {
                val widget: HTWidget = wrapper.widget
                if (wrapper.bounds.contains(x, y) && widget is HTGhostWidget) {
                    val ghostConsumer: HTGhostWidget.GhostIngredientConsumer = widget.getGhostConsumer() ?: continue
                    val stack: Any = getFirstStack(ghostConsumer, stack) ?: continue
                    ghostConsumer.accept(stack)
                    return true
                }
            }
            return false
        }

        /**
         * @see mekanism.client.recipe_viewer.emi.EmiGhostIngredientHandler.getFirstSupportedStack
         */
        @JvmStatic
        private fun getFirstStack(ghostConsumer: HTGhostWidget.GhostIngredientConsumer, ingredient: EmiIngredient): Any? {
            for (stack: EmiStack in ingredient.emiStacks) {
                val key: Any = stack.key
                val rawStack: Any = if (key is Item) {
                    stack.itemStack
                } else if (key is Fluid) {
                    createFluidStack(key, HTConst.DEFAULT_FLUID_AMOUNT, stack.componentChanges)
                } else {
                    continue
                }
                val stack: Any? = ghostConsumer.supportedTarget(rawStack)
                if (stack != null) return stack
            }
            return null
        }

        override fun render(
            screen: Screen,
            dragged: EmiIngredient,
            draw: GuiGraphics,
            mouseX: Int,
            mouseY: Int,
            delta: Float,
        ) {
            for (wrapper: HTWidgetContainerScreen.WidgetWrapper<*> in getWidgets(screen)) {
                val widget: HTWidget = wrapper.widget
                val bounds: HTBounds = wrapper.bounds
                if (bounds.contains(mouseX, mouseY) && widget is HTGhostWidget) {
                    draw.fill(bounds.left, bounds.top, bounds.right, bounds.bottom, -0x77dd44cd)
                }
            }
        }
    }

    data object StackProvider : EmiStackProvider<Screen> {
        override fun getStackAt(screen: Screen, x: Int, y: Int): EmiStackInteraction {
            for (wrapper: HTWidgetContainerScreen.WidgetWrapper<*> in getWidgets(screen)) {
                if (wrapper.bounds.contains(x, y)) {
                    val widget: HTWidget = wrapper.widget
                    if (widget is HTIngredientWidget) {
                        val ingredient: Any = widget.getIngredient() ?: continue
                        val stack: EmiStack = when (ingredient) {
                            is ItemStack -> ingredient.toEmi()
                            is FluidStack -> ingredient.toEmi()
                            else -> continue
                        }
                        return EmiStackInteraction(stack, null, false)
                    }
                }
            }

            return EmiStackInteraction.EMPTY
        }
    }
}

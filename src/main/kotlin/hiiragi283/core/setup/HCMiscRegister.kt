package hiiragi283.core.setup

import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.collection.asSequence
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.item.HTCreativeModeTabHelper
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTSimpleDeferredItem
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.common.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.core.common.text.HCTranslation
import hiiragi283.core.support.gui.factory.HTBlockWidgetHolderContext
import hiiragi283.core.support.gui.factory.HTItemWidgetHolderContext
import hiiragi283.core.support.gui.sync.HTBoolSyncPayload
import hiiragi283.core.support.gui.sync.HTFluidSyncPayload
import hiiragi283.core.support.gui.sync.HTFractionSyncPayload
import hiiragi283.core.support.gui.sync.HTIntSyncPayload
import hiiragi283.core.support.gui.sync.HTItemSyncPayload
import hiiragi283.core.support.gui.sync.HTLongSyncPayload
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal data object HCMiscRegister {
    @JvmStatic
    private val TRIPLE_COMPARATOR: Comparator<Triple<Comparable<*>, HTMaterialKey, *>> =
        compareBy<Triple<Comparable<*>, HTMaterialKey, *>> { it.first }.thenComparing { it.second }

    @JvmStatic
    fun register(event: RegisterEvent) {
        // Creative Mode Tab
        event.register(Registries.CREATIVE_MODE_TAB) { helper ->
            helper.register(
                HCCreativeTabs.COMMON,
                HTCreativeModeTabHelper.createSimpleTab(HCTranslation.HIIRAGI_CORE, HCItems.IRIDESCENT_POWDER) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    // Items
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = HCItems.REGISTER.asSequence())
                    // Blocks
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = HCBlocks.REGISTER.asItemSequence())
                    // Fluids
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = HCFluids.REGISTER.asItemSequence())
                },
            )

            helper.register(
                HCCreativeTabs.MATERIAL,
                HTCreativeModeTabHelper.createSimpleTab(HCTranslation.CREATIVE_TAB_MATERIAL, Items.IRON_INGOT) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    // Items
                    HTCreativeModeTabHelper.addToDisplay(
                        parameters,
                        output,
                        HiiragiCoreAccess.INSTANCE
                            .registeredContents
                            .items
                            .asSequence()
                            .sortedWith(TRIPLE_COMPARATOR)
                            .map { HTSimpleDeferredItem(it.third.getId()) },
                    )
                    // Blocks
                    HTCreativeModeTabHelper.addToDisplay(
                        parameters,
                        output,
                        HiiragiCoreAccess.INSTANCE
                            .registeredContents
                            .blocks
                            .asSequence()
                            .sortedWith(TRIPLE_COMPARATOR)
                            .map { HTSimpleDeferredItem(it.third.getId()) },
                    )
                    // Fluids
                    HTCreativeModeTabHelper.addToDisplay(
                        parameters,
                        output,
                        HiiragiCoreAccess.INSTANCE
                            .registeredFluids
                            .asSequence()
                            .sortedWith(TRIPLE_COMPARATOR)
                            .map { it.third }
                            .map { HTSimpleDeferredItem(it.getBucketSupplier().getId()) },
                    )
                },
            )

            helper.register(
                HCCreativeTabs.EQUIPMENT,
                HTCreativeModeTabHelper.createSimpleTab(HCTranslation.CREATIVE_TAB_EQUIPMENT, Items.IRON_PICKAXE) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    // Items
                    HTCreativeModeTabHelper.addToDisplay(
                        parameters,
                        output,
                        HiiragiCoreAccess.INSTANCE
                            .registeredContents
                            .tools
                            .asSequence()
                            .sortedWith(TRIPLE_COMPARATOR)
                            .map { HTSimpleDeferredItem(it.third.getId()) },
                    )
                },
            )
        }
        // Data Component Type
        event.register(Registries.DATA_COMPONENT_TYPE) { helper ->
            helper.register(HiiragiCoreAPI.id("blueprint_number"), HCDataComponents.BLUEPRINT_NUMBER)
            helper.register(HiiragiCoreAPI.id("bottle_type"), HCDataComponents.BOTTLE_TYPE)
            helper.register(HiiragiCoreAPI.id("color"), HCDataComponents.COLOR)
            helper.register(HiiragiCoreAPI.id("location"), HCDataComponents.LOCATION)
            helper.register(HiiragiCoreAPI.id("experience"), HCDataComponents.EXPERIENCE)

            helper.register(HiiragiCoreAPI.id(HTConst.ENERGY), HCDataComponents.ENERGY)
            helper.register(HiiragiCoreAPI.id(HTConst.FLUID), HCDataComponents.FLUID)
        }
        // Menu Type
        event.register(Registries.MENU) { helper ->
            helper.register(HTBlockWidgetHolderContext.MENU_TYPE.getId(), IMenuTypeExtension.create(HTBlockWidgetHolderContext::create))
            helper.register(HTItemWidgetHolderContext.MENU_TYPE.getId(), IMenuTypeExtension.create(HTItemWidgetHolderContext::create))
        }
        // Recipe Serializer
        event.register(Registries.RECIPE_SERIALIZER) { helper ->
            helper.register(HiiragiCoreAPI.id("eternal_upgrade"), HCRecipeSerializers.ETERNAL_UPGRADE)
            helper.register(HiiragiCoreAPI.id("blueprint_cloning"), HCRecipeSerializers.BLUEPRINT_CLONING)
            helper.register(HiiragiCoreAPI.id("experience_storing"), HCRecipeSerializers.EXPERIENCE_STORING)

            helper.register(HiiragiCoreAPI.id(HTConst.CHARGING), HCRecipeSerializers.CHARGING)
            helper.register(HiiragiCoreAPI.id(HTConst.CRUSHING), HCRecipeSerializers.CRUSHING)
            helper.register(HiiragiCoreAPI.id(HTConst.EXPLODING), HCRecipeSerializers.EXPLODING)

            helper.register(HiiragiCoreAPI.id(HTConst.EMPTYING), HCRecipeSerializers.EMPTYING)
            helper.register(HiiragiCoreAPI.id(HTConst.FILLING), HCRecipeSerializers.FILLING)
        }
        // Recipe Type
        event.register(Registries.RECIPE_TYPE) { helper ->
            for (recipeType: HTRecipeType<*> in HCRecipeTypes.allTypes) {
                helper.register(recipeType.getId(), recipeType)
            }
        }

        // Attachment Type
        event.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("in_world_recipe_caches"), HCAttachmentTypes.IN_WORLD_RECIPE_CACHES)
        }
        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("blue_print"), HTBluePrintIngredient.TYPE)
        }
        // Fluid Ingredient Type
        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("potion"), HTPotionFluidIngredient.TYPE)
        }

        // Item Result type
        event.register(HCRegistries.Keys.ITEM_RESULT_SERIALIZER) { helper ->
            helper.register(HiiragiCoreAPI.id("simple"), HTItemResult.Simple.SERIALIZER)
            helper.register(HiiragiCoreAPI.id("tag"), HTItemResult.Tagged.SERIALIZER)
            helper.register(HiiragiCoreAPI.id("material_part"), HTItemResult.MaterialPart.SERIALIZER)
        }
        // Slot Sync Type
        event.register(HCRegistries.Keys.SLOT_TYPE) { helper ->
            helper.register(HTConst.COMMON.toId("boolean"), HTBoolSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("fraction"), HTFractionSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("integer"), HTIntSyncPayload.TYPE)
            helper.register(HTConst.COMMON.toId("long"), HTLongSyncPayload.TYPE)

            helper.register(vanillaId(HTConst.FLUID), HTFluidSyncPayload.TYPE)
            helper.register(vanillaId(HTConst.ITEM), HTItemSyncPayload.TYPE)
        }
        // Widget Type
        event.register(HCRegistries.Keys.WIDGET_TYPE) { helper ->
            fun register(type: HTWidgetType.Simple<*>) {
                helper.register(type.id, type)
            }

            register(HCWidgetTypes.FLUID)
            register(HCWidgetTypes.ITEM)
            register(HCWidgetTypes.PROGRESS)
        }
    }
}

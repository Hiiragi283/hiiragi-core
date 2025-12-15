package hiiragi283.core.api

import hiiragi283.core.api.resource.vanillaId
import net.minecraft.client.renderer.LightTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion

data object HTConst {
    //    GUI    //

    @JvmField
    val BLOCK_ATLAS: ResourceLocation = vanillaId("textures/atlas/blocks.png")

    @JvmField
    val GUI_ATLAS: ResourceLocation = vanillaId("textures/atlas/gui.png")

    const val FULL_BRIGHT: Int = LightTexture.FULL_BRIGHT

    //    Item    //

    const val ABSOLUTE_MAX_STACK_SIZE: Int = Item.ABSOLUTE_MAX_STACK_SIZE

    //    Mod ID    //

    const val MINECRAFT = "minecraft"
    const val NEOFORGE: String = NeoForgeVersion.MOD_ID
    const val COMMON = "c"

    @JvmField
    val BUILTIN_IDS: Set<String> = setOf(MINECRAFT, NEOFORGE, COMMON, HiiragiCoreAPI.MOD_ID)

    //    Serialization    //

    const val ACCESS_CONFIG = "access_config"
    const val OWNER = "owner"

    const val ITEM = "item"
    const val SLOT = "slot"
    const val ITEMS = "items"

    const val FLUID = "fluid"
    const val TANK = "tank"
    const val FLUIDS = "fluids"

    const val AMOUNT = "amount"
    const val CAPACITY = "capacity"
    const val BATTERIES = "batteries"

    const val ID = "id"
    const val TAG = "tag"
    const val COUNT = "count"
    const val COMPONENTS = "components"

    const val PREVENT_ITEM_MAGNET = "PreventRemoteMovement"

    //    Recipes    //

    const val INGREDIENT = "ingredient"

    const val CATALYST = "catalyst"

    const val RESULT = "result"
    const val RESULTS = "results"
    const val ITEM_RESULT = "item_result"
    const val FLUID_RESULT = "fluid_result"
}

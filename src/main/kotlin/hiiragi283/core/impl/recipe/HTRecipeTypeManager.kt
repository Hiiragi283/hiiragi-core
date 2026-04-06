package hiiragi283.core.impl.recipe

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeInput
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.TagsUpdatedEvent

@EventBusSubscriber(modid = HiiragiCoreAPI.MOD_ID)
data object HTRecipeTypeManager {
    @JvmStatic
    private val instances: MutableMap<ResourceLocation, HTRecipeTypeImpl<*, *>> = hashMapOf()

    @JvmStatic
    fun <INPUT : RecipeInput, RECIPE : Any> create(id: ResourceLocation): HTRecipeTypeImpl<INPUT, RECIPE> {
        val recipeType = HTRecipeTypeImpl<INPUT, RECIPE>(id)
        check(instances.put(id, recipeType) == null) { "Duplicated recipe type $id" }
        return recipeType
    }

    @SubscribeEvent
    fun clearCache(event: TagsUpdatedEvent) {
        instances.values.forEach(HTRecipeTypeImpl<*, *>::clearCache)
    }
}

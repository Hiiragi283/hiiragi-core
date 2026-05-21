package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.item.consume.HTClearRandomEffectConsumeEffect
import hiiragi283.lib.recipe.ingredient.HTMaterialPartIngredient
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal data object HCMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Consume Effect Type
        event.register(Registries.CONSUME_EFFECT_TYPE) { helper ->
            helper.register(HiiragiCoreAPI.id("clear_random_effect"), HTClearRandomEffectConsumeEffect.TYPE)
        }

        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("material_part"), HTMaterialPartIngredient.TYPE)
        }
    }
}

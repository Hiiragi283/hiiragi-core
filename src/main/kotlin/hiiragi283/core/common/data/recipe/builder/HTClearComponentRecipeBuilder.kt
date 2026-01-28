package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.common.crafting.HTClearComponentRecipe
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.CraftingBookCategory

class HTClearComponentRecipeBuilder : HTRecipeBuilder("${HTConst.SHAPELESS}/clear") {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTClearComponentRecipeBuilder.() -> Unit) {
            HTClearComponentRecipeBuilder().apply(builderAction).save(output)
        }
    }

    var group: String? = null
    var category: CraftingBookCategory = CraftingBookCategory.MISC
    lateinit var item: HTItemHolderLike<*>
    val targets: ComponentTargets = ComponentTargets()

    override fun getPrimalId(): ResourceLocation = item.getId()

    override fun createRecipe(): HTClearComponentRecipe = HTClearComponentRecipe(
        group ?: "",
        category,
        item.getItemHolder(),
        targets.holderSet,
    )

    //    HolderSetHolder    //

    inner class ComponentTargets {
        lateinit var holderSet: HolderSet<DataComponentType<*>>
            private set

        operator fun plusAssign(types: Collection<DataComponentType<*>>) {
            this.plusAssign(HolderSet.direct(BuiltInRegistries.DATA_COMPONENT_TYPE::wrapAsHolder, types))
        }

        operator fun plusAssign(holderSet: HolderSet<DataComponentType<*>>) {
            check(!::holderSet.isInitialized) { "Component types have been already initialized" }
            this.holderSet = holderSet
        }
    }
}

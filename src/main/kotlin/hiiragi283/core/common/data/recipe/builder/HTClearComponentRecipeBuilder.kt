package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toHolderLike
import hiiragi283.core.common.crafting.HTClearComponentRecipe
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.level.ItemLike

class HTClearComponentRecipeBuilder(private val item: HTItemHolderLike<*>) :
    HTRecipeBuilder<HTClearComponentRecipeBuilder>("shapeless/clear") {
    companion object {
        @JvmStatic
        fun create(item: ItemLike): HTClearComponentRecipeBuilder = HTClearComponentRecipeBuilder(item.asItem().toHolderLike())
    }
    
    private lateinit var holderSet: HolderSet<DataComponentType<*>>

    fun setTargets(vararg types: DataComponentType<*>): HTClearComponentRecipeBuilder =
        setTargets(HolderSet.direct(BuiltInRegistries.DATA_COMPONENT_TYPE::wrapAsHolder, *types))

    fun setTargets(holderSet: HolderSet<DataComponentType<*>>): HTClearComponentRecipeBuilder = apply {
        check(!::holderSet.isInitialized) { "Component types have been already initialized" }
        this.holderSet = holderSet
    }

    //    RecipeBuilder    //

    private var group: String? = null
    private var category: CraftingBookCategory = CraftingBookCategory.MISC

    /**
     * レシピのグループを指定します。
     */
    fun setGroup(group: String?): HTClearComponentRecipeBuilder = apply {
        this.group = group
    }

    /**
     * レシピのカテゴリを指定します。
     */
    fun setCategory(category: CraftingBookCategory): HTClearComponentRecipeBuilder = apply {
        this.category = category
    }

    override fun getPrimalId(): ResourceLocation = item.getId()

    override fun createRecipe(): HTClearComponentRecipe = HTClearComponentRecipe(
        group ?: "",
        category,
        item.getHolder(),
        holderSet,
    )
}

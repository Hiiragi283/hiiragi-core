package hiiragi283.core.api.data.recipe.builder

import hiiragi283.core.api.resource.withPrefix
import hiiragi283.core.api.resource.withSuffix
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput

fun RecipeBuilder.savePrefix(output: RecipeOutput, prefix: String) {
    this.save(output, this.defaultId().withPrefix(prefix))
}

fun RecipeBuilder.saveSuffix(output: RecipeOutput, suffix: String) {
    this.save(output, this.defaultId().withSuffix(suffix))
}

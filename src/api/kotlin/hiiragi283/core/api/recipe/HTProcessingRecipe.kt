package hiiragi283.core.api.recipe

import net.minecraft.world.item.crafting.RecipeInput
import org.apache.commons.lang3.math.Fraction

/**
 * 処理時間と獲得経験値を保持する[HTRecipe]の拡張クラスです。
 * @param time レシピの処理時間
 * @param exp レシピの実行時にもらえる経験値量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTProcessingRecipe<INPUT : RecipeInput>(val time: Int, val exp: Fraction) : HTRecipe<INPUT>

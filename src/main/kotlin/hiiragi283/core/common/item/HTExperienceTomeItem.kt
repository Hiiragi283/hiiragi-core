package hiiragi283.core.common.item

import hiiragi283.core.api.color.HTDefaultColor
import hiiragi283.core.util.ExpLevel
import hiiragi283.core.util.ExpValue
import hiiragi283.core.util.HTExperienceHelper
import hiiragi283.core.util.storedExperience
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.roundToInt

class HTExperienceTomeItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack: ItemStack = player.getItemInHand(usedHand)
        if (stack.isEmpty) return InteractionResultHolder.pass(stack)
        // スニーク中 -> 貯めた経験値を放出
        // 通常 -> 経験値を貯める
        // 1レベルずつ処理させる
        val playerLevel: Int = player.experienceLevel
        val currentExp: ExpValue = player.storedExperience
        if (player.isShiftKeyDown) {
            val nextLevel: Int = playerLevel + 1
            val diffExp: ExpValue = HTExperienceHelper.getExpForLevel(nextLevel) - currentExp
            val fixedExp: ExpValue = minOf(HTExperienceHelper.getStoredExp(stack), diffExp)
            if (!level.isClientSide) {
                player.storedExperience += fixedExp
                HTExperienceHelper.updateStoredExp(stack) { it - fixedExp }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
        } else {
            val prevLevel: Int = maxOf(0, playerLevel - 1)
            val diffExp: ExpValue = currentExp - HTExperienceHelper.getExpForLevel(prevLevel)
            val fixedExp: ExpValue = minOf(Int.MAX_VALUE - HTExperienceHelper.getStoredExp(stack), diffExp)
            if (!level.isClientSide) {
                player.storedExperience -= fixedExp
                HTExperienceHelper.updateStoredExp(stack) { it + fixedExp }
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
        }
    }

    override fun isBarVisible(stack: ItemStack): Boolean = stack.count == 1

    override fun getBarWidth(stack: ItemStack): Int {
        val storedExp: ExpValue = HTExperienceHelper.getStoredExp(stack)
        val currentLevel: ExpLevel = HTExperienceHelper.getLevelForExp(storedExp)
        val nextLevel: ExpLevel = currentLevel + 1
        val nextExp: ExpValue = HTExperienceHelper.getExpForLevel(nextLevel)
        val progress: Long = nextExp - storedExp
        return (13f - progress * 13f / nextExp).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int = HTDefaultColor.LIME.color
}

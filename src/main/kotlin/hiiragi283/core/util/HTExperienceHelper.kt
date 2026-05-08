package hiiragi283.core.util

import com.google.common.primitives.Ints
import hiiragi283.core.config.HCConfig
import hiiragi283.core.setup.HCDataComponents
import it.unimi.dsi.fastutil.longs.Long2IntArrayMap
import net.minecraft.core.Holder
import net.minecraft.core.Vec3i
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.util.function.LongUnaryOperator

typealias ExpValue = Long
typealias ExpLevel = Int

/**
 * @see me.desht.pneumaticcraft.common.util.EnchantmentUtils
 */
object HTExperienceHelper {
    @JvmStatic
    fun getExpRatio(): Int = HCConfig.COMMON.expConversionRatio.asInt

    @JvmStatic
    fun fluidAmountFromExp(value: Int): Int = value * getExpRatio()

    @JvmStatic
    fun fluidAmountFromExp(value: ExpValue): Long = value * getExpRatio().toLong()

    @JvmStatic
    fun expAmountFromFluid(amount: Int): ExpValue = amount / getExpRatio().toLong()

    @JvmStatic
    fun popExperienceOrb(level: Level, pos: Vec3i, amount: ExpValue) {
        popExperienceOrb(level, Vec3.atCenterOf(pos), amount)
    }

    @JvmStatic
    fun popExperienceOrb(level: Level, pos: Vec3, amount: ExpValue) {
        if (level is ServerLevel) {
            repeatLongAsInt(amount) { ExperienceOrb.award(level, pos, it) }
        }
    }

    @JvmStatic
    fun repeatLongAsInt(amount: Long, action: (Int) -> Unit) {
        repeat((amount / Int.MAX_VALUE).toInt()) {
            action(Int.MAX_VALUE)
        }
        action((amount % Int.MAX_VALUE).toInt())
    }

    //    Player    //

    @JvmStatic
    fun getPlayerExp(player: Player): ExpValue = getExpForLevel(player.experienceLevel) + (player.experienceProgress * player.xpNeededForNextLevel).toLong()

    @JvmStatic
    fun setPlayerExp(player: Player, amount: Long) {
        val fixedAmount: Long = maxOf(0, amount)
        player.totalExperience = Ints.saturatedCast(fixedAmount)
        player.experienceLevel = getLevelForExp(fixedAmount)
        val expForLevel: ExpValue = getExpForLevel(player.experienceLevel)
        player.experienceProgress = (fixedAmount - expForLevel) / player.xpNeededForNextLevel.toFloat()
    }

    @JvmStatic
    fun getExpForLevel(level: ExpLevel): ExpValue = when {
        level == 0 -> 0L
        level <= 15 -> sum(level, 7, 2)
        level <= 30 -> 315 + sum(level - 15, 37, 5)
        else -> 1395 + sum(level - 30, 112, 9)
    }

    @JvmStatic
    private fun sum(n: Int, a: Int, d: Int): Long = n * (2 * a + (n - 1) * d) / 2L

    @JvmStatic
    fun getExpBarCapacity(level: ExpLevel): ExpValue = when {
        level >= 30 -> 112 + (level - 30L) * 9
        level >= 15 -> 37 + (level - 15L) * 5
        else -> 7 + level * 2L
    }

    @JvmStatic
    private val levelCache: MutableMap<Long, Int> = Long2IntArrayMap()

    @JvmStatic
    fun getLevelForExp(amount: ExpValue): ExpLevel = levelCache.computeIfAbsent(amount, ::findLevelForExp)

    @JvmStatic
    private fun findLevelForExp(amount: ExpValue): ExpLevel {
        var amount1: ExpValue = amount
        var level = 0
        while (true) {
            val nextAmount: ExpValue = getExpBarCapacity(level)
            if (amount1 < nextAmount) return level
            level++
            amount1 -= nextAmount
        }
    }

    //    Player    //

    @JvmStatic
    fun getStoredExp(stack: ItemStack): ExpValue = stack.getOrDefault(HCDataComponents.EXPERIENCE, 0)

    @JvmStatic
    fun updateStoredExp(stack: ItemStack, operator: LongUnaryOperator): ExpValue? = stack.update(HCDataComponents.EXPERIENCE, 0, operator::applyAsLong)

    //    Enchantment    //

    @JvmStatic
    inline fun getTotalCost(enchantments: ItemEnchantments, transform: (Enchantment, Int) -> Int): Int = enchantments.entrySet().sumOf { (holder: Holder<Enchantment>, level: Int) -> transform(holder.value(), level) }

    @JvmStatic
    fun getTotalMinCost(enchantments: ItemEnchantments): Int = getTotalCost(enchantments, Enchantment::getMinCost)

    @JvmStatic
    fun getTotalMaxCost(enchantments: ItemEnchantments): Int = getTotalCost(enchantments, Enchantment::getMaxCost)

    //    Interaction    //

    /*fun moveExperience(
        from: HTExperienceTank?,
        to: HTExperienceTank?,
        amount: Long = from?.getAmount() ?: 0,
        access: HTStorageAccess = HTStorageAccess.INTERNAL,
    ): Long? {
        if (from == null || to == null || amount <= 0) return null
        val simulatedExtracted: Long = from.extract(amount, HTStorageAction.SIMULATE, access)
        val simulatedInserted: Long = to.insert(simulatedExtracted, HTStorageAction.SIMULATE, access)

        val extracted: Long = from.extract(simulatedInserted, HTStorageAction.EXECUTE, access)
        val remainder: Long = to.insert(extracted, HTStorageAction.EXECUTE, access)
        if (remainder > 0) {
            val leftover: Long = from.insert(remainder, HTStorageAction.EXECUTE, access)
            if (leftover > 0) {
                RagiumAPI.LOGGER.error("Experience storage $from did not accept leftover amount from $to! Voiding it.")
            }
        }
        return remainder
    }

    fun moveExperience(from: HTItemSlot, containerSetter: HTStackSetter<ImmutableItemStack>, to: HTExperienceTank): Boolean {
        val stack: ImmutableItemStack = from.getStack() ?: return false
        if (!HTExperienceCapabilities.hasCapability(stack)) return false
        val wrapper: HTExperienceHandlerItemWrapper = HTExperienceHandlerItemWrapper.create(stack.copyWithAmount(1)) ?: return false
        return moveExperience(from, containerSetter, wrapper, to)
    }

    fun moveExperience(
        slot: HTItemSlot,
        containerSetter: HTStackSetter<ImmutableItemStack>,
        from: HTExperienceHandlerItemWrapper,
        to: HTExperienceTank,
    ): Boolean {
        val result: Long? = moveExperience(from, to)
        if (result != null) {
            val container: ImmutableItemStack? = from.container
            if (container != null) {
                if (container.amount() == 1) {
                    containerSetter.setStack(container)
                } else {
                    slot.extract(1, HTStorageAction.EXECUTE, HTStorageAccess.MANUAL)
                }
            }
        }
        return result != null
    }*/
}

//    Extension    //

var Player.storedExperience: Long
    get() = HTExperienceHelper.getPlayerExp(this)
    set(value) = HTExperienceHelper.setPlayerExp(this, value)

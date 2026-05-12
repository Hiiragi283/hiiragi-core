package hiiragi283.core.common.block.dispenser

import hiiragi283.core.setup.HCItems
import hiiragi283.core.util.HTExperienceHelper
import net.minecraft.core.Position
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior
import net.minecraft.core.dispenser.DispenseItemBehavior
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.phys.Vec3

data object HCDispenserBehaviours {
    @JvmField
    val EXPERIENCE_TOME: DispenseItemBehavior = object : DefaultDispenseItemBehavior() {
        override fun execute(blockSource: BlockSource, item: ItemStack): ItemStack {
            val storedExp: Long = HTExperienceHelper.getStoredExp(item)
            if (storedExp > 0) {
                val position: Position = DispenserBlock.getDispensePosition(blockSource)
                HTExperienceHelper.popExperienceOrb(blockSource.level, position as Vec3, storedExp)
                HTExperienceHelper.updateStoredExp(item) { 0 }
            }
            return item
        }
    }

    @JvmStatic
    fun init() {
        DispenserBlock.registerBehavior(HCItems.EXPERIENCE_TOME, EXPERIENCE_TOME)
    }
}

package hiiragi283.core.common.block

import hiiragi283.core.common.block.entity.HTCopperBasinBlockEntity
import hiiragi283.core.setup.HCBlockEntityTypes
import hiiragi283.lib.block.HTBasicEntityBlock
import hiiragi283.lib.transfer.fluid.FluidResourceHandler
import hiiragi283.lib.world.getTypedBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.WeatheringCopper
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.transfer.fluid.FluidUtil

class HTWeatheringCopperBasinBlock(private val weatherState: WeatheringCopper.WeatherState, properties: Properties) :
    HTBasicEntityBlock(HCBlockEntityTypes.COPPER_BASIN, properties),
    WeatheringCopper {
    override fun useItemOn(itemStack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): InteractionResult {
        val result: InteractionResult = super.useItemOn(itemStack, state, level, pos, player, hand, hitResult)
        if (itemStack.isEmpty) return result
        if (!player.isShiftKeyDown) {
            if (!level.isClientSide) {
                val copperBasin: HTCopperBasinBlockEntity = level.getTypedBlockEntity(pos) ?: return InteractionResult.FAIL
                val basinHandler: FluidResourceHandler = copperBasin.getFluidHandler(hitResult.direction)
                val result: InteractionResult.Success = InteractionResult.CONSUME
                when {
                    copperBasin.drainContainer(player, hand) -> return result
                    copperBasin.fillContainer(player, hand) -> return result
                    FluidUtil.interactWithFluidHandler(player, hand, pos, basinHandler) -> return result
                }
            } else {
                return InteractionResult.SUCCESS
            }
        }
        return result
    }

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        changeOverTime(state, level, pos, random)
    }

    override fun isRandomlyTicking(state: BlockState): Boolean = super.isRandomlyTicking(state) && WeatheringCopper.getNext(state.block).isPresent

    //    WeatheringCopper    //

    override fun getAge(): WeatheringCopper.WeatherState = weatherState

    /*override fun changeOverTime(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        val chance = 0.05688889f
        if (random.nextFloat() < chance) {
            getNextState(state, level, pos, random).ifPresent { newState: BlockState ->
                val basinBlockEntity: HTCopperBasinBlockEntity =
                    level.getTypedBlockEntity<HTCopperBasinBlockEntity>(pos) ?: return@ifPresent
                val access: RegistryAccess = level.registryAccess()
                val tag: CompoundTag = basinBlockEntity.saveWithoutMetadata(access)

                level.setBlockAndUpdate(pos, newState)
                level.getBlockEntity(pos)?.loadWithComponents(tag, access)
            }
        }
    }*/
}

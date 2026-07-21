package hiiragi283.core.support.gui.factory

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.registry.HTDeferredHolder
import hiiragi283.core.api.resource.SupplierWithId
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.serialization.network.asOption
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.Option
import hiiragi283.core.api.util.toOption
import hiiragi283.core.support.gui.menu.HTWidgetContainerMenu
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

@JvmRecord
data class HTItemWidgetHolderContext(
    val factory: Factory,
    val player: Player,
    val hand: InteractionHand?,
    val stack: ItemStack,
) : HTWidgetHolderContext,
    MenuProvider {
    companion object {
        @JvmStatic
        private val HAND_CODEC: StreamCodec<ByteBuf, Option<InteractionHand>> = HTStreamCodecs.enum<InteractionHand>().asOption()

        @JvmField
        val MENU_TYPE: SupplierWithId<MenuType<HTWidgetContainerMenu>> = HTDeferredHolder(Registries.MENU, HiiragiCoreAPI.id(HTConst.ITEM))

        @JvmStatic
        fun openMenu(player: ServerPlayer, hand: InteractionHand): Boolean {
            val stack: ItemStack = player.getItemInHand(hand)
            val item: Item = stack.item
            if (item is Factory) {
                val context: HTItemWidgetHolderContext = item.createContext(player, hand, stack)
                return player.openMenu(context).isPresent
            }
            return false
        }

        @JvmStatic
        fun openMenu(player: ServerPlayer, stack: ItemStack): Boolean {
            val item: Item = stack.item
            if (item is Factory) {
                val context: HTItemWidgetHolderContext = item.createContext(player, null, stack)
                return player.openMenu(context).isPresent
            }
            return false
        }

        @JvmStatic
        fun create(containerId: Int, inventory: Inventory, buffer: RegistryFriendlyByteBuf): HTWidgetContainerMenu {
            val player: Player = inventory.player
            val hand: InteractionHand? = HAND_CODEC.decode(buffer).getOrNull()
            val stack: ItemStack = ItemStack.STREAM_CODEC.decode(buffer)
            val item: Item = stack.item
            if (item is Factory) {
                val context: HTItemWidgetHolderContext = item.createContext(player, hand, stack)
                return HTWidgetContainerMenu(MENU_TYPE.get(), containerId, inventory, context)
            } else {
                error("Cannot create menu from $stack in $hand")
            }
        }
    }

    val level: Level get() = player.level()

    override fun setup(player: Player, widgetHolder: HTWidgetHolder) {
        factory.setup(this, widgetHolder)
    }

    override fun stillValid(player: Player): Boolean = factory.stillValid(this)

    override fun getDisplayName(): Text = factory.getDisplayName(this)

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTWidgetContainerMenu = HTWidgetContainerMenu(MENU_TYPE.get(), containerId, playerInventory, this)

    override fun writeClientSideData(menu: AbstractContainerMenu, buffer: RegistryFriendlyByteBuf) {
        HAND_CODEC.encode(buffer, hand.toOption())
        ItemStack.STREAM_CODEC.encode(buffer, stack)
    }

    //    Factory    //

    fun interface Factory {
        fun setup(context: HTItemWidgetHolderContext, widgetHolder: HTWidgetHolder)

        fun createContext(player: Player, hand: InteractionHand?, stack: ItemStack): HTItemWidgetHolderContext = HTItemWidgetHolderContext(this, player, hand, stack)

        fun stillValid(context: HTItemWidgetHolderContext): Boolean {
            val (_, player: Player, hand: InteractionHand?, stack: ItemStack) = context
            return when {
                hand != null -> stack.`is`(player.getItemInHand(hand).item)
                else -> stack.`is`(HiiragiCoreTags.Items.BYPASS_MENU_VALIDATION)
            }
        }

        fun getDisplayName(context: HTItemWidgetHolderContext): Text = context.stack.hoverName
    }
}

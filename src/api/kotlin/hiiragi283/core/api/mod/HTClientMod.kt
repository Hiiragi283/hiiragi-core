package hiiragi283.core.api.mod

import hiiragi283.core.api.event.HTRegisterWidgetRendererEvent
import net.minecraft.client.color.block.BlockColor
import net.minecraft.client.color.item.ItemColor
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.event.AddPackFindersEvent
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

/**
 * Hiiragi Coreとそれを前提とするmodで使用される，クライアント側のmodの抽象クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@OnlyIn(Dist.CLIENT)
abstract class HTClientMod {
    init {
        val eventBus: IEventBus = MOD_BUS
        val container: ModContainer = LOADING_CONTEXT.activeContainer

        eventBus.addListener(::clientSetup)
        eventBus.addListener(::registerWidgetRenderer)
        eventBus.addListener(::registerBlockColors)
        eventBus.addListener(::registerItemColors)
        eventBus.addListener(::registerClientExtensions)
        eventBus.addListener(::registerScreens)
        eventBus.addListener(::registerEntityRenderer)
        eventBus.addListener(::registerPack)

        initialize(eventBus, container)
    }

    /**
     * 初期化を行います。
     */
    protected abstract fun initialize(eventBus: IEventBus, container: ModContainer)

    /**
     * ConfigにGUIを追加します。
     */
    protected fun configScreen(container: ModContainer) {
        container.registerExtensionPoint(IConfigScreenFactory::class.java, IConfigScreenFactory(::ConfigurationScreen))
    }

    /**
     * レジストリへの登録後のセットアップを行います。
     */
    protected open fun clientSetup(event: FMLClientSetupEvent) {}

    /**
     * ウィジェットのレンダラーを登録します。
     */
    protected open fun registerWidgetRenderer(event: HTRegisterWidgetRendererEvent) {}

    /**
     * [BlockColor]を登録します。
     */
    protected open fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {}

    /**
     * [ItemColor]を登録します。
     */
    protected open fun registerItemColors(event: RegisterColorHandlersEvent.Item) {}

    /**
     * 各種クライアント側での拡張を登録します。
     */
    protected open fun registerClientExtensions(event: RegisterClientExtensionsEvent) {}

    /**
     * メニューとスクリーンの紐づけを行います。
     */
    protected open fun registerScreens(event: RegisterMenuScreensEvent) {}

    /**
     * [Entity]や[BlockEntity]のレンダラーを登録します。
     */
    protected open fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {}

    /**
     * 追加のリソースパックを登録します。
     * @since 21.1.0
     */
    protected open fun registerPack(event: AddPackFindersEvent) {}
}

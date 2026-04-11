package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.sync.HTSyncType
import hiiragi283.core.api.gui.widget.HTWidgetHolder
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.integration.jei.widget.HTRecipeAreaWidget
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.common.gui.sync.HTFractionSyncSlot
import hiiragi283.core.impl.gui.widget.HTAbstractWidget
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTProgressWidget :
    HTAbstractWidget,
    HTRecipeAreaWidget<HTProgressWidget> {
    companion object {
        @JvmStatic
        fun createArrow(syncSlot: HTFractionSyncSlot, x: Int, y: Int): HTProgressWidget = HTProgressWidget(
            syncSlot,
            x,
            y,
            24,
            16,
        ).setTexture(HiiragiCoreAPI.id(HTConst.TEXTURES, HTConst.GUI, "arrow"))
    }

    private val fractionSlot: HTFractionSyncSlot

    constructor(syncSlot: HTFractionSyncSlot, bounds: HTBounds) : super(bounds) {
        this.fractionSlot = syncSlot
    }

    constructor(syncSlot: HTFractionSyncSlot, x: Int, y: Int, width: Int, height: Int) : super(x, y, width, height) {
        this.fractionSlot = syncSlot
    }

    var texture: ResourceLocation? = null
    val backgroundTexture: ResourceLocation? get() = texture?.withSuffix("_background")

    fun setTexture(texture: ResourceLocation): HTProgressWidget = apply { this.texture = texture }

    var fillDirection = HTFillDirection.LEFT_TO_RIGHT

    fun setDirection(direction: HTFillDirection): HTProgressWidget = apply { this.fillDirection = direction }

    fun getProgress(): Fraction = fractionSlot.amountAsFraction

    override fun getType(): HTWidgetType<HTProgressWidget> = HCWidgetTypes.PROGRESS.get()

    override fun setupHolder(widgetHolder: HTWidgetHolder) {
        widgetHolder.track(fractionSlot, HTSyncType.S2C)
    }

    //    HTRecipeAreaWidget    //

    private val recipeTypes: MutableSet<HTRecipeViewerType<*>> = hashSetOf()

    override fun getSupportedRecipeTypes(): Iterable<HTRecipeViewerType<*>> = this.recipeTypes

    override fun setSupportedRecipeTypes(recipeTypes: Iterable<HTRecipeViewerType<*>>): HTProgressWidget {
        this.recipeTypes += recipeTypes
        return this
    }
}

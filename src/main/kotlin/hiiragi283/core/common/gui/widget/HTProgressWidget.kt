package hiiragi283.core.common.gui.widget

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTWidgetType
import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import hiiragi283.core.api.recipe.viewer.widget.HTRecipeAreaWidget
import hiiragi283.core.impl.gui.widget.HTAbstractWidget
import hiiragi283.core.setup.HCWidgetTypes
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTProgressWidget :
    HTAbstractWidget,
    HTRecipeAreaWidget<HTProgressWidget> {
    companion object {
        @JvmStatic
        fun createArrow(progressGetter: () -> Fraction, x: Int, y: Int): HTProgressWidget = HTProgressWidget(
            progressGetter,
            x,
            y,
            24,
            16,
        ).setTexture(HiiragiCoreAPI.id(HTConst.TEXTURES, HTConst.GUI, "arrow"))
    }

    private val progressGetter: () -> Fraction

    constructor(progressGetter: () -> Fraction, bounds: HTBounds) : super(bounds) {
        this.progressGetter = progressGetter
    }

    constructor(progressGetter: () -> Fraction, x: Int, y: Int, width: Int, height: Int) : super(x, y, width, height) {
        this.progressGetter = progressGetter
    }

    var texture: ResourceLocation? = null
    val backgroundTexture: ResourceLocation? get() = texture?.withSuffix("_background")

    fun setTexture(texture: ResourceLocation): HTProgressWidget = apply { this.texture = texture }

    var fillDirection = HTFillDirection.LEFT_TO_RIGHT

    fun setDirection(direction: HTFillDirection): HTProgressWidget = apply { this.fillDirection = direction }

    fun getProgress(): Fraction = progressGetter()

    override fun getType(): HTWidgetType<HTProgressWidget> = HCWidgetTypes.PROGRESS.get()

    //    HTRecipeAreaWidget    //

    private val recipeTypes: MutableSet<HTRecipeViewerType<*>> = hashSetOf()

    override fun getSupportedRecipeTypes(): Iterable<HTRecipeViewerType<*>> = this.recipeTypes

    override fun setSupportedRecipeTypes(recipeTypes: Iterable<HTRecipeViewerType<*>>): HTProgressWidget {
        this.recipeTypes += recipeTypes
        return this
    }
}

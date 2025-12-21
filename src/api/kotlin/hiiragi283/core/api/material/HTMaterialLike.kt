package hiiragi283.core.api.material

/**
 * [HTMaterialKey]を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTMaterialLike {
    /**
     * 保持している[素材キー][HTMaterialKey]を返します。
     */
    fun asMaterialKey(): HTMaterialKey

    /**
     * 保持している[素材キー][HTMaterialKey]の名前を返します。
     */
    fun asMaterialName(): String = asMaterialKey().name

    /**
     * 指定した[ほかの素材][other]と素材キーが一致するか判定します。
     */
    fun isOf(other: HTMaterialLike): Boolean = this.asMaterialKey() == other.asMaterialKey()
}

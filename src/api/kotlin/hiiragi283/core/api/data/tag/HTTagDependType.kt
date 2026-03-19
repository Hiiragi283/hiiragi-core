package hiiragi283.core.api.data.tag

/**
 * タグの依存関係を表すクラスです。
 */
enum class HTTagDependType {
    /**
     * 必須
     */
    REQUIRED,

    /**
     * 選択的
     */
    OPTIONAL,
}

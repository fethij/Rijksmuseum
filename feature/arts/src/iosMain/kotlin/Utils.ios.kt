import com.tewelde.rijksmuseum.core.model.Art

actual val Art.artUrl: String
    get() = this.webImage.url

actual val minGridSize: Int
    get() = 175
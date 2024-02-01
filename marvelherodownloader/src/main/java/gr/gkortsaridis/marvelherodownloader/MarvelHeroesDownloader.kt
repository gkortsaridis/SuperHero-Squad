package gr.gkortsaridis.marvelherodownloader

class MarvelHeroesDownloader {
    companion object {
        var privateApiKey: String? = null
        var publicApiKey: String? = null
        var characterLimit: Int = 100
        var heroesOffset = 0

        fun isSetup(): Boolean {
            return privateApiKey != null && publicApiKey != null
        }
        fun setupDownloader(privateApiKey: String, publicApiKey: String, characterLimit: Int = 100) {
            this.privateApiKey = privateApiKey
            this.publicApiKey = publicApiKey
            this.characterLimit = characterLimit
        }
    }

}
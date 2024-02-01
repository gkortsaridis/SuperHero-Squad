package gr.gkortsaridis.superherosquadmaker.ui

import gr.gkortsaridis.marvelherodownloader.model.CharacterDataContainer
import gr.gkortsaridis.marvelherodownloader.model.CharacterDataWrapper
import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.marvelherodownloader.model.Thumbnail
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response

object DataMocks {
    val IRON_MAN_NAME = "Iron Man"
    val CAPTAIN_AMERICA_NAME = "Captain America"
    val HULK_NAME = "Hulk"

    val mockIronMan = Hero(id = 0, name = IRON_MAN_NAME, thumbnail = Thumbnail(path = "", extension = ""), description = "Description")
    val mockCaptainAmerica = Hero(id = 1, name = CAPTAIN_AMERICA_NAME, thumbnail = Thumbnail(path = "", extension = ""), description = "Description")
    val mockHulk = Hero(id = 2, name = HULK_NAME, thumbnail = Thumbnail(path = "", extension = ""), description = "Description")

    val mockCharacterDataContainer = CharacterDataContainer(
        offset = 0,
        limit = 10,
        total = 3,
        count = 3,
        results = listOf(mockIronMan, mockCaptainAmerica, mockHulk)
    )

    val mockDataWrapper = CharacterDataWrapper(
        code = 200,
        status = "Ok",
        data = mockCharacterDataContainer,
    )

    val heroesErrorResponse: Response<CharacterDataWrapper> = Response.error(409, ResponseBody.Companion.create(
        "application/json".toMediaType(),
        ""
    ))

    val heroesSuccessResponse: Response<CharacterDataWrapper> = Response.success(mockDataWrapper)
}
package lama.bibek.hellogames.interfaces

import lama.bibek.hellogames.models.GameObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WSInterface {

    @GET("list")
    fun getGameList() : Call<List<GameObject>>

    @GET("details")
    fun getGameById(@Query("game_id") game_id: Int) : Call<GameObject>
}
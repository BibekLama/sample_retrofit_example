# Sample Retrofit Example
This is simple example of fetching data from API endpoints using retrofit library.
## Screenshots
<img src="https://github.com/BibekLama/sample_retrofit_example/blob/master/app/assets/Screenshot_2020-07-07-17-01-08-90.png" width="40%" title="Splash Screen" alt="Screenshots"></img> 
<img src="https://github.com/BibekLama/sample_retrofit_example/blob/master/app/assets/Screenshot_2020-07-07-17-01-15-36.png" width="40%" title="Splash Screen" alt="Screenshots"></img>

***

### Open build.graddle(app) add the below lines in dependencies
    dependencies {
        ...

        //retrofit
        implementation 'com.squareup.retrofit2:retrofit:2.6.2'
        implementation 'com.squareup.retrofit2:converter-gson:2.6.2'

        // Glide
        implementation 'com.github.bumptech.glide:glide:4.9.0'
        annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'
    }
### Create Model
    data class GameObject(val id: Int,
        val name: String,
        val type: String,
        val players: Int,
        val year: Int,
        val url: String,
        val picture: String,
        val description_fr: String?,
        val description_en: String?)

### Create Interface for API Endpoints

    interface WSInterface {
        @GET("list")
        fun getGameList() : Call<List<GameObject>>

        @GET("details")
        fun getGameById(@Query("game_id") game_id: Int) : Call<GameObject>
    }

### This code in callback function that return response from each endpoints

    val callback : Callback<GameObject> = object : Callback<GameObject> {

        override fun onFailure(call: Call<GameObject>?, t: Throwable?) {
            Log.d("err","Problem in connection with web service")
        }

        override fun onResponse( call: Call<GameObject>, response: Response<GameObject>) {
            if(response.code() == 200) {
                \\ Implement you code here
        }
    }


### Allow pre-existing unmodifiable objects to be converted to and from JSON
[For more info](https://github.com/google/gson)

    val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
    val retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(jsonConverter).build()
    val service: WSInterface = retrofit.create(WSInterface::class.java)
    service.getGameList().enqueue(callback)

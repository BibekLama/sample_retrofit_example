package lama.bibek.hellogames

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import lama.bibek.hellogames.interfaces.WSInterface
import lama.bibek.hellogames.models.GameObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameActivity : AppCompatActivity() {

    val baseURL = "https://androidlessonsapi.herokuapp.com/api/game/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getIntExtra("id", 0)
        getGameData(id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getGameData(id: Int){
        val callback : Callback<GameObject> = object : Callback<GameObject> {
            override fun onFailure(call: Call<GameObject>?, t: Throwable?) {
                Log.d("err","Problem in connection with web service")
            }
            override fun onResponse(
                call: Call<GameObject>,
                response: Response<GameObject>
            ) {
                if(response.code() == 200) {
                    loadScreen(response.body())
                }
            }
        }

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(jsonConverter).build()

        val service: WSInterface = retrofit.create(WSInterface::class.java)

        service.getGameById(id).enqueue(callback)
    }

    private fun loadScreen(data: GameObject?){
        val gameImage = findViewById<ImageView>(R.id.gameImage)
        Glide.with(this@GameActivity).load(data!!.picture).into(gameImage)
        val nameText = findViewById<TextView>(R.id.name)
        nameText.text = data!!.name
        val typeText = findViewById<TextView>(R.id.type)
        typeText.text = data!!.type
        val playersText = findViewById<TextView>(R.id.players)
        playersText.text = data!!.players.toString()
        val yearText = findViewById<TextView>(R.id.year)
        yearText.text = data!!.year.toString()
        val descText = findViewById<TextView>(R.id.desc)
        descText.text = data!!.description_en
        val linkBtn = findViewById<Button>(R.id.linkBtn)
        linkBtn.setOnClickListener(View.OnClickListener {
            val url = data!!.url
            val implicitIntent = Intent(Intent.ACTION_VIEW)
            implicitIntent.data = Uri.parse(url)
            startActivity(implicitIntent)
        })
    }
}

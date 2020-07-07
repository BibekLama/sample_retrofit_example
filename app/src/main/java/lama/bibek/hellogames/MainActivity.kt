package lama.bibek.hellogames

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import lama.bibek.hellogames.interfaces.WSInterface
import lama.bibek.hellogames.models.GameObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    val baseURL = "https://androidlessonsapi.herokuapp.com/api/game/"

    var data: List<GameObject>? = null

    var randomIds: List<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()
        refreshBtn.setOnClickListener(View.OnClickListener {
            loadData()
        })
    }

    private fun loadData(){

        val callback : Callback<List<GameObject>> = object : Callback<List<GameObject>> {
            override fun onFailure(call: Call<List<GameObject>>?, t: Throwable?) {
                Log.d("err","Problem in connection with web service")
            }
            override fun onResponse(
                call: Call<List<GameObject>>,
                response: Response<List<GameObject>>
            ) {
                if(response.code() == 200) {
                    data = response.body()
                    val rand = Random()
                    val newList: MutableList<GameObject> = ArrayList()
                    for (i in 0 until 4) {
                        val randomIndex: Int = rand.nextInt(data!!.size)
                        newList.add(data!!.get(randomIndex));
                    }

                    val d1 = newList.get(0)
                    Glide.with(this@MainActivity).load(d1.picture).into(img1)
                    card1.setOnClickListener(View.OnClickListener { clickListener(d1)})
                    val d2 = newList.get(1)
                    Glide.with(this@MainActivity).load(d2.picture).into(img2)
                    card2.setOnClickListener(View.OnClickListener { clickListener(d2)})
                    val d3 = newList.get(2)
                    Glide.with(this@MainActivity).load(d3.picture).into(img3)
                    card3.setOnClickListener(View.OnClickListener { clickListener(d3)})
                    val d4 = newList.get(3)
                    Glide.with(this@MainActivity).load(d4.picture).into(img4)
                    card4.setOnClickListener(View.OnClickListener { clickListener(d4)})
                }
            }
        }

        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        val retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(jsonConverter).build()

        val service: WSInterface = retrofit.create(WSInterface::class.java)

        service.getGameList().enqueue(callback)
    }

    private fun clickListener(data: GameObject) {
        val intent = Intent(this@MainActivity, GameActivity::class.java)
        intent.putExtra("id",data.id)
        startActivity(intent)
    }
}

package com.example.flixster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONObject

private const val API_KEY = "34e61e3ddd6944220f1e09bcfb1b726d"

class CinemaMoviesFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cinema_movies_list, container, false)
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        updateAdapter(recyclerView)
        return view
    }

    private fun updateAdapter(recyclerView: RecyclerView) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api-key"] = API_KEY

        client[
                "https://api.themoviedb.org/3/movie/now_playing?api_key=34e61e3ddd6944220f1e09bcfb1b726d",
                params,
                object : JsonHttpResponseHandler()
                { //connect these callbacks to your API call
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Headers,
                        json: JsonHttpResponseHandler.JSON
                    ) {
                        // The wait for a response is over
                        //Parse JSON into Models

                        Log.d("Json",json.toString())
                        val resultsJson = json.jsonObject.get("results").toString()

                        Log.d("JsonPage",resultsJson)
                        val gson = Gson()
                        val arrayMovieType = object : TypeToken<List<CinemaMovie>>() {}.type
                        val models : List<CinemaMovie> = gson.fromJson(resultsJson, arrayMovieType)
                        recyclerView.adapter = CinemaMoviesRecyclerViewAdapter(models)

                        // Look for this in Logcat:
                        Log.d("CinemaMovieFragment", "response successful")
                    }
                    /*
                     * The onFailure function gets called when
                     * HTTP response status is "4XX" (eg. 401, 403, 404)
                     */
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        errorResponse: String,
                        t: Throwable?
                    ) {

                        // If the error is not null, log it!
                        t?.message?.let {
                            Log.e("CinemaMovieFragment", errorResponse)
                        }
                    }
                }]

    }

    override fun onItemClick(item: CinemaMovie){
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }
}
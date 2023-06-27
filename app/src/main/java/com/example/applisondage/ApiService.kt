import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.applisondage.MainActivity
import org.json.JSONArray

class ApiService(val context : MainActivity, private val url: String) {

    fun fetchData(onSuccess: (JSONArray) -> Unit, onError: (String) -> Unit) {
        val request = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                onSuccess(response)
            },
            Response.ErrorListener { error ->
                onError(error.message ?: "An error occurred")
            }
        )

        VolleySingleton.getInstance(context).addToRequestQueue(request)
    }
}
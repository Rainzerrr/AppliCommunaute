import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkTask : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String): String {
        val urlString = params[0]
        var result = ""

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                reader.close()
                result = stringBuilder.toString()
            }
            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    override fun onPostExecute(result: String) {}
}
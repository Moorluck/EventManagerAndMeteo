package be.bxl.eventsmanager.api

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class HttpRequest {

    companion object {
        fun getJsonFromRequest(url : String) : String? {

            val urlRequest = URL(url)

            var requestResult : String? = null
            var connection : HttpURLConnection? = null

            try {
                connection = urlRequest.openConnection() as HttpURLConnection
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    return null
                }

                val streamReader = InputStreamReader(connection.inputStream)
                val reader = BufferedReader(streamReader)

                val builder = StringBuilder()
                var line : String?
                var finish = false

                while (!finish) {
                    line = reader.readLine()

                    if (line == null) {
                        finish = true
                    }
                    else
                    {
                        builder.append(line)
                        builder.append("\n")
                    }
                }

                reader.close()
                streamReader.close()

                requestResult = builder.toString()

            }
            catch (e : Exception) {
                e.printStackTrace()
            }
            finally {
                connection?.disconnect()
            }

            return requestResult
        }
    }


}
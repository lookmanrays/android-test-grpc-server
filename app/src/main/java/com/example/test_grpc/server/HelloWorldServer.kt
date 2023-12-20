package com.example.test_grpc.server

import io.grpc.Grpc
import io.grpc.InsecureServerCredentials
import io.grpc.Server
import io.grpc.stub.StreamObserver
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import proto.v1.GreeterServiceGrpc
import proto.v1.SayHelloRequest
import proto.v1.SayHelloResponse
import java.io.IOException

class HelloWorldServer(private val port: Int) {
    private val server: Server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
        .addService(GreeterImpl())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
    }

    fun stop() {
        server.shutdown()
    }

    private class GreeterImpl : GreeterServiceGrpc.GreeterServiceImplBase() {
        override fun sayHello(req: SayHelloRequest, responseObserver: StreamObserver<SayHelloResponse>) {
            val joke = fetchJoke()
            val reply = SayHelloResponse.newBuilder().setMessage(joke).build()
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }

        private fun fetchJoke(): String {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://api.chucknorris.io/jokes/random")
                .build()

            return try {
                val response = client.newCall(request).execute()
                val jsonResponse = response.body?.string() ?: "{}"
                val jsonObject = JSONObject(jsonResponse)
                jsonObject.getString("value") ?: "No joke found"
            } catch (e: IOException) {
                "Error: ${e.message}"
            }
        }
    }
}
package com.example.test_grpc.client

import io.grpc.ManagedChannelBuilder
import proto.v1.GreeterServiceGrpc
import proto.v1.SayHelloRequest

class HelloWorldClient(private val host: String, private val port: Int) {
    private val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
    private val blockingStub = GreeterServiceGrpc.newBlockingStub(channel)

    fun sayHello(name: String): String {
        val request = SayHelloRequest.newBuilder().setName(name).build()
        return try {
            val response = blockingStub.sayHello(request)
            "Norris response: \n${response.message}"
        } catch (ex: Exception) {
            "Error: ${ex.message}"
        }
    }
}
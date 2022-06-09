package it.fscarponi.opensea

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import java.io.InputStream

class OpenSeaRepository {
    private val client= HttpClient(CIO)

    suspend fun getTileStream(row: Int, col: Int, zoomLvl: Int): InputStream= client.get("https://c.tile.openstreetmap.org/${zoomLvl}/$col/$row.png").bodyAsChannel().toInputStream()
}
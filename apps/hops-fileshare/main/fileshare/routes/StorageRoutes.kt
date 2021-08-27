package fileshare.routes

import fileshare.domain.StorageClient
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.features.origin
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.response.respondBytesWriter
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.utils.io.copyAndClose
import no.nav.helse.hops.routing.fullUrl

fun Routing.storageRoutes(storageClient: StorageClient) {
    authenticate {
        route("files") {
            post {
                val fileName = storageClient.save(call.request.receiveChannel(), call.request.contentType())

                val fileUrl = "${call.request.origin.fullUrl()}/$fileName"
                call.response.headers.append(HttpHeaders.Location, fileUrl)
                call.respond(HttpStatusCode.Created)
            }

            get("/{fileName}") {
                val fileName = call.parameters["fileName"]!!

                val response = storageClient.download(fileName)
                call.respondBytesWriter(response.contentType(), response.status) {
                    response.content.copyAndClose(this)
                }
            }
        }
    }
}
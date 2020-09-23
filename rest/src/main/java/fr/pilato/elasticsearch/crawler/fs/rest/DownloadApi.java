package fr.pilato.elasticsearch.crawler.fs.rest;

import fr.pilato.elasticsearch.crawler.fs.client.ElasticsearchClient;
import fr.pilato.elasticsearch.crawler.fs.settings.FsSettings;
import org.apache.http.client.utils.URIUtils;
import org.apache.logging.log4j.util.Base64Util;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Path("/fetch")
public class DownloadApi extends RestApi {

    private final ElasticsearchClient esClient;
    private final FsSettings settings;

    DownloadApi(FsSettings settings, ElasticsearchClient esClient) {
        this.settings = settings;
        this.esClient = esClient;
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get(@QueryParam("path") String path) throws UnsupportedEncodingException {
        path = URLDecoder.decode(path, "UTF-8");
        String rootFolder = this.settings.getFs().getUrl();
        java.nio.file.Path fullPath = Paths.get(rootFolder, path);

        logger.info("fetch file {}", fullPath.toString());
        if (!Files.exists(fullPath)) {
            throw new NotFoundException();
        } else {
            File file = fullPath.toFile();
            return Response.ok(file).build();
        }
    }

}

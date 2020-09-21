package edu.escuelaing.arep.docker;

import com.google.gson.Gson;
import com.mongodb.DBObjectCodec;
import org.bson.codecs.ObjectIdCodec;

import static spark.Spark.*;


public class LogService {

    public static DatosService datosService = new DatosService();

    public static void main(String[] args) {
        port(getPort());
        Gson gson = new Gson();
        get("/hello", (req,res) -> "Hello Docker!");

        get("/dato", (req, res) -> {
            res.type("application/json");
            System.out.println(req.queryParams("dato"));
            Datos dato = new Datos(req.queryParams("dato"));
            return datosService.addDato(dato);
        }, gson ::toJson);

        get("/",(req, res) -> {
            res.type("application/json");
            return datosService.getAllDatos();
        }, gson ::toJson);
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
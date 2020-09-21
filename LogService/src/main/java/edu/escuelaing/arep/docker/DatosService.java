package edu.escuelaing.arep.docker;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;

public class DatosService {
    MongoClient cliente = new MongoClient(new MongoClientURI("mongodb://ec2-54-162-150-53.compute-1.amazonaws.com:32768/"));
    Datastore dataStore = new Morphia().createDatastore(cliente,"datos");

    public String addDato(Datos dato){
        dataStore.save(dato);
        return "Dato añadido";
    }

    public List<Datos> getAllDatos(){
        List<Datos> datos = dataStore.find(Datos.class).asList();
        List<Datos> resultado = new ArrayList<>();
        if(datos != null) {
            if (datos.size() <= 10) {
                return datos;
            } else {
                int indice = (datos.size() - 10);
                for (int i = indice; i < datos.size(); i++) {
                    resultado.add(datos.get(i));
                }
                return resultado;
            }

        }
        return null;
    }
}

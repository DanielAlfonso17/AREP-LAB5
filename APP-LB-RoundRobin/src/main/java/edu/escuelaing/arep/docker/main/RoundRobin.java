package edu.escuelaing.arep.docker.main;

import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class RoundRobin {

    private static int balancer = 3;

    public static void main(String[] args) {
        port(getPort());
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "/index");
        });

        post("/",(req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            String valor = req.body().substring(7);
            BufferedReader in = null;
            balancer = loadBalancer();
            URL logService = null;
            if(balancer == 1){
                logService = new URL("http://ec2-54-162-150-53.compute-1.amazonaws.com:35001//dato?dato="+valor);
            }else if(balancer == 2){
                logService = new URL("http://ec2-54-162-150-53.compute-1.amazonaws.com:35002//dato?dato="+valor);
            }else if(balancer == 3){
                logService = new URL("http://ec2-54-162-150-53.compute-1.amazonaws.com:35003//dato?dato="+valor);
            }
            URLConnection conexion = logService.openConnection();
            in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            BufferedReader bufferIn = new BufferedReader(new InputStreamReader(System.in));
            String line = in.readLine();
            in.close();
            bufferIn.close();
            res.redirect("/resultados");
            return new ModelAndView(model, "/index");
        },new ThymeleafTemplateEngine());

        get("/resultados", (req, res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            BufferedReader in = null;
            balancer = loadBalancer();
            URL logService = null;
            if(balancer == 1){
                logService = new URL("http://ec2-54-162-150-53.compute-1.amazonaws.com:35001/");
            }else if(balancer == 2){
                logService = new URL("http://ec2-54-162-150-53.compute-1.amazonaws.com:35002/");
            }else if(balancer == 3){
                logService = new URL("http://ec2-54-162-150-53.compute-1.amazonaws.com:35003/");
            }
            URLConnection conexion = logService.openConnection();
            in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            BufferedReader bufferIn = new BufferedReader(new InputStreamReader(System.in));
            String line = in.readLine();
            in.close();
            bufferIn.close();
            line = line.replace("\\\"", "");
            model.put("result", line);
            return render(model, "/result");
        });

        post("/resultados" ,(req,res) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            res.redirect("/");
            return new ModelAndView(model, "/index");
        },new ThymeleafTemplateEngine());


    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    private static int loadBalancer(){
        if(balancer <= 1) {
            balancer = 3;
        }else {
            balancer--;
        }
        return balancer;
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5678;
    }
}

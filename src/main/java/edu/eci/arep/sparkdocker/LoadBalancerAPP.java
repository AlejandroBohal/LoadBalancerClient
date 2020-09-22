package edu.eci.arep.sparkdocker;


import edu.eci.arep.sparkdocker.WebClient.HttpClientBalanced;


import static spark.Spark.*;

/**
 * The type Load balancer app.
 */
public class LoadBalancerAPP {
    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String ... args){
        HttpClientBalanced clientBalanced = new HttpClientBalanced();
        port(getPort());
        staticFileLocation("/");
        get("/",(req,res)-> {
            res.redirect("/index.html");
            res.status(200);
            return null;
        });
        get("/messages",(req,res) -> clientBalanced.getMessages("/messages"));
        post("/messages",(req,res) -> clientBalanced.postMessage(req.body(), "/messages"));
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4568; //returns default port if heroku-port isn't set (i.e. on localhost)
    }


}

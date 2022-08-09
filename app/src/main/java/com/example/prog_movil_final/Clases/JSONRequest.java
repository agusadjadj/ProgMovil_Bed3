package com.example.prog_movil_final.Clases;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JSONRequest {

    private String FICH_WS = "http://fich.unl.edu.ar/reservas-fich/service/webservice-bedelia-movil.php?fecha_inicio=";//+ php?[fecha_inicio=2022-05-6]
    private String NEWS_WS = "https://www.unl.edu.ar/noticias/contents/getnewshome/limit:15"; // /limit:n
    private ArrayList<EventoAgenda> newsEvents = new ArrayList<>();
    private ArrayList<Clase> clasesDiarias = new ArrayList<>();

    public JSONRequest() {     }

    //Clase para devolver el día y poder formatear la consulta al WSS de FICHH
    private String getDay(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String nowDay = dtf.format(now).toString();
//        Log.e("DEBUG_DAY_NOW",nowDay);
        return nowDay;
    }

    public ArrayList<EventoAgenda> getNewsEvents() {
//        Log.e("DEBUG_PRE_RETURN_EVENTOS",String.valueOf(newsEvents.size()));
            return newsEvents;
    }

    public void consultarNoticias(Context contextFragment, final VolleyCallBack callBack){

        StringRequest getRequest = new StringRequest(Request.Method.GET, NEWS_WS, new Response.Listener<String>() {
            @Override
            public void onResponse(String getRequest) {
                try {
                    JSONArray jsonResponse = new JSONArray(getRequest);//JSONArray para ir por clases principales del JSON

                    JSONArray principalObject;
                    JSONObject jsonObject;      //JSONObject para ir desmenuzando el Array
//                    Log.e("DEBUG_LENGTH_JSON", String.valueOf(jsonResponse.length()));
//                    Log.e("DEBUG_JSON", jsonResponse.toString());

                    for (int i=0; i<jsonResponse.length(); i++){
                        EventoAgenda event = new EventoAgenda();         //Evento para ir cargando en la lista y seteando
                        //Primero traigo la informacion en news
                        principalObject = new JSONArray(jsonResponse.getJSONObject(i).getString("News"));

                        jsonObject = principalObject.getJSONObject(0); //El indice para todos es 0
                        //Primero tengo que sacar lo que existe en 0.
//                        Log.e("DEBUG_TITLE",jsonObject.getString("title").toString());

                        event.setTitle(jsonObject.getString("title").toString());          //Seteo el titulo
                        event.setBody(jsonObject.getString("body").toString());            //Seteo el body
                        event.setTime(jsonObject.getString("modified").toString());        //Seteo la fecha
                        event.setCopete(jsonObject.getString("copete").toString());        //Seteo el copete
                        event.setVolanta(jsonObject.getString("volanta").toString());      //Seteo la volanta

//                        String[] tmp_fecha = jsonObject.getString("modified").split("-");
//                        String fecha = tmp_fecha[2].substring(0,2)+"-"+tmp_fecha[1]+"-"+tmp_fecha[0];

                        //Luego traigo la informacion en mediafile
                        JSONArray auxArray = jsonObject.getJSONArray("Mediafile"); //Traigo el array de Mediafile
                        if(!(auxArray.length() == 0)) {
                            jsonObject = auxArray.getJSONObject(0);                     //El index sera 0
                            String pathAux1 = "https://web9.unl.edu.ar/noticias/img/thumbs/";
                            String dataPath = jsonObject.getString("path").toString();
                            String pathAux2 = dataPath.split("([.][A-z]+$)")[0];
                            String pathAux3 = dataPath.substring(pathAux2.length());

                            event.setPathImage(pathAux1+pathAux2+"_vga"+pathAux3);   //Traigo el path de la imagen
                        }

                        //Agrego a la lista el evento.
                        newsEvents.add(event);
//                        Log.e("DEBUG_INTERNO_NEWS",String.valueOf(newsEvents.size()));
//                        Log.e("FALLO EN LA ITERACION",String.valueOf(i));
                    }
                    callBack.onSuccess(newsEvents);
//                    Log.e("DEBUG_POST_PROCESS",String.valueOf(newsEvents.size()));


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });

        Volley.newRequestQueue(contextFragment).add(getRequest);
//        Log.e("DEBUG_PRE_RETURN",String.valueOf(newsEvents.size()));
//        return newsEvents;
    }



    public void consultarClases(Context contextFragment, final VolleyCallBack callBack){

        String nowDay = getDay();
//        String nowDay = "2022-08-5";
//        Log.e("DEBUG_DDAY",nowDay);

        StringRequest getRequest = new StringRequest(Request.Method.GET, (FICH_WS + nowDay).toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String getRequest) {
                try {
//                    Log.e("JSON_DEBUG",getRequest);
                    JSONObject jsonResponse = new JSONObject(getRequest);//JSONArray para ir por clases principales del JSON

                    JSONArray principalObject;
                    JSONObject jsonObject;      //JSONObject para ir desmenuzando el Array
                    principalObject = jsonResponse.getJSONArray("clases");

//                    Log.e("DEBUG_LENGTH_JSON", String.valueOf(principalObject.length()));

                    for (int i=0; i<principalObject.length(); i++){
                        Clase clase = new Clase();
                        //Cargo los valores del JSON a una clase.
                        clase.setId(String.valueOf(principalObject.getJSONObject(i).get("id")));
                        clase.setArea(String.valueOf(principalObject.getJSONObject(i).get("area")));
                        clase.setAula(String.valueOf(principalObject.getJSONObject(i).get("aula")));
                        clase.setEvento(String.valueOf(principalObject.getJSONObject(i).get("evento")));
                        clase.setDescripcion(String.valueOf(principalObject.getJSONObject(i).get("descripcion")));
                        clase.setFechaInicio(String.valueOf(principalObject.getJSONObject(i).get("fecha_inicio")));
                        clase.setHoraInicio(String.valueOf(principalObject.getJSONObject(i).get("hora_inicio")));
                        clase.setFechaFin(String.valueOf(principalObject.getJSONObject(i).get("fecha_fin")));
                        clase.setHoraFin(String.valueOf(principalObject.getJSONObject(i).get("hora_fin")));
                        clase.setEstado(String.valueOf(principalObject.getJSONObject(i).get("estado")));

                        clasesDiarias.add(clase);
                    }
                    callBack.onSuccess(clasesDiarias);
//                    Log.e("DEBUG_POST_PROCESS",String.valueOf(clasesDiarias.size()));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });

        Volley.newRequestQueue(contextFragment).add(getRequest);
//        Log.e("DEBUG_PRE_RETURN",String.valueOf(newsEvents.size()));
//        return newsEvents;
    }



}
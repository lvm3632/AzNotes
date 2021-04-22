package com.example.aznotes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Nota {

    private int id_nota;
    private String title;
    private String texto;
    private String fecha;

    public Nota(){}

    public Nota(String title, String texto, String fecha, int id_nota) {
        this.title = title;
        this.texto = texto;
        this.fecha = fecha;
        this.id_nota = id_nota;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {

        String temp = fechalegible(fecha);
        this.fecha = unixSecondsToDate(temp);
    }

    public int getId_Nota() {
        return id_nota;
    }

    public void setId_Nota(int id_nota) {
        this.id_nota = id_nota;
    }

    private String unixSecondsToDate(String timeStamp){
        long unixSeconds = Long.parseLong(timeStamp);
        // convert seconds to milliseconds
        Date date = new java.util.Date(unixSeconds*1000L);
        // the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-6"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    private String fechalegible(String valor){
        String resultado = "";
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("[0-9]+")
                .matcher(valor);
        while (m.find()) {
            allMatches.add(m.group());
            resultado = allMatches.get(0).toString();
        }
        return resultado;
    }
}

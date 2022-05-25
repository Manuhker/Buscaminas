package com.example.buscaminas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class tablero extends View {
    Casillas[][] casillas;
    public tablero(Context context){
        super(context);

    }
    @Override
    protected  void onDraw(Canvas canvas){
        canvas.drawRGB(0,0,0);
        int ancho=0;
        if (canvas.getWidth()<canvas.getHeight()){
            ancho=this.getWidth();
        }else {
            ancho=this.getHeight();
        }
        int Ancho=ancho/8;
        Paint pincel=new Paint();
        pincel.setTextSize(20);
        Paint pincel2=new Paint();
        pincel2.setTextSize(20);
        pincel2.setTypeface(Typeface.DEFAULT_BOLD);
        pincel2.setARGB(255,0,0,255);
        Paint pincelLineal=new Paint();
        pincelLineal.setARGB(255,255,255,255);
        int filaact=0;
        for(int i=0;i<8;i++){
            for(int c=0;c<8;c++){
                casillas[i][c].fijarxy(c*Ancho,filaact,Ancho);
                if(casillas[i][c].desocupada==false){
                    pincel.setARGB(153,204,204,204);
                }else{
                    pincel.setARGB(255,153,153,153);
                }
                canvas.drawRect(c*Ancho,filaact,
                        c*Ancho+Ancho-2,filaact+Ancho-2,pincel);
                canvas.drawLine(c*Ancho,filaact,c*Ancho+Ancho,filaact,pincelLineal);
                canvas.drawLine(c*Ancho+Ancho-1,filaact,c*Ancho+Ancho-1,filaact+Ancho,pincelLineal);
                if (casillas [i][c].contenido>=1 && casillas[i][c].contenido<=8 && casillas [i][c].desocupada){
                    canvas.drawText(String.valueOf(casillas[i][c].contenido),c*Ancho+(Ancho/2)-8,filaact+Ancho/2,pincel2);
                }
                if(casillas[i][c].contenido==80&&casillas[i][c].desocupada){
                    Paint bomba=new Paint();
                    bomba.setARGB(255,255,0,0);
                    canvas.drawCircle(c*Ancho+(Ancho/2),filaact
                            +(Ancho/2),8,bomba);

                }
            }

        }
        filaact=filaact+Ancho;
    }
}

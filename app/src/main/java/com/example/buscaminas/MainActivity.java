package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private Tablero fondo;
    int x, y;
    private Casillas[][] casillas;
    private boolean activo = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = (LinearLayout) findViewById(R.id.Layout1);
        fondo = new Tablero(this);
        fondo.setOnTouchListener(this);
        layout.addView(fondo);
        casillas = new Casillas[8][8];
        for (int i= 0; i < 8; i++) {
            for (int c = 0; c < 8; c++) {
                casillas[i][c] = new Casillas();
            }
        }
        this.disponerBombas();
        this.BombasPe();
        getSupportActionBar().hide();
    }

    public void reiniciar(View v) {
        casillas = new Casillas[8][8];
        for (int i= 0; i < 8; i++) {
            for (int c = 0; c < 8; c++) {
                casillas[i][c] = new Casillas();
            }
        }
        this.disponerBombas();
        this.BombasPe();
        activo = true;

        fondo.invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (activo)
            for (int i = 0; i < 8; i++) {
                for (int c = 0; c < 8; c++) {
                    if (casillas[i][c].dentro((int) event.getX(),
                            (int) event.getY())) {
                        casillas[i][c].desocupada = true;
                        if (casillas[i][c].contenido == 80) {
                            Toast.makeText(this, "Has perdido",
                                    Toast.LENGTH_LONG).show();
                            activo = false;
                        } else if (casillas[i][c].contenido == 0)
                            recorrer(i, c);
                        fondo.invalidate();
                    }
                }
            }
        if (gano() && activo) {
            Toast.makeText(this, "Has Ganado ", Toast.LENGTH_LONG).show();
            activo = false;
        }

        return true;
    }

    class Tablero extends View {

        public Tablero(Context context) {
            super(context);
        }
        protected void onDraw(Canvas canvas){
            canvas.drawRGB(0, 0, 0);
            int ancho = 0;
            if (canvas.getWidth() < canvas.getHeight())
                ancho = fondo.getWidth();
            else
                ancho = fondo.getHeight();
            int Ancho= ancho / 8;
            Paint paint = new Paint();
            paint.setTextSize(20);
            Paint paint2 = new Paint();
            paint2.setTextSize(20);
            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            paint2.setARGB(255, 0, 0, 255);
            Paint paintlinea1 = new Paint();
            paintlinea1.setARGB(255, 255, 255, 255);
            int filaact = 0;
            for (int i = 0; i < 8; i++) {
                for (int c = 0; c < 8; c++) {
                    casillas[i][c].fijarxy(c * Ancho, filaact, Ancho);
                    if (casillas[i][c].desocupada == false)
                        paint.setARGB(153, 204, 204, 204);
                    else
                        paint.setARGB(255, 153, 153, 153);
                    canvas.drawRect(c * Ancho, filaact, c * Ancho
                            + Ancho - 2, filaact + Ancho - 2, paint);

                    canvas.drawLine(c * Ancho, filaact, c * Ancho
                            + Ancho, filaact, paintlinea1);
                    canvas.drawLine(c * Ancho + Ancho - 1, filaact, c
                                    * Ancho + Ancho - 1, filaact + Ancho,
                            paintlinea1);

                    if (casillas[i][c].contenido >= 1
                            && casillas[i][c].contenido <= 8
                            && casillas[i][c].desocupada)
                        canvas.drawText(
                                String.valueOf(casillas[i][c].contenido), c
                                        * Ancho + (Ancho / 2) - 8,
                                filaact + Ancho / 2, paint2);

                    if (casillas[i][c].contenido == 80
                            && casillas[i][c].desocupada) {
                        Paint bomba = new Paint();
                        bomba.setARGB(255, 255, 0, 0);
                        canvas.drawCircle(c * Ancho + (Ancho / 2),
                                filaact + (Ancho / 2), 8, bomba);
                    }

                }
                filaact = filaact + Ancho;
            }
        }
    }

    private void disponerBombas() {
        int cantidad = 8;
        do {
            int fila = (int) (Math.random() * 8);
            int columna = (int) (Math.random() * 8);
            if (casillas[fila][columna].contenido == 0) {
                casillas[fila][columna].contenido = 80;
                cantidad--;
            }
        } while (cantidad != 0);
    }

    private boolean gano() {
        int cant = 0;
        for (int f = 0; f < 8; f++)
            for (int c = 0; c < 8; c++)
                if (casillas[f][c].desocupada)
                    cant++;
        if (cant == 56)
            return true;
        else
            return false;
    }

    private void BombasPe() {
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (casillas[f][c].contenido == 0) {
                    int cant = Coordenada(f, c);
                    casillas[f][c].contenido = cant;
                }
            }
        }
    }

    int Coordenada(int fila, int columna) {
        int total = 0;
        if (fila - 1 >= 0 && columna - 1 >= 0) {
            if (casillas[fila - 1][columna - 1].contenido == 80)
                total++;
        }
        if (fila - 1 >= 0) {
            if (casillas[fila - 1][columna].contenido == 80)
                total++;
        }
        if (fila - 1 >= 0 && columna + 1 < 8) {
            if (casillas[fila - 1][columna + 1].contenido == 80)
                total++;
        }

        if (columna + 1 < 8) {
            if (casillas[fila][columna + 1].contenido == 80)
                total++;
        }
        if (fila + 1 < 8 && columna + 1 < 8) {
            if (casillas[fila + 1][columna + 1].contenido == 80)
                total++;
        }

        if (fila + 1 < 8) {
            if (casillas[fila + 1][columna].contenido == 80)
                total++;
        }
        if (fila + 1 < 8 && columna - 1 >= 0) {
            if (casillas[fila + 1][columna - 1].contenido == 80)
                total++;
        }
        if (columna - 1 >= 0) {
            if (casillas[fila][columna - 1].contenido == 80)
                total++;
        }
        return total;
    }

    private void recorrer(int fil, int col) {
        if (fil >= 0 && fil < 8 && col >= 0 && col < 8) {
            if (casillas[fil][col].contenido == 0) {
                casillas[fil][col].desocupada= true;
                casillas[fil][col].contenido = 50;
                recorrer(fil, col + 1);
                recorrer(fil, col - 1);
                recorrer(fil + 1, col);
                recorrer(fil - 1, col);
                recorrer(fil - 1, col - 1);
                recorrer(fil - 1, col + 1);
                recorrer(fil + 1, col + 1);
                recorrer(fil + 1, col - 1);
            } else if (casillas[fil][col].contenido >= 1
                    && casillas[fil][col].contenido <= 8) {
                casillas[fil][col].desocupada = true;
            }
        }
    }

}
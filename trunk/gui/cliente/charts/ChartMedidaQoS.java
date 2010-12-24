/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.cliente.charts;

import info.monitorenter.gui.chart.*;
import info.monitorenter.gui.chart.labelformatters.*;
import info.monitorenter.gui.chart.traces.*;
import info.monitorenter.gui.chart.traces.painters.*;
import info.monitorenter.gui.chart.rangepolicies.*;
import info.monitorenter.gui.chart.pointhighlighters.*;
import info.monitorenter.gui.chart.pointpainters.*;
import info.monitorenter.util.Range;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.io.File;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.SortedSet;
import java.util.Iterator;
import java.text.DecimalFormat;
import qos.estimacionQoS.*;
import qos.estimacionQoS.parametrosQoS.*;

/**
 *
 * @author Antonio
 */
public class ChartMedidaQoS extends ZoomableChart {

    MedidaQoS medidaQoS;

    float[] maximoDeIntervalos;
    float[] minimoDeIntervalos;
    float[] mediaDeIntervalos;


    public ChartMedidaQoS(MedidaQoS medidaQoS) {

        super();

        this.medidaQoS = medidaQoS;
        float[][] ejeX = medidaQoS.valoresEjeX();
        float[][] ejeY = medidaQoS.valoresEjeY();
        int numeroIntervalos = medidaQoS.getParametrosTransmision().getNumeroIntervalos();        

        Trace2DSimple trace = new Trace2DSimple();
        TracePainterDisc tpd;
        for(int i=0; i<numeroIntervalos; i++) {

            //creación de las curvas de cada intervalo
            trace = new Trace2DSimple();
            tpd = new TracePainterDisc();
            tpd.setDiscSize(5);
            trace.setTracePainter(new TracePainterLine());
            trace.addTracePainter(tpd);
            trace.addPointHighlighter(
                    new PointHighlighterConfigurable(new PointPainterDisc(13), true)
                    );

            trace.setColor(Color.BLUE);
            trace.setName("interval " + i +" ");
            
            super.addTrace(trace);
            for(int j=0; j<ejeY[i].length; j++) {
               if(ejeY[i][j] != ParametroQoS.VALOR_DEFECTO) {
                trace.addPoint(
                        new TracePoint2D( ejeX[i][j] , ejeY[i][j] )
                        );
               }
            }            
            if(trace.isEmpty())
                trace.addPoint(Float.NaN, Float.NaN);                                
        }

        //titulo de los ejes
        super.getAxisX().getAxisTitle().setTitle(
                this.tituloEje( medidaQoS.nombreEjeX() , this.medidaQoS.unidadesEjeXtoString() )
                );
        super.getAxisY().getAxisTitle().setTitle(
               this.tituloEje( medidaQoS.nombreEjeY() , this.medidaQoS.unidadesEjeYtoString() )
                );

        //formato etiquetas ejes
        super.getAxisX().setFormatter(new LabelFormatterSimple());
        super.getAxisY().setFormatter(new LabelFormatterSimple());

        //formato fuente ejes
        Font axisFont = new Font(Font.DIALOG, Font.BOLD, 15);
        super.getAxisX().getAxisTitle().setTitleFont(axisFont);
        super.getAxisY().getAxisTitle().setTitleFont(axisFont);

        //ver grid
        super.getAxisX().setPaintGrid(true);
        super.getAxisY().setPaintGrid(true);
       
        super.getAxisY().setRangePolicy(
                new RangePolicyMinimumViewport(new Range(0,1) )
                );

        super.enablePointHighlighting(true);
        super.setUseAntialiasing(true);
        super.setToolTipType(Chart2D.ToolTipType.VALUE_SNAP_TO_TRACEPOINTS);

        //calcular estadísticas
        this.calcularEstadisticas();
    }


  /**
   * Resets the zooming area to a range that displays all data.
   * <p>
   */
  public void zoomAll() {

      super.zoomAll();
      this.getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(0,1)));
  }

    /**
     * obtiene los valores máximo, mínimo y medio por cada intervalo
     */
    private void calcularEstadisticas() {

        int numeroIntervalos = this.medidaQoS.getParametrosTransmision().
                getNumeroIntervalos();
        this.maximoDeIntervalos = new float[numeroIntervalos];
        this.minimoDeIntervalos = new float[numeroIntervalos];
        this.mediaDeIntervalos = new float[numeroIntervalos];

        for(int i=0; i<numeroIntervalos; i++) {
            this.maximoDeIntervalos[i] = this.medidaQoS.getParametroQoS(i).valorMaximo();
            this.minimoDeIntervalos[i] = this.medidaQoS.getParametroQoS(i).valorMinimo();
            this.mediaDeIntervalos[i] = this.medidaQoS.getParametroQoS(i).valorMedio();
        }

    }


    private String tituloEje(String magnitud, String unidad) {

        String titulo="";
        titulo += magnitud;
        if(!unidad.equals("")){
            titulo+= " (" + unidad + ")";
        }
        return titulo;
    }

    /**
     * Genera los archivos txt con los datos relevantes de la medida
     * -condiciones de medida
     * -valores
     * -estadísticas (máximo, mínimo y media)
     */
     public void exportarAtxt(File directorioPadre)
            throws IOException {

         FileOutputStream fos;
         BufferedWriter writer;
         DecimalFormat decimalFormatter = (DecimalFormat) DecimalFormat.getInstance();
         decimalFormatter.setMaximumFractionDigits(4);
         decimalFormatter.setMinimumFractionDigits(4);

         //Creación del directorio para albergar los ficheros .txt
         File directorio = new File(
                 directorioPadre,
                 this.medidaQoS.fechaDeMedidaToString() + " " +this.medidaQoS.nombreParametroQoS()
                 );
         directorio.mkdir();

         //ARCHIVO DE CONDICIONES
         File archivoCondiciones = new File(directorio, "conditions" + directorio.getName()+ ".txt");
         fos = new FileOutputStream(archivoCondiciones);
         writer = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-16")));
         writer.write(this.medidaQoS.condicionesDeMedidaToString());
         writer.flush();
         writer.close();
         
         //ARCHIVO DE VALORES
         File archivoValores = new File(directorio, "values" + directorio.getName() +".txt");
         fos = new FileOutputStream(archivoValores);
         writer = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-16")));
         //cabecera
         writer.write(super.getAxisX().getAxisTitle().getTitle());
         writer.write(",");
         writer.write('\t');
         writer.write(super.getAxisY().getAxisTitle().getTitle());
         writer.newLine();
         //valores
         SortedSet<ITrace2D> traces = super.getTraces();
         Iterator<ITrace2D> iteratorTraces = traces.iterator();
         ITrace2D trace;
         while (iteratorTraces.hasNext()) {

             trace = iteratorTraces.next();
             Iterator<ITracePoint2D> iteratorPoints = trace.iterator();
             ITracePoint2D point;
             while (iteratorPoints.hasNext()) {
                 point = iteratorPoints.next();
                 writer.write(decimalFormatter.format(point.getX()));
                 writer.write('\t');
                 writer.write(",");
                 writer.write('\t');
                 writer.write(decimalFormatter.format(point.getY()));
                 writer.newLine();
             }
         }

         writer.flush();
         writer.close();

         //ARCHIVO DE ESTADÍSTICAS
         File archivoEstadisticas = new File(directorio, "statistics" + directorio.getName() +".txt");
         fos = new FileOutputStream(archivoEstadisticas);
         writer = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-16")));
         //cabecera
         writer.write("interval");
         writer.write(",");
         writer.write('\t');
         writer.write(super.getAxisY().getAxisTitle().getTitle() +" max");
         writer.write(",");
         writer.write('\t');
         writer.write(super.getAxisY().getAxisTitle().getTitle() +" min");
         writer.write(",");
         writer.write('\t');
         writer.write(super.getAxisY().getAxisTitle().getTitle() +" mean");
         writer.newLine();
         //valores
         for(int i=0; i<this.maximoDeIntervalos.length; i++) {
             writer.write(Integer.toString(i));
             writer.write(",");
             writer.write('\t');
             writer.write(decimalFormatter.format(this.maximoDeIntervalos[i]));
             writer.write(",");
             writer.write('\t');
             writer.write(decimalFormatter.format(this.minimoDeIntervalos[i]));
             writer.write(",");
             writer.write('\t');
             writer.write(decimalFormatter.format(this.mediaDeIntervalos[i]));
             writer.newLine();
         }

         writer.flush();
         writer.close();


    }

   

}




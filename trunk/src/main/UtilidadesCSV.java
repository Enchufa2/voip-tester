/*
Este fichero es parte de VoIPTester

    Copyright (C) 2009-2010
    	      Antonio Sánchez Navarro (titosanxez@gmail.com)
	      Juan M. López Soler (juanma@ugr.es)
	      Jorge Navarro Ortiz (jorgenavarro@ugr.es)


    Este programa es software libre: usted puede redistribuirlo y/o modificarlo
    bajo los términos de la Licencia Pública General GNU publicada
    por la Fundación para el Software Libre, ya sea la versión 3
    de la Licencia, o (a su elección) cualquier versión posterior.

    Este programa se distribuye con la esperanza de que sea útil, pero
    SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita
    MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO.
    Consulte los detalles de la Licencia Pública General GNU para obtener
    una información más detallada.

    Debería haber recibido una copia de la Licencia Pública General GNU
    junto a este programa.
    En caso contrario, consulte <http://www.gnu.org/licenses/>.

*/
package main;

import qos.estimacionQoS.MedidaQoS;
import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author Antonio
 */
public class UtilidadesCSV {

    public static void exportMedidaQoStoCSV(
            MedidaQoS medidaQoS,
            File directorioPadre
            ) throws IOException{

        FileOutputStream fos;
        BufferedWriter writer;
        DecimalFormat decimalFormatter = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        decimalFormatter.setGroupingUsed(false);
        decimalFormatter.setMaximumFractionDigits(4);
        decimalFormatter.setMinimumFractionDigits(4);

        String fileName = medidaQoS.nombreParametroQoS() + "_" + medidaQoS.fechaDeMedidaToString();

        //ARCHIVO DE CONDICIONES
        File archivoCondiciones = new File(directorioPadre, fileName + "conditions" + ".csv");
        fos = new FileOutputStream(archivoCondiciones);
        writer = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("US-ASCII")));
        writer.write(medidaQoS.condicionesDeMedidaToString());
        writer.flush();
        writer.close();

        //ARCHIVO DE VALORES
        UtilidadesCSV.writeToCsv(medidaQoS, directorioPadre);

    }

    public static void exportarMedidaQoStoCSV(
            ArrayList<MedidaQoS> medidasQoS,
            File directorioPadre
            )throws IOException{

        //CreaciÃ³n del directorio para albergar los ficheros .txt
        String nameDir = medidasQoS.get(0).fechaDeMedidaToString() +"_pT_"
                + medidasQoS.get(0).getParametrosTransmision().getTiempoEntreEnvios() +"_"
                + medidasQoS.get(0).getParametrosTransmision().getLongitudDatos() +"_"
                + medidasQoS.get(0).getParametrosTransmision().getMensajesPorIntervalo() +"_"
                + medidasQoS.get(0).getParametrosTransmision().getNumeroIntervalos();

        File directorio = new File(
                directorioPadre,
                nameDir);
        directorio.mkdir();
        
        for(MedidaQoS mq: medidasQoS) {

            UtilidadesCSV.writeToCsv(mq, directorio);
        }
    }

    /**
     * 
     */
    public static void writeToCsv(MedidaQoS medidaQoS, File directorioPadre)
            throws IOException {

        FileOutputStream fos;
        BufferedWriter writer;
        DecimalFormat decimalFormatter = (DecimalFormat) DecimalFormat.getInstance(Locale.US);
        decimalFormatter.setGroupingUsed(false);
        decimalFormatter.setMaximumFractionDigits(4);
        decimalFormatter.setMinimumFractionDigits(4);

        String fileName = medidaQoS.nombreEjeX() + "_" + medidaQoS.unidadesEjeXtoString()
                + "_" + medidaQoS.nombreEjeY() + "_" + medidaQoS.unidadesEjeYtoString()
                + "_" + medidaQoS.fechaDeMedidaToString();

        //ARCHIVO DE VALORES
        File archivoValores = new File(directorioPadre, fileName + ".csv");
        fos = new FileOutputStream(archivoValores);
        writer = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("US-ASCII")));
        //parametros de transmisiÃ³n
        writer.write(
                decimalFormatter.format(medidaQoS.getParametrosTransmision().getTiempoEntreEnvios())
                );
        writer.write(",");
        writer.write('\t');
        writer.write(
                decimalFormatter.format(medidaQoS.getParametrosTransmision().getLongitudDatos())
                );
        writer.write(",");
        writer.write('\t');
        writer.write(
                decimalFormatter.format(medidaQoS.getParametrosTransmision().getMensajesPorIntervalo())
                );
        writer.write(",");
        writer.write('\t');
        writer.write(
                decimalFormatter.format(medidaQoS.getParametrosTransmision().getNumeroIntervalos())
                );
        writer.newLine();
        //valores
        float[][] ejeX = medidaQoS.valoresEjeX();
        float[][] ejeY = medidaQoS.valoresEjeY();
        int numeroIntervalos = ejeX.length;
        for (int i = 0; i < numeroIntervalos; i++) {
            for (int j = 0; j < ejeY[0].length; j++) {

                writer.write(decimalFormatter.format(ejeX[i][j]));
                writer.write('\t');
                writer.write(",");
                writer.write('\t');
                writer.write(decimalFormatter.format(ejeY[i][j]));
                writer.newLine();

            }
        }
        writer.flush();
        writer.close();
        
    }


}

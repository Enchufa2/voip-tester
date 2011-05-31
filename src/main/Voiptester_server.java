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

import argparser.*;
import nucleo.servidor.*;


/**
 *
 * @author Antonio
 */
public class Voiptester_server {


    public static void main(String args[]) {

        String usage = "[{-sH, --serverHost} host] [{-sP, --serverPort} port] [{-aP, --activityPeriod} ap] [{-help, -?}]";

        //elementos para parser de linea
        ArgParser parser = new ArgParser("voiptester_server" + usage);
        StringHolder host = new StringHolder();
        host.value="localhost";
        parser.addOption("-sH, --serverHost %s #Select the register server host or IP to wait requests", host);

        IntHolder port = new IntHolder();
        port.value=4662;
        parser.addOption("-sP, --serverPort %d #Select the register server port to wait requests", port);

        IntHolder period = new IntHolder();
        period.value = 10;
        parser.addOption("-aP, --activityPeriod %d #Set the client activity period of time in seconds. Spent this time," +
                "the server checks the client availability in order to accept requests", period);

        //parsear comando
        parser.matchAllArgs(args);
        
        //elementos para voiptester
        ControlServidor controlServidor;
        

        //Iniciar servidor
        controlServidor = new ControlServidor();
        controlServidor.ajustarPeriodoActividad(period.value);
        //System.setProperty("java.rmi.server.hostname", host.value);

        try{

            controlServidor.registrarServidor(host.value, port.value);            
            System.out.println("server started on " +host.value + ":" + port.value);

        }catch(ControlServidorException cse) {

            System.out.println(cse.getMessage() + ": "+ cse.getCause().getMessage());
            System.exit(-1);

        }

    }

}

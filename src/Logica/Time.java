/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Time {

    public static void obtenerCPUTime(long startSystemTimeNano,  long startUserTimeNano){
        long taskUserTimeNano = getUserTime( ) - startUserTimeNano;
        long taskSystemTimeNano = getSystemTime( ) - startSystemTimeNano;
        double canti=((double)(taskSystemTimeNano+taskUserTimeNano)/1000000000);
        if(canti<0) {
        	canti=canti*(-1);
        }
        System.out.println("CPU TIME: "+ canti +" segundos");
    }
    
    public static long getCpuTime( ) {
     ThreadMXBean bean =  ManagementFactory.getThreadMXBean( ) ;
     return bean.isCurrentThreadCpuTimeSupported( ) ?
         bean.getCurrentThreadCpuTime( ) : 0L;
    }
    public static long getUserTime( ) {
     ThreadMXBean bean =  ManagementFactory.getThreadMXBean( ) ;
     return bean.isCurrentThreadCpuTimeSupported( ) ?
         bean.getCurrentThreadUserTime( ) : 0L;
    }

 /** Get system time in nanoseconds.  */
    public static long getSystemTime( ) {
        ThreadMXBean bean =  ManagementFactory.getThreadMXBean( ) ;
        return bean.isCurrentThreadCpuTimeSupported( ) ?
            (bean.getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime() ) : 0L;
    } 
    
}

/*long startSystemTimeNano = getSystemTime( );
  long startUserTimeNano = getUserTime( );*/

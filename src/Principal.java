import java.io.IOException;
import java.util.Scanner;

import Logica.Time;

public class Principal {

	public static void main(String[] args) throws Exception {
		String comando;
		String [] argumentos;
		System.out.print(">>> ");
		Scanner sc = new Scanner(System.in);
		comando = sc.nextLine();
		argumentos = comando.split(" ");
		Tabla t;
				
		while (!argumentos[0].equalsIgnoreCase("SALIR")) {
				
		
			if(argumentos[0].equals("CREAR") && argumentos[1].equals("TABLA") && argumentos.length >=8){
				if( AnalizarMetadatos.existe(argumentos[2])) {
					System.out.println("La tabla \""+argumentos[2]+"\" existe.");
				}else {
					if (!argumentos [3].equals("CAMPOS") || !argumentos[5].equals("CLAVE") || !argumentos[7].equals("LONGITUD")) {
						System.out.println("SINTAXIS Incorrecta");
					}else {
						if(argumentos[4].split(",").length == argumentos[8].split(",").length) {
							try {
								if (argumentos[9].equals("ENCRIPTADO") && argumentos.length == 11){
									long startTimeMs = System.currentTimeMillis ( );

									long startSystemTimeNano = Time.getSystemTime( );
									long startUserTimeNano = Time.getUserTime( );

									AnalizarMetadatos.escribirMetadata(argumentos[2], argumentos[4], argumentos[6], argumentos[8], argumentos[10]);
									t = new Tabla(argumentos[2]);
									t.crearTabla();
									System.out.println("Nueva tabla \""+argumentos[2] +"\" escrita en el metadatos");
									
									Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
									long taskTimeMs = System.currentTimeMillis () - startTimeMs;
					                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
														
								}	
							}
							catch (Exception e) {
								long startTimeMs = System.currentTimeMillis ( );
								long startSystemTimeNano = Time.getSystemTime( );
								long startUserTimeNano = Time.getUserTime( );

								AnalizarMetadatos.escribirMetadata(argumentos[2], argumentos[4], argumentos[6], argumentos[8], "F");
								t = new Tabla(argumentos[2]);
								t.crearTabla();
								System.out.println("Nueva tabla \""+argumentos[2] +"\" escrita en el metadatos");
								Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
								long taskTimeMs = System.currentTimeMillis () - startTimeMs;
				                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
							}
						}else {
								System.out.println("Escriba una longitud para cada campo");
						}
					}
				}
			}
			//MODIFICAR TABLA
			else if(argumentos[0].equals("MODIFICAR") && argumentos[1].equals("TABLA")&& argumentos[3].equals("CAMPO")&& argumentos[5].equals("POR") && argumentos.length==7) {
				if (!AnalizarMetadatos.modificarCamposMetadata(argumentos[2], argumentos[4], argumentos[6])) {
					System.out.println("EL campo \""+ argumentos[4]+"\"  no existe en la tabla \""+argumentos[2]+"\"");
				}
				else {
					long startTimeMs = System.currentTimeMillis ( );
					long startSystemTimeNano = Time.getSystemTime( );
					long startUserTimeNano = Time.getUserTime( );

					t = new Tabla(argumentos[2]);
					AnalizarMetadatos.llenarTabladeMetadatos(t);
					t.modificarEncabezado();
					System.out.println("Actualizado con éxito");
					Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
					long taskTimeMs = System.currentTimeMillis () - startTimeMs;
	                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
				}
			}
			else if(argumentos[0].equals("ELIMINAR") && argumentos[1].equals("TABLA") && argumentos.length == 3){
				long startTimeMs = System.currentTimeMillis ( );
				long startSystemTimeNano = Time.getSystemTime( );
				long startUserTimeNano = Time.getUserTime( );

				AnalizarMetadatos.borrarTabla(argumentos[2]);
				Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
				long taskTimeMs = System.currentTimeMillis () - startTimeMs;
                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
				
			}
			//INSERTAR EN nombre_tabla VALORES vCampo1 , vCampo2 ,... , vCampoN
			else if(argumentos[0].equals("INSERTAR") && argumentos[1].equals("EN") && argumentos[3].equals("VALORES") && argumentos.length == 5){
				if(AnalizarMetadatos.existe(argumentos[2])) {
					long startTimeMs = System.currentTimeMillis ( );
					long startSystemTimeNano = Time.getSystemTime( );
					long startUserTimeNano = Time.getUserTime( );

					t = new Tabla(argumentos[2]);
					AnalizarMetadatos.llenarTabladeMetadatos(t);
					t.agregarRegistro(argumentos[4]);	
					Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
					long taskTimeMs = System.currentTimeMillis () - startTimeMs;
	                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
				}else {
					System.out.println("No existe la tabla \""+argumentos[2]+"\" en el metadatos");
				}
			//LEER nombreArchivo PARA nombreTABLA 
			}
			else if(argumentos[0].equals("LEER")&&argumentos[2].equals("PARA") && argumentos.length== 4) {
				if(AnalizarMetadatos.existe(argumentos[3])) {
					long startTimeMs = System.currentTimeMillis ( );
					long startSystemTimeNano = Time.getSystemTime( );
					long startUserTimeNano = Time.getUserTime( );

					t = new Tabla(argumentos[3]);
					AnalizarMetadatos.llenarTabladeMetadatos(t);
					t.registosDeArchivo("archivos/"+argumentos[1]+".csv");	
					Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
					long taskTimeMs = System.currentTimeMillis () - startTimeMs;
	                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
				}else {
					System.out.println("No existe la tabla \""+argumentos[3]+"\" en el metadatos");
				}
			}
			//ACTUALIZAR REGISTRO nombre_tabla CLAVE valorCampoClave CAMPO campo POR valor_campo_nuevo 
			else if(argumentos[0].equals("ACTUALIZAR")&&argumentos[1].equals("REGISTRO") && argumentos[3].equals("CLAVE") && argumentos[5].equals("CAMPO") && argumentos[7].equals("POR") && argumentos.length== 9) {
				if(AnalizarMetadatos.existe(argumentos[2])) {
					long startTimeMs = System.currentTimeMillis ( );
					long startSystemTimeNano = Time.getSystemTime( );
					long startUserTimeNano = Time.getUserTime( );

					t = new Tabla(argumentos[2]);
					AnalizarMetadatos.llenarTabladeMetadatos(t);
	
					if(t.modificarRegistro(argumentos[4], argumentos[6], argumentos[8])) {
						System.out.println("Modificado con éxito");					
					}else {
						System.out.println("No se pudo modificar");
					}

					Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
					long taskTimeMs = System.currentTimeMillis () - startTimeMs;
	                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
				}else {
					System.out.println("No existe la tabla \""+argumentos[2]+"\" en el metadatos");
				}
			}
			//BORRAR REGISTRO tabla1 CLAVE 1
			else if(argumentos[0].equals("BORRAR")&&argumentos[1].equals("REGISTRO") && argumentos[3].equals("CLAVE") && argumentos.length== 5) {
						if(AnalizarMetadatos.existe(argumentos[2])) {
							long startTimeMs = System.currentTimeMillis ( );
							long startSystemTimeNano = Time.getSystemTime( );
							long startUserTimeNano = Time.getUserTime( );

							t = new Tabla(argumentos[2]);
							AnalizarMetadatos.llenarTabladeMetadatos(t);
	
							if(t.eliminarRegistro(argumentos[4])) {
								System.out.println("Eliminado logicamente con éxito");					
							}else {
								System.out.println("No se pudo eliminar");
							}
							Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
							long taskTimeMs = System.currentTimeMillis () - startTimeMs;
			                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
						}else {
							System.out.println("No existe la tabla \""+argumentos[2]+"\" en el metadatos");
						}
			}
			//UNIR
			else if(argumentos[0].equals("UNIR") && argumentos[2].equals("POR") && argumentos.length==4){// && argumentos[4].equals("ORDENADO")&& argumentos[6].equals("VER") && 
				String []tablas=argumentos[1].split(",");
				long startTimeMs = System.currentTimeMillis ( );
				long startSystemTimeNano = Time.getSystemTime( );
				long startUserTimeNano = Time.getUserTime( );

				if(tablas.length==2){
					Tabla t1 = new Tabla(tablas[0]);
					AnalizarMetadatos.llenarTabladeMetadatos(t1);
					Tabla t2 = new Tabla(tablas[1]);
					AnalizarMetadatos.llenarTabladeMetadatos(t2);
					t1.unirTabla(t2, argumentos[3]);
					Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
					long taskTimeMs = System.currentTimeMillis () - startTimeMs;
	                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
				}else{
					System.out.println("Numero de tablas no permitido....");
				}
			}
			//SELECCIONAR
			else if(argumentos[0].equals("SELECCIONAR")) {
				try {
					if(argumentos.length==11 && argumentos[0].equals("SELECCIONAR") && argumentos[1].equals("DE") && 
		                    argumentos[3].equals("DONDE") && argumentos[5].equals("=") && 
		                    argumentos[7].equals("ORDENADO") && argumentos[9].equals("VER")){
		                String nombreTabla = argumentos[2];
		                String nombreCampo = argumentos[4];
		                String valorCampo = argumentos[6];
		                String ordenacion = argumentos[8];
		                String numRegis = argumentos[10];
		                long startTimeMs = System.currentTimeMillis ( );
		                long startSystemTimeNano = Time.getSystemTime( );
						long startUserTimeNano = Time.getUserTime( );

		                t = new Tabla(nombreTabla);
		                AnalizarMetadatos.llenarTabladeMetadatos(t);
		                int num=0;
		                try{
		                	num = Integer.parseInt(numRegis);

		                	t.buscarRegistro(nombreCampo, valorCampo,ordenacion,num);
		                }	
		                catch(Exception e){
		                	System.out.println("NUMERO INCORRECTO DE REGISTROS");
		                }
						Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
		                long taskTimeMs = System.currentTimeMillis () - startTimeMs;
		                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
		            }
					
				}catch (Exception e) {

					if(argumentos[0].equals("SELECCIONAR") && argumentos[1].equals("DE") && 
		                    argumentos[3].equals("DONDE") && argumentos[5].equals("=")){

		                String nombreTabla = argumentos[2];
		                String nombreCampo = argumentos[4];
		                String valorCampo = argumentos[6];
		            
		                long startTimeMs = System.currentTimeMillis ( );
		                long startSystemTimeNano = Time.getSystemTime( );
						long startUserTimeNano = Time.getUserTime( );

		                t = new Tabla(nombreTabla);
		                AnalizarMetadatos.llenarTabladeMetadatos(t);
		                
		                t.buscarRegistro(nombreCampo, valorCampo,"asc",t.getNumRegistros());
						Time.obtenerCPUTime(startSystemTimeNano, startUserTimeNano);
		                long taskTimeMs = System.currentTimeMillis () - startTimeMs;
		                System.out.println("WALL TIME: "+ (double)taskTimeMs/1000+ " segundos");
		            }

				}

			}
			else {
				System.out.println("Comando no valido");
			}
			System.out.print(">>> ");
			comando = sc.nextLine();
			argumentos = comando.split(" ");
		}
		
		
		
		String todasLasTablas= AnalizarMetadatos.nombresTablas();
		for(int i=0; i<todasLasTablas.split(",").length;i++) {
			t = new Tabla(todasLasTablas.split(",")[i]);
			AnalizarMetadatos.llenarTabladeMetadatos(t);
			t.compactarArchivo();	
		}
		
		System.out.println("Saliendo...");
	
	}
	
	
}

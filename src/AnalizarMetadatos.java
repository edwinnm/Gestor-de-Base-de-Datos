
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;


public class AnalizarMetadatos {
	static String CAMINO = "archivos/metadatos.txt";

	public static boolean existe(String nombreTabla) throws IOException {
		String actual="";
		String [] campos;
		BufferedReader metadata;
		try {
			metadata = new BufferedReader(new FileReader(CAMINO));
			//Lectura hasta el final de un archivo
			while((actual = metadata.readLine()) != null) {
				campos = actual.split(" ");
				if (nombreTabla.equals(campos[0])) {
					return true;
				}
			}
			return false;
			
		} catch (FileNotFoundException e) {
			System.out.println("NO existe el directorio");
			
		}
		
		return false;		
	}
	public static String nombresTablas() throws IOException {
		String actual="";
		String [] campos;
		String nombresTablas="";
		BufferedReader metadata;
		try {
			metadata = new BufferedReader(new FileReader(CAMINO));
			//Lectura hasta el final de un archivo
			while((actual = metadata.readLine()) != null) {
				campos = actual.split(" ");
				nombresTablas+=campos[0]+",";
			}
			
			
		} catch (FileNotFoundException e) {
			System.out.println("NO existe el directorio");
			
		}
		
		return nombresTablas.substring(0, nombresTablas.length()-1);		
	}
	public static boolean existeCampo(String nombreTabla, String nombreCampo) throws IOException {
		String actual="";
		String [] campos;
		BufferedReader metadata;
		try {
			metadata = new BufferedReader(new FileReader(CAMINO));
			//Lectura hasta el final de un archivo
			while((actual = metadata.readLine()) != null) {
				campos = actual.split(" ");
				if (campos[1].contains(nombreCampo)&&campos[0].equals(nombreTabla)) {
					
					return true;
				}
			}
			return false;
			
		} catch (FileNotFoundException e) {
			System.out.println("NO existe el directorio");
			
		}
		
		return false;
		
	}
	
	public static Tabla llenarTabladeMetadatos(Tabla t) throws IOException {
		String actual="";
		String [] campos;
		BufferedReader metadata;
		try {
			metadata = new BufferedReader(new FileReader(CAMINO));
			//Lectura hasta el final de un archivo
			while((actual = metadata.readLine()) != null) {
				campos = actual.split(" ");
				if (t.getNombreTabla().equals(campos[0])) {
					t.setCampos(campos[1].split(","));
					t.setNumCampos(campos[1].split(",").length);
					String sTexto=campos[4];
					int indices[]=new int[t.getNumCampos()];
					int contador=0;
				    while ( sTexto.indexOf("V")> -1) {
				    	indices[contador] = sTexto.indexOf("V");
				    	sTexto = sTexto.substring(sTexto.indexOf(
				        		  	"V")+"V".length(),sTexto.length());
				        contador++; 
				    }
				    
					t.setNumCamposEncriptados(contador);
					t.setCampoClave(campos[3]);
					t.setLongitudCampos(campos[2].split(","));
					String camposEncriptados[] = new String[contador];
					int j=0;
					for(int i=0;i<campos[4].split(",").length;i++) {
						
						if(campos[4].split(",")[i].equals("V")) {
							camposEncriptados[j]=t.getCampos()[i];
							j++;
						}
					}
					if(t.getNumCamposEncriptados()!=0) {
						
						t.setCamposEncriptados(camposEncriptados);

					}else {
						t.setCamposEncriptados(null);
					}
					return t;
				}
			}
			
			
		} catch (FileNotFoundException e) {
			System.out.println("NO existe el directorio");
			
		}
		return null; 
	}
	public static boolean modificarCamposMetadata(String nombreTabla, String campoAntiguo, String campoNuevo) {
		FileWriter fichero = null;
		PrintWriter pw = null;
        String actual;
        String texto="";
		String [] campos;
		BufferedReader metadata;
		boolean estuvo= false;
        try {
            fichero = new FileWriter("archivos/aux.txt");
            pw = new PrintWriter(fichero);
            metadata= new BufferedReader(new FileReader(CAMINO));
                        
            while((actual = metadata.readLine()) != null) {
            	campos = actual.split(" ");
				if (campos[1].contains(campoAntiguo)&&campos[0].equals(nombreTabla)) {
					texto = actual.replaceAll(campoAntiguo, campoNuevo);
					pw.println(texto);
					estuvo = true;
				}else {
					pw.println(actual);
				}
			}
            borrarFichero(new File(CAMINO));
            File nuevo = new File("archivos/aux.txt");
            nuevo.renameTo(new File(CAMINO));
			

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
		return estuvo;
	}
		
	public static void eliminarNombreMetadata(String nombreTabla) {
		FileWriter fichero = null;
		PrintWriter pw = null;
        String actual;
        String texto="";
		String [] campos;
		BufferedReader metadata;
		 
		try {
            fichero = new FileWriter("archivos/aux.txt");
            pw = new PrintWriter(fichero);
            metadata= new BufferedReader(new FileReader(CAMINO));
                        
            while((actual = metadata.readLine()) != null) {
            	campos = actual.split(" ");
				if (!campos[0].equals(nombreTabla)) {
					pw.println(actual);
				}
			}
            borrarFichero(new File(CAMINO));
            File nuevo = new File("archivos/aux.txt");
            nuevo.renameTo(new File(CAMINO));
			

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
		
	}
	
	public static void escribirMetadata(String nombreTabla, String campos, String clave,
										 String longitudCampos,String camposEncriptados ) {
		FileWriter fichero = null;
        PrintWriter pw = null;
        String texto;
        try
        {
            fichero = new FileWriter(CAMINO, true);
            pw = new PrintWriter(fichero);
            //Coloco el texto que va a ser escrito en el archivo metadatos.txt
            if (camposEncriptados.equals("F")) {
                texto = nombreTabla+" "+campos+" "+longitudCampos+" "+clave+" ";
                for (int i=0;i<campos.split(",").length; i++) {
                	texto = texto+"F,";
                }
            }else {
            	texto = nombreTabla+" "+campos+" "+longitudCampos+" "+clave+" "+camposEncriptados+",";
            }
            System.out.println(texto.substring(0, texto.length()-1));

            
            pw.println(texto.substring(0, texto.length()-1));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
		
	}
	public static void borrarTabla(String nombreTabla) throws IOException {
		if (existe(nombreTabla)) {
			String actual="";
			BufferedReader metadata;
			
			try {
				metadata = new BufferedReader(new FileReader("archivos/"+nombreTabla+".csv"));
				//Lectura hasta el final de un archivo
				metadata.readLine();
				
				if (metadata.readLine() == null) {
					borrarFichero(new File("archivos/"+nombreTabla+".csv"));
					eliminarNombreMetadata(nombreTabla);
					System.out.println("ELiminado con éxito la tabla \""+nombreTabla+"\"");
					
				}else {
					System.out.println("Tabla no vacia, no se puede borrar");
				}
				
				
				
			} catch (FileNotFoundException e) {
				System.out.println("NO existe el directorio");
				
			}
		}else {
			System.out.println("La tabla \""+nombreTabla+"\" no existe");
		}
	
	}
	public static  void borrarFichero(File Ffichero){
	     try {
	         /*Si existe el fichero*/
	         if(Ffichero.exists()){
	           /*Borra el fichero*/  
	           Ffichero.delete(); 
	           //System.out.println("Fichero Borrado con Exito");
	         }
	     } catch (Exception ex) {
	         /*Captura un posible error y le imprime en pantalla*/ 
	          System.out.println(ex.getMessage());
	     }
	} 
	
	
}

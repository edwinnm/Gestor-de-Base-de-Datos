import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import javax.sound.midi.Soundbank;



public class Tabla implements Serializable {
	static String CAMINO ="archivos/";	
	private int numCampos;
	private String [] campos;
	private int numCamposEncriptados;
	private String [] camposEncriptados;
	private String campoClave;
	private String [] longitudCampos;
	private String nombreTabla;
	private int numRegistros;
	private int tamañoRegistro;
	private static File archivo;
	private RandomAccessFile flujo;
	private static  ArrayList<String> listaClaves= new ArrayList<String>();

	static String [] campodeReferencia=null;
	public Tabla(String nombreTabla) throws IOException {
		this.nombreTabla = nombreTabla;
		AnalizarMetadatos.llenarTabladeMetadatos(this);
		archivo =new File("archivos/"+this.getNombreTabla()+".csv");

		
		try {
			tamañoRegistro= calcularTamañoRegisto();
		}catch (Exception e) {
			System.out.println("En el apartado longitud unicamente va numeros");
		}
		
	}
	
	public int calcularTamañoRegisto(){
		int tR=0;
		for (int i=0;i<this.longitudCampos.length;i++) {
			tR+=Integer.parseInt(longitudCampos[i]);
		}
		return tR; 
	}
	public int getTamañoRegistro() {
		return tamañoRegistro;
	}
	
	public String getCampoClave() {
		return campoClave;
	}
	public String[] getCampos() {
		return campos;
	}
	public String getCamposString() {
		String texto="";
		String [] campos= getCampos();
		for(int i=0 ; i<getNumCampos(); i++) {
			texto += campos[i]+",";
		}
		
		return texto.substring(0, texto.length()-1);
	}
	public String[] getCamposEncriptados() {
		return camposEncriptados;
	}
	public String[] getLongitudCampos() {
		return longitudCampos;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public int getNumCampos() {
		return numCampos;
	}
	public int getNumCamposEncriptados() {
		return numCamposEncriptados;
	}
	public int getNumRegistros() {
		return numRegistros;
	}
	public void setCampoClave(String campoClave) {
		this.campoClave = campoClave;
	}
	public void setCampos(String[] campos) {
		this.campos = campos;
	}
	public void setCamposEncriptados(String[] camposEncriptados) {
		this.camposEncriptados = camposEncriptados;
	}
	public static void setArchivo(File archivo) {
		Tabla.archivo = archivo;
	}
	public void setLongitudCampos(String[] longitudCampos) {
		this.longitudCampos = longitudCampos;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
	public void setNumCampos(int numCampos) {
		this.numCampos = numCampos;
	}
	public void setNumCamposEncriptados(int numCamposEncriptados) {
		this.numCamposEncriptados = numCamposEncriptados;
	}
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	public void setFlujo(RandomAccessFile flujo) {
		this.flujo = flujo;
	}
	
	public void crearTabla() {
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(CAMINO+"/"+getNombreTabla()+".csv", true);
            pw = new PrintWriter(fichero);
            pw.println(getCamposString());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
		
	}
	
	//Ingesar un Registro
	public boolean agregarRegistro(String texto) throws ClassNotFoundException, IOException {
		flujo = new RandomAccessFile(archivo, "rw");

		numRegistros=(int)Math.ceil((double)flujo.length()/(double)tamañoRegistro);
		for(int i=0;i<getNumRegistros();i++) {
			flujo.seek(i*tamañoRegistro);
			listaClaves.add(flujo.readLine().split(",")[indiceDeCampo(getCampoClave())]);
		}

			if(texto.length()>tamañoRegistro) {
				System.out.println("El registro es de tamaño mayor al acordado");
				return false;
			}else {
				if(!listaClaves.contains((texto.split(",")[indiceDeCampo(getCampoClave())]))) {
					listaClaves.add(texto.split(",")[indiceDeCampo(getCampoClave())]);
					if(getNumCamposEncriptados()!=0) {
						for(int i=0;i<getNumCamposEncriptados();i++) {
							String nuevo = (texto.split(",")[indiceDeCampo(camposEncriptados[i])]);
							texto = texto.replace(nuevo,Encriptado.encriptar(nuevo));
						}
					}
					
					
					flujo.seek(numRegistros*tamañoRegistro);
					flujo.writeBytes(texto);
					flujo.writeBytes(System.getProperty("line.separator"));
					flujo.close();
					
					numRegistros++;
					System.out.println("Agregado correctamente");
					return true; 
				}else {
					System.out.println("Campo clave repetido, no se agregó");
					
				}
			}
			
		
		return false;
	}
	
	public boolean agregar(String texto) throws ClassNotFoundException, IOException {
		flujo = new RandomAccessFile(archivo, "rw");

		numRegistros=(int)Math.ceil((double)flujo.length()/(double)tamañoRegistro);
		
			if(texto.length()>tamañoRegistro) {
				System.out.println("El registro es de tamaño mayor al acordado");
				return false;
			}else {
				if(getNumCamposEncriptados()!=0) {
					for(int i=0;i<getNumCamposEncriptados();i++) {
						String nuevo = (texto.split(",")[indiceDeCampo(camposEncriptados[i])]);
						texto = texto.replace(nuevo,Encriptado.encriptar(nuevo));
						
					
					
					
					flujo.seek(numRegistros*tamañoRegistro);
					flujo.writeBytes(texto);
					flujo.writeBytes(System.getProperty("line.separator"));
					flujo.close();
					
					numRegistros++;
					System.out.println("Agregado correctamente");
					return true; 
					}
				}else {
					System.out.println("Campo clave repetido, no se agregó");
					
				}
		}
			
		
		return false;
	}
	
	public void agregarCompactado(String texto, RandomAccessFile flujo) throws ClassNotFoundException, IOException {
				
		flujo.seek(numRegistros*tamañoRegistro);
		flujo.writeBytes(texto);
		flujo.writeBytes(System.getProperty("line.separator"));
		numRegistros++;
		System.out.println("Agregado correctamente: "+texto);
		
	}
	public void registosDeArchivo(String pathArchivo) throws IOException, ClassNotFoundException {
		File f = new File(pathArchivo);
		FileReader fr = new FileReader(f);
		String camposArchivos;
		String actual;
		BufferedReader br;
		try {
			br = new BufferedReader(fr);
			//Lectura hasta el final de un archivo
			camposArchivos=br.readLine();
			
			if(camposArchivos.equals(getCamposString())) {
				while((actual = br.readLine()) != null) {
				agregarRegistro(actual);
				}	
			}else {
				System.out.println("Los campos no coinciden");
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("NO existe el directorio");
			
		}
	}
	
	public int indiceDeCampo(String campo) {
		
		for(int i=0;i<getNumCampos();i++) {
			if(campo.equals(getCampos()[i])) {
				return i;
				
			}
		}
		return -1;
	}
	public boolean modificarRegistro(String valorCampoClave, String campoABuscar, String reemplazo)throws IOException {
		String registro="";
		String[]arrayCampos;
		flujo = new RandomAccessFile(archivo, "rw");
		numRegistros=(int)Math.ceil((double)flujo.length()/(double)tamañoRegistro);
		
			
		for(int i=0;i<getNumRegistros();i++) {
			flujo.seek(i*tamañoRegistro);
			registro = flujo.readLine();
			
			arrayCampos=registro.split(",");
			if(arrayCampos[indiceDeCampo(getCampoClave())].equals(valorCampoClave)) {
				System.out.println("Registro antiguo: "+registro);
				registro = registro.replace(arrayCampos[indiceDeCampo(campoABuscar)],reemplazo);
				System.out.println("Registro nuevo: "+registro);
				flujo.seek(i*tamañoRegistro);
				flujo.writeBytes(registro);
				flujo.writeBytes(System.getProperty("line.separator"));
				flujo.close();
				return true;
			}
		}
		
		return false;
	}
	
	public boolean eliminarRegistro(String valorCampoClave) throws IOException {
		String registro;
		String[]arrayCampos;
		flujo = new RandomAccessFile(archivo, "rw");
		numRegistros=(int)Math.ceil((double)flujo.length()/(double)tamañoRegistro);
				
			
		for(int i=0;i<getNumRegistros();i++) {
			flujo.seek(i*tamañoRegistro);
			registro = flujo.readLine();
			
			arrayCampos=registro.split(",");
			if(arrayCampos[indiceDeCampo(getCampoClave())].equals(valorCampoClave)) {
				flujo.seek(i*tamañoRegistro);
				flujo.writeBytes(" ");
				//flujo.writeBytes(System.getProperty("line.separator"));
				flujo.close();
				return true;
			}
		}
		
		return false;
		
	}
	public void modificarEncabezado() throws IOException {
		flujo = new RandomAccessFile(archivo, "rw");
		flujo.seek(0);
		flujo.writeBytes(getCamposString());
		flujo.writeBytes(System.getProperty("line.separator"));
		flujo.close();
	}
	
    public  void compactarArchivo() throws IOException, ClassNotFoundException {
    	 // Abrimos el flujo.
    	flujo = new RandomAccessFile(archivo, "rw");
		numRegistros=(int)Math.ceil((double)flujo.length()/(double)tamañoRegistro);
        String[] listado = new String[numRegistros];
        for(int i=0; i<getNumRegistros(); ++i) {
        	flujo.seek(i*tamañoRegistro);
        	listado[i] = flujo.readLine();
        }
            
        flujo.close();


        File tempo = new File("aux.csv");
                
        flujo = new RandomAccessFile(tempo, "rw");
        setFlujo(flujo);
        setNumRegistros(0);
        for(String s : listado) {
        	System.out.println(s.split(",")[indiceDeCampo(getCampoClave())]);
           if(!s.split(",")[0].equals(" ")) {
                agregarCompactado(s,flujo);
           }
          }
        flujo.close();
        archivo.delete(); // Borramos el archivo.
        tempo.renameTo(archivo);
              
    }
    

    public boolean buscarRegistro(String campo, String valorABuscar,String ordenacion,int nume) throws IOException, Exception {
		String registro="";
        
      
        if(!ordenacion.equals("asc") && !ordenacion.equals("desc")){
        	System.out.println("INGRESAR UNA ORDEANCION VALIDA");
        }
    
        ArrayList<String> todasLasCoincidencias= new ArrayList<String>();
        boolean uno=false;
        boolean esCampoEncriptado = false;
		String[]arrayCampos;
        String aux;
		flujo = new RandomAccessFile(archivo, "rw");
		numRegistros=(int)Math.ceil((double)flujo.length()/(double)tamañoRegistro);
		
		if(getCamposString().contains(campo)) {

			for(int i=0;i<getNumCamposEncriptados();i++) {
				
				if(campo.equals(getCamposEncriptados()[i])) {
					esCampoEncriptado=true;
				}
			
			}
			
			for(int i=0;i<getNumRegistros();i++) {
				flujo.seek(i*tamañoRegistro);
				registro=flujo.readLine();
				arrayCampos=registro.split(",");
                if(esCampoEncriptado) {
                	if(Encriptado.desencriptar(arrayCampos[indiceDeCampo(campo)]).equals(valorABuscar)&&!registro.equals(" ")) {
                		for(int j=0; j<getNumCamposEncriptados();j++){
                			
	                		aux=Encriptado.desencriptar(arrayCampos[indiceDeCampo(getCamposEncriptados()[j])]);
	                		registro = registro.replaceAll(arrayCampos[indiceDeCampo(getCamposEncriptados()[j])], aux);
	                    }  
                		todasLasCoincidencias.add(registro);
                		
                	}
                }else {
                	if(arrayCampos[indiceDeCampo(campo)].equals(valorABuscar)&&!registro.equals(" ")) {
	                	if(getNumCamposEncriptados()!= 0){
		                	for(int j=0; j<getNumCamposEncriptados();j++){
		                		aux=Encriptado.desencriptar(arrayCampos[indiceDeCampo(getCamposEncriptados()[j])]);
		                        registro = registro.replaceAll(arrayCampos[indiceDeCampo(getCamposEncriptados()[j])], aux);
		                    	
		                	} 
		                	todasLasCoincidencias.add(registro);
	                	}                       
	                } 
                }
                

			uno=true;
			}
			
			
			if (ordenacion.equals("desc")) {
				Collections.sort(todasLasCoincidencias);

			} else if (ordenacion.equals("asc")) {
				
				Collections.sort(todasLasCoincidencias, Collections.reverseOrder());
			}
			for (String s : todasLasCoincidencias) {
				System.out.println(s);
				--nume;
				if (nume == 0) {
					break;
				}
			}
                        
		}
		return uno;
		
	}
	public  void unirTabla(Tabla t2, String condicion) throws IOException{

		String []dato=condicion.split("=");
        String actual,actual1;
		String [] campos,campos1;
		RandomAccessFile flujo1;
		campodeReferencia= getCampos();
		File archivo1 = new File("archivos/"+this.getNombreTabla()+".csv");
		flujo1 = new RandomAccessFile(archivo1, "rw");		
		RandomAccessFile flujo2;
		File archivo2 = new File("archivos/"+t2.getNombreTabla()+".csv");
		flujo2 = new RandomAccessFile(archivo2, "rw");				

		ArrayList <String[]>campoTabla1= new ArrayList<String[]>(); 
		ArrayList <String[]>campoTabla2= new ArrayList<String[]>(); 
		int posicion1=0,posicion2=0;
		boolean esCampoEncriptado1=false;
		boolean esCampoEncriptado2=false;
		for(int i=0;i<getNumCamposEncriptados();i++) {
			
			if(dato[0].equals(getCamposEncriptados()[i])) {
				esCampoEncriptado1=true;
			}
		
		}

		for(int i=0;i<t2.getNumCamposEncriptados();i++) {
			
			if(dato[0].equals(t2.getCamposEncriptados()[i])) {
				esCampoEncriptado2=true;
			}
		
		}
		
		boolean estuvo= false;
		if(AnalizarMetadatos.existe(this.getNombreTabla()) && AnalizarMetadatos.existe(t2.getNombreTabla())){
			if(AnalizarMetadatos.existeCampo(this.getNombreTabla(),dato[0])){
				posicion1=posicionDecampo(dato[0]);
				if(AnalizarMetadatos.existeCampo(t2.getNombreTabla(),dato[0])){
					posicion2=posicionDecampo(dato[0]);
					////Se lee el archivo csv con el nombre de la tabla y el campo a coincidir 
		
					flujo1.seek(0);
					flujo2.seek(0);
					int i=0,j=0;
					while((actual = flujo1.readLine()) != null && (actual1=flujo2.readLine())!=null) {
						flujo1.seek(i*tamañoRegistro);
						actual=flujo1.readLine();
						flujo2.seek(j*t2.getTamañoRegistro());
						actual1=flujo2.readLine();
						//System.out.println(actual);
		            	//System.out.println(actual1);
		            	campos = actual.split(",");
		            	campos1= actual1.split(",");
		            	if(esCampoEncriptado1) {
		            		if(Encriptado.desencriptar(campos[posicion1]).equals((dato[1].replaceAll("\"","")))){
			            		campoTabla1.add(campos);///Almacena los arreglos coincidientes con la condicion que se encuentren en la tabla 1
		            		}
		            	}else if(campos[posicion1].equals((dato[1].replaceAll("\"","")))){
			            	campoTabla1.add(campos);///Almacena los arreglos coincidientes con la condicion que se encuentren en la tabla 1
		            	}
		            	if(esCampoEncriptado2) {
		            		if(Encriptado.desencriptar(campos[posicion2]).equals((dato[1].replaceAll("\"","")))){
			            		campoTabla2.add(campos);///Almacena los arreglos coincidientes con la condicion que se encuentren en la tabla 1
		            		}
		            	}else if(campos1[posicion2].equals((dato[1].replaceAll("\"","")))){
		            		campoTabla2.add(campos1);///Almacena los arreglos coincidientes con la condicion que se encuentren en la tabla 2
		            	}
		            	i++;
		            	j++;
		            }
				}else{
					System.out.println("El campo: "+dato[0]+" no existe en la tabla: "+t2.getNombreTabla());
				}
				
			}else{
				System.out.println("El campo: "+dato[0]+" no existe en la tabla: "+this.getNombreTabla());
			}
			
		}else{
			System.out.println("Una de las tablas no existe...");
		}
		int i=0;
		///Verifica si las tablas tubieron coincidencias
		if(campoTabla1.size()==0 || campoTabla2.size()==0){
			System.out.println("No existen coincidencias...");
		}else{
			////Imprime los elementos de acuerdo al orden de coincidiencias de aparicion
			System.out.println("\tUnion de Tablas");
			while((i<campoTabla1.size() && i<campoTabla2.size())){
				
				mostrar1(campoTabla1.get(i), this);
				mostrar2(campoTabla2.get(i), t2, posicion2);
				System.out.println("");
				++i;
			}
		}
		
	}
	///Mostrar un vector de String
	public static void mostrar1(String[]a, Tabla t) throws IOException{
		String[]campos= t.getCampos();
		
		int posEncriptados[]= new int[t.getNumCamposEncriptados()];
		
		int j=0;
		
		for(int i=0;i<t.getNumCampos();i++) {
					
			if(j<t.getNumCamposEncriptados() && campos[i].equals(t.getCamposEncriptados()[j])) {
				posEncriptados[j]=i;
				j++;
			}
		}
		j=0;
		for(int i=0;i<a.length;i++) {
			
			if(j<t.getNumCamposEncriptados() && posEncriptados[j]==i) {
				a[i]= Encriptado.desencriptar(a[i]);
				j++;
			}
		}
		
		for(int i=0;i<a.length;i++) {
			System.out.print(a[i]+" ");
		}
		
	}
	public static void mostrar2(String[]a, Tabla t, int noQuiero) throws IOException{
		String[]campos= t.getCampos();
		
		int posEncriptados[]= new int[t.getNumCamposEncriptados()];
		
		int j=0;
		
		for(int i=0;i<t.getNumCampos();i++) {
					
			if(j<t.getNumCamposEncriptados() && campos[i].equals(t.getCamposEncriptados()[j])) {
				posEncriptados[j]=i;
				j++;
			}
		}
		j=0;
		for(int i=0;i<a.length;i++) {
			
			if(j<t.getNumCamposEncriptados() && posEncriptados[j]==i) {
				a[i]= Encriptado.desencriptar(a[i]);
				j++;
			}
		}
		
		for(int i=0;i<a.length;i++) {
			if(i!=noQuiero) {
				System.out.print(a[i]+" ");	
			}
			
		}
		
	}
	///// Retorna la posicion donde se encuentra el campo por el cual se va a buscar la coincidencia
	public static int posicionDecampo(String nombreCampo){
		int i=0;
		///cambiar en vez de a por campodeReferencia
		
		while(i<campodeReferencia.length && ! campodeReferencia[i].equals(nombreCampo)){
			++i;
		}
		
		return i;
	}
	
}

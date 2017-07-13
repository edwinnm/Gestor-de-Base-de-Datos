import java.io.IOException;

public class Encriptado {

	private final static char caracter_rellenador = '~';
	private final static int clave = 2;
	
	public static String encriptar(String textoOriginal) throws IOException{
		int filas = 0;
		String textoCifrado="";
		int columnas = clave;
		int index = 0;
			
		if(textoOriginal.length()%clave==0){
			filas =(textoOriginal.length()/clave);
		}else{
			filas = (textoOriginal.length()/clave)+1;
		}
			
		char matriz [][] = new char [filas][columnas];
		char aux [] = new char[filas] ;
			
		for(int i = 0 ; i<filas ; i++){
			for(int j = 0 ; j<columnas ; j++){
				if (index >= textoOriginal.length()){
					matriz[i][j] = caracter_rellenador;
				}else{
					matriz[i][j]= textoOriginal.charAt(index);
				}
				index++;
			}
		}
		for(int i = 0 ; i<columnas ; i++){
			for(int j = 0 ; j<filas ; j++){
				textoCifrado= textoCifrado+String.valueOf(matriz[j][i]);
			}
		}
		return textoCifrado;
					
	}
		
	public static String desencriptar(String texto) throws IOException{
			int filas = 0;
			int columnas = clave;
			int index = 0;
			String textoDescifrado="";
			int posicion, contador =0;
			
			posicion= texto.indexOf(caracter_rellenador);
			
			while(posicion !=-1){
				contador++;
				posicion= texto.indexOf(caracter_rellenador,posicion+1);
			}
			
			int tamanioOriginal = texto.length()-contador;
			
			if(tamanioOriginal%clave==0){
				filas =(tamanioOriginal/clave);
			}else{
				filas = (tamanioOriginal/clave)+1;
			}
			
			char matriz [][] = new char [filas][columnas];
			char aux [] = new char[filas] ;
			
			for(int i = 0 ; i<columnas ; i++){
				for(int j = 0 ; j<filas ; j++){
					matriz[j][i]= texto.charAt(index);
					index++;
				}
			}
			for(int i = 0 ; i<filas ; i++){
				for(int j = 0 ; j<columnas ; j++){
					if(matriz[i][j]!=caracter_rellenador){
						textoDescifrado= textoDescifrado+String.valueOf(matriz[i][j]);
					}
				}
				
			}
			
			return textoDescifrado;
		}
	
}
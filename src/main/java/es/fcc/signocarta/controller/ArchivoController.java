package es.fcc.signocarta.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controlador responsable de gestionar la descarga de archivos por parte del
 * usuario.
 */
@Controller
public class ArchivoController {
	/**
	 * Ruta base donde se almacenan los archivos subidos por los usuarios. Esta ruta
	 * es relativa al directorio de ejecución de la aplicación.
	 */
	private final String RUTA_ARCHIVOS = "archivosUsuarios"; // Carpeta externa, relativa

	/**
	 * Endpoint para descargar un archivo específico por su nombre.
	 * 
	 * @param nombreArchivo nombre del archivo a descargar (incluye extensión)
	 * @return una respuesta HTTP con el archivo como recurso adjunto, o un error si
	 *         no se encuentra o no se puede acceder
	 */
	@GetMapping("/descargar/{nombreArchivo:.+}")
	public ResponseEntity<Resource> descargarArchivo(@PathVariable String nombreArchivo) {
		try {
			// Construye la ruta del archivo, asegurando que esté normalizada para evitar
			// errores de path
			Path archivoPath = Paths.get(RUTA_ARCHIVOS).resolve(nombreArchivo).normalize();
			Resource recurso = new UrlResource(archivoPath.toUri());

			if (!recurso.exists()) {
				return ResponseEntity.notFound().build();
			}
			// Si el archivo existe, se prepara la respuesta con cabeceras para forzar
			// descarga
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
					.body(recurso);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}
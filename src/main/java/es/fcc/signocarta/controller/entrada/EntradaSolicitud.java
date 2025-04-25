package es.fcc.signocarta.controller.entrada;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class EntradaSolicitud implements Serializable{

	
	private static final long serialVersionUID = -1633306282112429404L;
	private MultipartFile archivo;
	private String comentario;

	public MultipartFile getArchivo() {
		return archivo;
	}

	public void setArchivo(MultipartFile archivo) {
		this.archivo = archivo;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

}

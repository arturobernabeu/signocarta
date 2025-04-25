package es.fcc.signocarta.controller.entrada;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

public class EntradaAcceso implements Serializable{

	
	private static final long serialVersionUID = 2334998921669523981L;
	@NotBlank(message = "Introduzca datos de acceso válido")
	private String nombreAcceso;
	private String password;
	public String getNombreAcceso() {
		return nombreAcceso;
	}
	public void setNombreAcceso(String nombreAcceso) {
		this.nombreAcceso = nombreAcceso;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	

}

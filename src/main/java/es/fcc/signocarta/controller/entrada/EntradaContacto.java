package es.fcc.signocarta.controller.entrada;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EntradaContacto implements Serializable{


	private static final long serialVersionUID = -4402855777453785794L;
	@NotBlank(message = "Introduzca un nombre")
	private String nombre;
	@NotBlank(message = "Introduzca un email")
	@Email(message = "Introduzca un email válido")
	private String email;
	@NotBlank(message = "Introduzca un motivo")
	private String motivo;
	@NotBlank(message = "Introduzca un mensaje")
	private String mensaje;
	
	public EntradaContacto(@NotBlank(message = "Introduzca un nombre") String nombre,
			@NotBlank(message = "Introduzca un email") @Email(message = "Introduzca un email válido") String email,
			@NotBlank(message = "Introduzca un motivo") String motivo,
			@NotBlank(message = "Introduzca un mensaje") String mensaje) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.motivo = motivo;
		this.mensaje = mensaje;
	}
	
	public EntradaContacto() {
		super();
	}


	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	

}

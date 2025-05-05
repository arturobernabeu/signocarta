package es.fcc.signocarta.controller.entrada;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EntradaRegistro implements Serializable {

	private static final long serialVersionUID = 3988505317614640842L;
	@NotBlank(message = "Introduzca un nombre")
	private String nombre;
	@NotBlank(message = "Introduzca un apellido")
	private String apellidos;
	@NotBlank(message = "Introduzca un email")
	@Email(message = "Introduzca un email válido")
	private String email;
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{6,}$", message = "La contraseña debe tener al menos 6 caracteres, una mayúscula y un número")
	private String password;
	
	private String passwordRepeat;
	
	public EntradaRegistro(@NotBlank(message = "Introduzca un nombre") String nombre,
			@NotBlank(message = "Introduzca un apellido") String apellidos,
			@NotBlank(message = "Introduzca un email") @Email(message = "Introduzca un email válido") String email,
			@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{6,}$", message = "La contraseña debe tener al menos 6 caracteres, una mayúscula y un número") String password,
			String passwordRepeat) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.password = password;
		this.passwordRepeat = passwordRepeat;
	}

	public EntradaRegistro() {
		super();
	}



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRepeat() {
		return passwordRepeat;
	}

	public void setPasswordRepeat(String passwordRepeat) {
		this.passwordRepeat = passwordRepeat;
	}

}

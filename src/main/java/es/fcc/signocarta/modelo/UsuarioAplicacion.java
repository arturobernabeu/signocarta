package es.fcc.signocarta.modelo;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class UsuarioAplicacion extends Usuario {

	private String email;
	@OneToMany(mappedBy = "usuarioApp", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Solicitud> listaSolicitudes;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Solicitud> getListaSolicitudes() {
		return listaSolicitudes;
	}

	public void setListaSolicitudes(List<Solicitud> listaSolicitudes) {
		this.listaSolicitudes = listaSolicitudes;
	}

}

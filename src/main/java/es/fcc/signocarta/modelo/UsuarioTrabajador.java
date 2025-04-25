package es.fcc.signocarta.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class UsuarioTrabajador extends Usuario {

	@Column(name = "cod_trabajador")
	private String codTrabajador;
	private String puesto;

	public String getCodTrabajador() {
		return codTrabajador;
	}

	public void setCodTrabajador(String codTrabajador) {
		this.codTrabajador = codTrabajador;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

}

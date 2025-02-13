package es.fcc.signocarta.modelo;

import jakarta.persistence.Entity;

@Entity
public class UsuarioTrabajador extends Usuario {
	private String cod_trabajador;
	private String puesto;

	public String getCod_trabajador() {
		return cod_trabajador;
	}

	public void setCod_trabajador(String cod_trabajador) {
		this.cod_trabajador = cod_trabajador;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

}

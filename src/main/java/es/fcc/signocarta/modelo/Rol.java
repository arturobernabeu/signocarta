package es.fcc.signocarta.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Rol {
	@Id
	private Integer id;
	private String nombre;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}

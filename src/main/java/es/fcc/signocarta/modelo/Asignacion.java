package es.fcc.signocarta.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Asignacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_solicitud", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_solicitud_asignacion"))
	private Solicitud solicitud;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_trabajador", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuarioTrabajador_asignacion"))
	private UsuarioTrabajador usuarioTrabajador;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Solicitud getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	public UsuarioTrabajador getUsuarioTrabajador() {
		return usuarioTrabajador;
	}

	public void setUsuarioTrabajador(UsuarioTrabajador usuarioTrabajador) {
		this.usuarioTrabajador = usuarioTrabajador;
	}

}

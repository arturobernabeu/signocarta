package es.fcc.signocarta.modelo;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Historico {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String evento;
	@ManyToOne
	@JoinColumn(name = "id_solicitud", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_solicitud_historico"))
	private Solicitud solicitud;
	@ManyToOne
	@JoinColumn(name = "id_trabajador", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuarioTrabajador_historico"))
	private UsuarioTrabajador usuarioTrabajador;
	@ManyToOne
	@JoinColumn(name = "id_estado", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_estado_historico"))
	private Estado estado;
	private Date fechaRegistro;
	
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEvento() {
		return evento;
	}
	public void setEvento(String evento) {
		this.evento = evento;
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
	public Estado getEstado() {
		return estado;
	}
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	

}

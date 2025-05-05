package es.fcc.signocarta.controller.salida;

import java.io.Serializable;
import java.util.Date;

import es.fcc.signocarta.modelo.Documento;
import es.fcc.signocarta.modelo.Presupuesto;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.modelo.UsuarioTrabajador;


public class SalidaHistoricoAdm implements Serializable{

	private static final long serialVersionUID = -9039316838724934934L;
	private Integer id;
	private Date fecha;
	private Documento docEntrada;
	private String estado;
	private Presupuesto presupuesto;
	private String observaciones;
	private UsuarioTrabajador usuarioTrabajador;
	private UsuarioAplicacion usuarioAplicacion;
	private Documento docSalida;
	private String evento;
	
	
	public String getEvento() {
		return evento;
	}
	public void setEvento(String evento) {
		this.evento = evento;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Documento getDocEntrada() {
		return docEntrada;
	}
	public void setDocEntrada(Documento docEntrada) {
		this.docEntrada = docEntrada;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public UsuarioTrabajador getUsuarioTrabajador() {
		return usuarioTrabajador;
	}
	public void setUsuarioTrabajador(UsuarioTrabajador usuarioTrabajador) {
		this.usuarioTrabajador = usuarioTrabajador;
	}
	
	public UsuarioAplicacion getUsuarioAplicacion() {
		return usuarioAplicacion;
	}
	public void setUsuarioAplicacion(UsuarioAplicacion usuarioAplicacion) {
		this.usuarioAplicacion = usuarioAplicacion;
	}
	public Documento getDocSalida() {
		return docSalida;
	}
	public void setDocSalida(Documento docSalida) {
		this.docSalida = docSalida;
	}
	public Presupuesto getPresupuesto() {
		return presupuesto;
	}
	public void setPresupuesto(Presupuesto presupuesto) {
		this.presupuesto = presupuesto;
	}
	
	

}

package es.fcc.signocarta.controller.salida;

import java.io.Serializable;
import java.util.Date;


import es.fcc.signocarta.modelo.Documento;
import es.fcc.signocarta.modelo.Presupuesto;

public class SalidaHistorico implements Serializable{


	private static final long serialVersionUID = 4264899222515925793L;
	private Integer id;
	private Date fecha;
	private Documento docEntrada;
	private String estado;
	private Presupuesto presupuesto;
	private Documento docSalida;
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
	public Presupuesto getPresupuesto() {
		return presupuesto;
	}
	public void setPresupuesto(Presupuesto presupuesto) {
		this.presupuesto = presupuesto;
	}
	public Documento getDocSalida() {
		return docSalida;
	}
	public void setDocSalida(Documento docSalida) {
		this.docSalida = docSalida;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	


}

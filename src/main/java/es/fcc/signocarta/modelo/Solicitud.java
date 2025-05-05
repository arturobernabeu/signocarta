package es.fcc.signocarta.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Solicitud {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;	
	private String observaciones;
	@ManyToOne
	@JoinColumn(name = "id_estado",referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_estado_solicitud"))
	private Estado estado;	
	@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Documento> listaDocumentos = new ArrayList<Documento>();
	private Date fechaRegistro;
	@ManyToOne	
	@JoinColumn(name = "id_usuarioApp", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuarioApp_solicitud"))
	private UsuarioAplicacion usuarioApp;
	@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Historico> historicos = new ArrayList<>();
	@OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL, orphanRemoval = true)
	private Presupuesto presupuesto;

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

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public List<Documento> getListaDocumentos() {
		return listaDocumentos;
	}

	public void setListaDocumentos(List<Documento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}

	public UsuarioAplicacion getUsuarioApp() {
		return usuarioApp;
	}

	public void setUsuarioApp(UsuarioAplicacion usuarioApp) {
		this.usuarioApp = usuarioApp;
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
	}
	public Presupuesto getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(Presupuesto presupuesto) {
		this.presupuesto = presupuesto;
	}
	

}

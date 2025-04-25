package es.fcc.signocarta.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.fcc.signocarta.controller.entrada.EntradaRegistro;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.modelo.UsuarioTrabajador;
import es.fcc.signocarta.repository.RolRepository;
import es.fcc.signocarta.repository.TrabajadorRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;
import es.fcc.signocarta.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final TrabajadorRepository trabajadorRepository;
	private final UsuarioAppRepository usuarioAppRepository;
	private final UsuarioRepository usuarioRepository;
	private final RolRepository rolRepository;

	public UsuarioService(TrabajadorRepository trabajadorRepository, UsuarioAppRepository usuarioAppRepository, RolRepository rolRepository, UsuarioRepository usuarioRepository) {
		super();
		this.trabajadorRepository = trabajadorRepository;
		this.usuarioAppRepository = usuarioAppRepository;
		this.rolRepository = rolRepository;
		this.usuarioRepository = usuarioRepository;
		
	}

	// método para obtener un usuarioTrabajados
	public Optional<UsuarioTrabajador> obtenerUsuarioTrabajador(String codTrabajador) {

		return trabajadorRepository.findByCodTrabajador(codTrabajador);
	}

	// método para obtener un usuarioAplicacion
	public Optional<UsuarioAplicacion> obtenerUsuarioAplicacion(String email) {

		return usuarioAppRepository.findByEmail(email);
	}

	// hacer método para comprobar si es un usuario trabajador

	public boolean isTrabajador(String datoEntrada) {
		return trabajadorRepository.existsByCodTrabajador(datoEntrada);
	}
	
	// método para comprobar si existe el email en la base de datos
	public boolean existeEmailUsuario(String email) {
		if (usuarioAppRepository.existsByEmail(email)) {
			return true;
		} else {
			return false;
		}
	}
	public UsuarioAplicacion guardar(EntradaRegistro solicitudRegistro) {

		UsuarioAplicacion usuario = new UsuarioAplicacion();
		usuario.setNombre(solicitudRegistro.getNombre());
		usuario.setApellidos(solicitudRegistro.getApellidos());
		usuario.setEmail(solicitudRegistro.getEmail());
		usuario.setPassword(solicitudRegistro.getPassword());
		usuario.setRol(rolRepository.getReferenceById(3));

		return usuarioAppRepository.save(usuario);
	}

	public Optional<UsuarioAplicacion> buscarUsuarioAppPorId(Long usuarioId) {		
		return usuarioAppRepository.findById(usuarioId.intValue());
	}
	// buscar Usuario por id
	public Optional<Usuario> buscarPorId(Long usuarioId) {		
		return usuarioRepository.findById(usuarioId.intValue());
		
	}
	

}

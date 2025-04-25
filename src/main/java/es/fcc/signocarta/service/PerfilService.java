package es.fcc.signocarta.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.fcc.signocarta.controller.entrada.EntradaRegistro;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.modelo.UsuarioTrabajador;
import es.fcc.signocarta.repository.TrabajadorRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;
import es.fcc.signocarta.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class PerfilService {

	@Autowired
	private final UsuarioAppRepository usuarioAppRepository;
	private final UsuarioRepository usuarioRepository;
	private final TrabajadorRepository trabajadorRepository;

	public PerfilService(UsuarioAppRepository usuarioAppRepository, UsuarioRepository usuarioRepository,
			TrabajadorRepository trabajadorRepository) {
		super();
		this.usuarioAppRepository = usuarioAppRepository;
		this.usuarioRepository = usuarioRepository;
		this.trabajadorRepository = trabajadorRepository;
	}

	public EntradaRegistro cargarDatosPerfil(Usuario usuario) {

		EntradaRegistro datos = new EntradaRegistro();
		datos.setNombre(usuario.getNombre());
		datos.setApellidos(usuario.getApellidos());
		if (usuario.getRol().getId() == 3) { // controlamos que para cambiar email sea un usuario de la aplicacion
			datos.setEmail(usuarioAppRepository.findById(usuario.getId()).get().getEmail());
		}
		return datos;
	}

	@Transactional
	public void actualizarDatos(EntradaRegistro datos, Long id) {

		Optional<Usuario> optionalUsuario = usuarioRepository.findById(id.intValue());// almacenamos el usuario que nos
																						// está llegando por parámetro
		// con este if nos aseguramos primero que hay algo dentro de optionalUsuario
		if (optionalUsuario.isPresent()) {
			// controlamos si es un usuario aplicacion para grabar en la tabla que le
			// corresponde
			if (optionalUsuario.get().getRol().getId() == 3) {

				Optional<UsuarioAplicacion> optionalUsuarioApp = usuarioAppRepository.findById(id.intValue());
				UsuarioAplicacion usuarioApp = optionalUsuarioApp.get();

				usuarioApp.setNombre(datos.getNombre());
				usuarioApp.setApellidos(datos.getApellidos());
				usuarioApp.setEmail(datos.getEmail());
				// comprobamos si ha modificado la contraseña
				if (datos.getPassword() == datos.getPasswordRepeat() & datos.getPassword() != null
						& datos.getPassword() != "") {
					usuarioApp.setPassword(datos.getPassword());
				}

			} else { // en el caso que se otro tipo de rol
				Optional<UsuarioTrabajador> optionalUsuarioTrab = trabajadorRepository.findById(id.intValue());
				UsuarioTrabajador usuarioTrabajador = optionalUsuarioTrab.get();

				usuarioTrabajador.setNombre(datos.getNombre());
				usuarioTrabajador.setApellidos(datos.getApellidos());
				// comprobamos si ha modificado la contraseña
				if (datos.getPassword() == datos.getPasswordRepeat() & datos.getPassword() != null
						& datos.getPassword() != "") {
					usuarioTrabajador.setPassword(datos.getPassword());
				}

			}

		} else {
			throw new RuntimeException("Usuario no encontrado");
		}

	}

}

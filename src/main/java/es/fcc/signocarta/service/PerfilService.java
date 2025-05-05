package es.fcc.signocarta.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.fcc.signocarta.controller.entrada.EntradaRegistro;
import es.fcc.signocarta.modelo.Usuario;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.modelo.UsuarioTrabajador;
import es.fcc.signocarta.repository.TrabajadorRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;
import es.fcc.signocarta.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

/**
 * Servicio encargado de la gestión del perfil de usuario. Permite cargar y
 * actualizar los datos del perfil, diferenciando entre usuarios de aplicación y
 * trabajadores según el rol del usuario.
 */
@Service
public class PerfilService {


	private final UsuarioAppRepository usuarioAppRepository;
	private final UsuarioRepository usuarioRepository;
	private final TrabajadorRepository trabajadorRepository;

	/**
	 * Constructor que inyecta los repositorios necesarios.
	 *
	 * @param usuarioAppRepository Repositorio de usuarios de aplicación.
	 * @param usuarioRepository    Repositorio general de usuarios.
	 * @param trabajadorRepository Repositorio de trabajadores.
	 */
	public PerfilService(UsuarioAppRepository usuarioAppRepository, UsuarioRepository usuarioRepository,
			TrabajadorRepository trabajadorRepository) {
		super();
		this.usuarioAppRepository = usuarioAppRepository;
		this.usuarioRepository = usuarioRepository;
		this.trabajadorRepository = trabajadorRepository;
	}

	/**
	 * Carga los datos actuales del perfil del usuario para ser visualizados y/o
	 * editados.
	 *
	 * @param usuario El objeto {@link Usuario} actualmente logado.
	 * @return Un objeto {@link EntradaRegistro} con los datos del usuario para
	 *         mostrar en el formulario.
	 */
	public EntradaRegistro cargarDatosPerfil(Usuario usuario) {

		EntradaRegistro datos = new EntradaRegistro();
		datos.setNombre(usuario.getNombre());
		datos.setApellidos(usuario.getApellidos());
		 if (usuario.getRol() != null && usuario.getRol().getId() == 3) { // controlamos que para cambiar email sea un usuario de la aplicacion
		        UsuarioAplicacion usuarioApp = usuarioAppRepository.findById(usuario.getId())
		            .orElseThrow(() -> new IllegalArgumentException("UsuarioAplicacion no encontrado con ID: " + usuario.getId()));
		        datos.setEmail(usuarioApp.getEmail());
		    }
		return datos;
	}

	/**
	 * Actualiza los datos del perfil del usuario en función de su tipo (aplicación
	 * o trabajador). Si el usuario modifica la contraseña y ambas coinciden,
	 * también se actualiza.
	 *
	 * @param datos Datos del formulario de entrada (nombre, apellidos, email,
	 *              contraseña, etc.).
	 * @param id    ID del usuario que está actualizando su perfil.
	 * @throws RuntimeException Si el usuario no existe en la base de datos.
	 */
	@Transactional
	public void actualizarDatos(EntradaRegistro datos, Long id) {
		// almacenamos el usuario que nos está llegando por parámetro
		Optional<Usuario> optionalUsuario = usuarioRepository.findById(id.intValue());
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
				if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
					usuarioApp.setPassword(datos.getPassword());
				}
			} else { // en el caso que sea otro tipo de rol
				Optional<UsuarioTrabajador> optionalUsuarioTrab = trabajadorRepository.findById(id.intValue());
				UsuarioTrabajador usuarioTrabajador = optionalUsuarioTrab.get();

				usuarioTrabajador.setNombre(datos.getNombre());
				usuarioTrabajador.setApellidos(datos.getApellidos());
				// comprobamos si ha modificado la contraseña
				if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
					usuarioTrabajador.setPassword(datos.getPassword());
				}
			}
		} else {
			throw new RuntimeException("Usuario no encontrado");
		}
	}
}

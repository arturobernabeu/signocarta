package es.fcc.signocarta.service;

import java.util.List;
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

/**
 * Servicio encargado de la gestión de usuarios en la aplicación. Incluye
 * operaciones para obtener, guardar, y verificar usuarios, tanto trabajadores
 * como usuarios de la aplicación, así como la gestión de roles.
 */
@Service
public class UsuarioService {

	private final TrabajadorRepository trabajadorRepository;
	private final UsuarioAppRepository usuarioAppRepository;
	private final UsuarioRepository usuarioRepository;
	private final RolRepository rolRepository;

	public UsuarioService(TrabajadorRepository trabajadorRepository, UsuarioAppRepository usuarioAppRepository,
			RolRepository rolRepository, UsuarioRepository usuarioRepository) {
		super();
		this.trabajadorRepository = trabajadorRepository;
		this.usuarioAppRepository = usuarioAppRepository;
		this.rolRepository = rolRepository;
		this.usuarioRepository = usuarioRepository;

	}

	/**
	 * Obtiene un usuario trabajador a partir del código de trabajador.
	 *
	 * @param codTrabajador Código único del trabajador.
	 * @return Un objeto `Optional` que puede contener el `UsuarioTrabajador`
	 *         correspondiente o estar vacío.
	 */
	public Optional<UsuarioTrabajador> obtenerUsuarioTrabajador(String codTrabajador) {

		return trabajadorRepository.findByCodTrabajador(codTrabajador);
	}

	/**
	 * Obtiene un usuario de la aplicación por su email.
	 *
	 * @param email Email del usuario en la aplicación.
	 * @return Un objeto `Optional` que puede contener el `UsuarioAplicacion`
	 *         correspondiente o estar vacío.
	 */
	public Optional<UsuarioAplicacion> obtenerUsuarioAplicacion(String email) {

		return usuarioAppRepository.findByEmail(email);
	}

	/**
	 * Verifica si un dato de entrada corresponde a un usuario trabajador existente.
	 *
	 * @param datoEntrada Código del trabajador o identificador.
	 * @return `true` si el dato de entrada corresponde a un trabajador; `false` en
	 *         caso contrario.
	 */

	public boolean isTrabajador(String datoEntrada) {
		return trabajadorRepository.existsByCodTrabajador(datoEntrada);
	}

	/**
	 * Verifica si existe un usuario con el email proporcionado.
	 *
	 * @param email Email del usuario.
	 * @return `true` si el email existe en la base de datos; `false` en caso
	 *         contrario.
	 */
	public boolean existeEmailUsuario(String email) {
		if (usuarioAppRepository.existsByEmail(email)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Guarda un nuevo usuario de la aplicación basado en los datos de registro
	 * proporcionados.
	 *
	 * @param solicitudRegistro Objeto con los datos de registro del nuevo usuario.
	 * @return El objeto `UsuarioAplicacion` recién creado.
	 */
	public UsuarioAplicacion guardar(EntradaRegistro solicitudRegistro) {

		UsuarioAplicacion usuario = new UsuarioAplicacion();
		usuario.setNombre(solicitudRegistro.getNombre());
		usuario.setApellidos(solicitudRegistro.getApellidos());
		usuario.setEmail(solicitudRegistro.getEmail());
		usuario.setPassword(solicitudRegistro.getPassword());
		usuario.setRol(rolRepository.getReferenceById(3));

		return usuarioAppRepository.save(usuario);
	}

	/**
	 * Busca un usuario de la aplicación por su ID.
	 *
	 * @param usuarioId ID del usuario de la aplicación.
	 * @return Un objeto `Optional` que puede contener el `UsuarioAplicacion`
	 *         correspondiente o estar vacío.
	 */
	public Optional<UsuarioAplicacion> buscarUsuarioAppPorId(Long usuarioId) {
		return usuarioAppRepository.findById(usuarioId.intValue());
	}

	/**
	 * Busca un usuario genérico por su ID.
	 *
	 * @param usuarioId ID del usuario.
	 * @return Un objeto `Optional` que puede contener el `Usuario` correspondiente
	 *         o estar vacío.
	 */
	public Optional<Usuario> buscarPorId(Long usuarioId) {
		return usuarioRepository.findById(usuarioId.intValue());

	}

	/**
	 * Obtiene una lista de todos los trabajadores.
	 *
	 * @return Lista de todos los objetos `UsuarioTrabajador`.
	 */
	public List<UsuarioTrabajador> listaTrabajadores() {
		List<UsuarioTrabajador> lista = trabajadorRepository.findAll();
		return lista;
	}

}

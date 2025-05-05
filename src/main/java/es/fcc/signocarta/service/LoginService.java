package es.fcc.signocarta.service;

import org.springframework.stereotype.Service;

import es.fcc.signocarta.repository.TrabajadorRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;

/**
 * Servicio encargado de la lógica de autenticación de usuarios y trabajadores.
 * Proporciona métodos para verificar si una entrada corresponde a un trabajador
 * y si las credenciales (usuario y contraseña) son válidas.
 */
@Service
public class LoginService {

	private final UsuarioAppRepository usuarioRepository;
	private final TrabajadorRepository trabajadorRepository;

	/**
	 * Constructor que inyecta las dependencias de los repositorios.
	 *
	 * @param usuarioRepository    Repositorio de usuarios de la aplicación.
	 * @param trabajadorRepository Repositorio de trabajadores.
	 */
	public LoginService(UsuarioAppRepository usuarioRepository, TrabajadorRepository trabajadorRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.trabajadorRepository = trabajadorRepository;
	}


	/**
	 * Verifica si la contraseña ingresada es correcta para el usuario o trabajador
	 * proporcionado. Primero comprueba si el usuario es un usuario de aplicación
	 * por su email. Si no es así, intenta verificar si es un trabajador por su
	 * código.
	 *
	 * @param usuario  Email o código del trabajador.
	 * @param password Contraseña ingresada.
	 * @return {@code true} si las credenciales son correctas, de lo contrario
	 *         {@code false}.
	 */
	public boolean passwordOK(String usuario, String password) {

		if (usuarioRepository.existsByEmail(usuario)) {
			// comprobamos si la contraseña indicada es igual a la que aparece en la base de
			// datos
			return usuarioRepository.findByEmail(usuario).map(usuarioApp -> usuarioApp.getPassword().equals(password))
					.orElse(false); // false si no encuentra el usuario

		} else {
			// comprobamos si la contraseña indicada es igual a la que aparece en la base de
			// datos
			return trabajadorRepository.findByCodTrabajador(usuario)
					.map(usuarioTrabajador -> usuarioTrabajador.getPassword().equals(password)).orElse(false);
			// false si no encuentra el usuario
		}
	}

}

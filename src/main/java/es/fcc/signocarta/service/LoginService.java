package es.fcc.signocarta.service;

import org.springframework.stereotype.Service;


import es.fcc.signocarta.repository.TrabajadorRepository;
import es.fcc.signocarta.repository.UsuarioAppRepository;

@Service
public class LoginService {

	private final UsuarioAppRepository usuarioRepository;
	private final TrabajadorRepository trabajadorRepository;

	public LoginService(UsuarioAppRepository usuarioRepository, TrabajadorRepository trabajadorRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.trabajadorRepository = trabajadorRepository;
	}

	// hacer método para comprobar si es un usuario trabajador

	public boolean isTrabajador(String datoEntrada) {
		return trabajadorRepository.existsByCodTrabajador(datoEntrada);
	}

	// método para comprobar que la contraseña es correcta para el usuario
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

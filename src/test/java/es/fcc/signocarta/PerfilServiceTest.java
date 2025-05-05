package es.fcc.signocarta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.fcc.signocarta.controller.entrada.EntradaRegistro;
import es.fcc.signocarta.modelo.Rol;
import es.fcc.signocarta.modelo.UsuarioAplicacion;
import es.fcc.signocarta.repository.UsuarioAppRepository;
import es.fcc.signocarta.service.PerfilService;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private UsuarioAppRepository usuarioAppRepository;

    @InjectMocks
    private PerfilService perfilService;

    @Test
    void testCargarDatosPerfil() {
        // Crear objeto mockeado
        UsuarioAplicacion usuarioApp = new UsuarioAplicacion();
        usuarioApp.setId(1);
        usuarioApp.setNombre("Luis");
        usuarioApp.setApellidos("Ramirez");

        Rol rol = new Rol();
        rol.setId(3);
        usuarioApp.setRol(rol);
        usuarioApp.setEmail("luis@app.com");

        // Configurar mock
        when(usuarioAppRepository.findById(1)).thenReturn(Optional.of(usuarioApp));

        // Ejecutar método
        EntradaRegistro datos = perfilService.cargarDatosPerfil(usuarioApp);

        // Verificar
        assertEquals("Luis", datos.getNombre());
        assertEquals("luis@app.com", datos.getEmail());
    }
}

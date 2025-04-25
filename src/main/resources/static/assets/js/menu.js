fetch("menu.html")
    .then((response) => response.text())
    .then((data) => {
        document.getElementById("menu-container").innerHTML = data;

        // Ahora que el menú ya está cargado, ejecutar la función para activar el enlace correcto
        marcarPaginaActiva();
    })
    .catch((error) => console.error("Error al cargar el menú:", error));

function marcarPaginaActiva() {
    let currentPage = window.location.pathname.split("/").pop(); // Obtener el archivo actual
    let menuItems = document.querySelectorAll("#menu-container .sidebar-item");

    menuItems.forEach((item) => {
        let link = item.querySelector("a");

        if (link.getAttribute("href") === currentPage) {
            item.classList.add("active"); // Marcar como activo
        } else {
            item.classList.remove("active");
        }
    });
}

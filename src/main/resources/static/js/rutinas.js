/**
 * Script de funcionalidades para el Sistema de Estacionamiento
 */

document.addEventListener('DOMContentLoaded', function() {
    // Configurar el cierre automático de las alertas después de 5 segundos
    setTimeout(function() {
        const alertas = document.querySelectorAll('.alert');
        alertas.forEach(function(alerta) {
            // Crear un nuevo evento de click
            const closeEvent = new Event('click');
            // Obtener el botón de cierre
            const closeButton = alerta.querySelector('.close');
            // Disparar el evento solo si existe el botón
            if (closeButton) {
                closeButton.dispatchEvent(closeEvent);
            }
        });
    }, 5000);

    // Activar tooltips de Bootstrap
    if (typeof $().tooltip === 'function') {
        $('[data-toggle="tooltip"]').tooltip();
    }

    // Activar popovers de Bootstrap
    if (typeof $().popover === 'function') {
        $('[data-toggle="popover"]').popover();
    }

    // Función para confirmar eliminación
    window.confirmarEliminacion = function(url, mensaje) {
        if (confirm(mensaje || '¿Está seguro de que desea eliminar este registro?')) {
            window.location.href = url;
        }
    };

    // Mejorar la experiencia de usuario en los formularios
    const forms = document.querySelectorAll('form');
    forms.forEach(function(form) {
        // Agregar clase de validación para Bootstrap
        form.classList.add('needs-validation');
        form.setAttribute('novalidate', '');

        // Evento de envío de formulario
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Manejar formato de fechas en inputs
    const fechaInputs = document.querySelectorAll('input[type="date"]');
    fechaInputs.forEach(function(input) {
        if (!input.value && input.getAttribute('data-default-today') === 'true') {
            const hoy = new Date();
            const año = hoy.getFullYear();
            const mes = String(hoy.getMonth() + 1).padStart(2, '0');
            const dia = String(hoy.getDate()).padStart(2, '0');
            input.value = `${año}-${mes}-${dia}`;
        }
    });

    // Agregar funcionalidad para búsqueda en tablas
    const searchInputs = document.querySelectorAll('.table-search');
    searchInputs.forEach(function(input) {
        input.addEventListener('keyup', function() {
            const searchValue = this.value.toLowerCase();
            const tableId = this.getAttribute('data-table');
            const table = document.getElementById(tableId);
            
            if (table) {
                const rows = table.querySelectorAll('tbody tr');
                
                rows.forEach(function(row) {
                    let found = false;
                    const cells = row.querySelectorAll('td');
                    
                    cells.forEach(function(cell) {
                        if (cell.textContent.toLowerCase().indexOf(searchValue) > -1) {
                            found = true;
                        }
                    });
                    
                    if (found) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                });
            }
        });
    });

    // Funcionalidad para el módulo de estacionamiento
    inicializarModuloEstacionamiento();
});

/**
 * Inicializa las funcionalidades específicas del módulo de estacionamiento
 */
function inicializarModuloEstacionamiento() {
    // Manejar selección de espacios de estacionamiento
    const espacios = document.querySelectorAll('.espacio-item');
    espacios.forEach(function(espacio) {
        espacio.addEventListener('click', function() {
            const idEspacio = this.getAttribute('data-id');
            const estado = this.getAttribute('data-estado');
            
            if (estado === 'DISPONIBLE') {
                // Redirigir a la página para crear ticket con este espacio
                window.location.href = `/tickets/nuevo?idEspacio=${idEspacio}`;
            } else if (estado === 'OCUPADO') {
                // Buscar el ticket asociado a este espacio
                fetch(`/api/tickets/espacio/${idEspacio}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data && data.idTicket) {
                            window.location.href = `/tickets/editar/${data.idTicket}`;
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    });

    // Actualizar contador de tiempo para tickets activos
    const tiempoContadores = document.querySelectorAll('.tiempo-contador');
    if (tiempoContadores.length > 0) {
        setInterval(function() {
            tiempoContadores.forEach(function(contador) {
                const fechaEntrada = new Date(contador.getAttribute('data-entrada'));
                const ahora = new Date();
                const diferencia = ahora - fechaEntrada;
                
                const horas = Math.floor(diferencia / (1000 * 60 * 60));
                const minutos = Math.floor((diferencia % (1000 * 60 * 60)) / (1000 * 60));
                
                contador.textContent = `${horas}h ${minutos}m`;
            });
        }, 60000); // Actualizar cada minuto
    }

    // Calcular tarifa automáticamente al seleccionar tipo
    const tarifaSelect = document.getElementById('tipoTarifa');
    if (tarifaSelect) {
        tarifaSelect.addEventListener('change', function() {
            const idTarifa = this.value;
            if (idTarifa) {
                fetch(`/api/tarifas/${idTarifa}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data && data.precio) {
                            const montoInput = document.getElementById('montoTotal');
                            if (montoInput) {
                                montoInput.value = data.precio;
                            }
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    }

    // Buscar vehículo por placa
    const placaInput = document.getElementById('buscarPlaca');
    if (placaInput) {
        placaInput.addEventListener('keyup', function(event) {
            if (event.key === 'Enter') {
                const placa = this.value.trim();
                if (placa) {
                    window.location.href = `/vehiculos/buscar-placa?numeroPlaca=${placa}`;
                }
            }
        });
    }

    // Inicializar vista de mapa de estacionamiento si existe
    inicializarMapaEstacionamiento();
}

/**
 * Inicializa el mapa visual del estacionamiento
 */
function inicializarMapaEstacionamiento() {
    const mapaContainer = document.getElementById('mapa-estacionamiento');
    if (!mapaContainer) return;

    // Si hay datos de espacios disponibles como atributo data
    const espaciosData = mapaContainer.getAttribute('data-espacios');
    if (espaciosData) {
        try {
            const espacios = JSON.parse(espaciosData);
            renderizarMapaEstacionamiento(mapaContainer, espacios);
        } catch (e) {
            console.error('Error al procesar datos de espacios:', e);
        }
    } else {
        // Si no hay datos, cargar desde la API
        const nivelId = mapaContainer.getAttribute('data-nivel-id');
        if (nivelId) {
            fetch(`/api/espacios/nivel/${nivelId}`)
                .then(response => response.json())
                .then(data => {
                    renderizarMapaEstacionamiento(mapaContainer, data);
                })
                .catch(error => console.error('Error al cargar espacios:', error));
        }
    }
}

/**
 * Renderiza el mapa visual del estacionamiento
 */
function renderizarMapaEstacionamiento(container, espacios) {
    // Limpiar el contenedor
    container.innerHTML = '';
    
    // Crear un grid para el mapa
    const mapa = document.createElement('div');
    mapa.className = 'mapa-grid';
    
    // Determinar el número máximo de filas y columnas
    let maxFila = 0;
    let maxColumna = 0;
    
    espacios.forEach(function(espacio) {
        const ubicacion = espacio.ubicacion.split('-');
        if (ubicacion.length === 2) {
            const fila = parseInt(ubicacion[0]);
            const columna = parseInt(ubicacion[1]);
            
            maxFila = Math.max(maxFila, fila);
            maxColumna = Math.max(maxColumna, columna);
        }
    });
    
    // Crear el grid con el tamaño correcto
    mapa.style.gridTemplateRows = `repeat(${maxFila + 1}, 60px)`;
    mapa.style.gridTemplateColumns = `repeat(${maxColumna + 1}, 80px)`;
    
    // Añadir los espacios al grid
    espacios.forEach(function(espacio) {
        const espacioDiv = document.createElement('div');
        espacioDiv.className = `espacio-item estado-${espacio.estado.toLowerCase()}`;
        espacioDiv.setAttribute('data-id', espacio.idEspacio);
        espacioDiv.setAttribute('data-estado', espacio.estado);
        
        const ubicacion = espacio.ubicacion.split('-');
        if (ubicacion.length === 2) {
            const fila = parseInt(ubicacion[0]);
            const columna = parseInt(ubicacion[1]);
            
            espacioDiv.style.gridRow = fila + 1;
            espacioDiv.style.gridColumn = columna + 1;
        }
        
        const numero = document.createElement('div');
        numero.className = 'espacio-numero';
        numero.textContent = espacio.numeroEspacio;
        
        const estado = document.createElement('div');
        estado.className = 'espacio-estado';
        estado.textContent = espacio.estado;
        
        espacioDiv.appendChild(numero);
        espacioDiv.appendChild(estado);
        mapa.appendChild(espacioDiv);
    });
    
    container.appendChild(mapa);
    
    // Asignar eventos de clic
    document.querySelectorAll('.espacio-item').forEach(function(espacioItem) {
        espacioItem.addEventListener('click', function() {
            const idEspacio = this.getAttribute('data-id');
            const estado = this.getAttribute('data-estado');
            
            if (estado.toUpperCase() === 'DISPONIBLE') {
                if (confirm('¿Desea crear un ticket para este espacio?')) {
                    window.location.href = `/tickets/nuevo?idEspacio=${idEspacio}`;
                }
            } else if (estado.toUpperCase() === 'OCUPADO') {
                if (confirm('Este espacio está ocupado. ¿Desea ver el ticket asociado?')) {
                    fetch(`/api/tickets/espacio/${idEspacio}`)
                        .then(response => response.json())
                        .then(data => {
                            if (data && data.idTicket) {
                                window.location.href = `/tickets/editar/${data.idTicket}`;
                            } else {
                                alert('No se encontró el ticket asociado a este espacio.');
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert('Error al consultar el ticket.');
                        });
                }
            }
        });
    });
}
package dataAccess;

import domain.Usuario;
import domain.Actividad;
import domain.Encargado;
import domain.Sala;
import domain.Sesion;
import domain.Reserva;
import domain.Factura;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import domain.Socio;
import exceptions.SocioAlreadyExistException;

public class DataAccess {
    private EntityManager db;
    private EntityManagerFactory emf;

    ConfigXML c = ConfigXML.getInstance();

    public DataAccess() {
        // Inicializa la base de datos si no existe o si está vacía
        String fileName = c.getDbFilename();
        open();
        try {
            // Comprobar si la base de datos está vacía (sin socios, encargados, salas o
            // actividades)
            long numSocios = (long) db.createQuery("SELECT COUNT(s) FROM Socio s").getSingleResult();
            long numEncargados = (long) db.createQuery("SELECT COUNT(e) FROM Encargado e").getSingleResult();
            long numSalas = (long) db.createQuery("SELECT COUNT(s) FROM Sala s").getSingleResult();
            long numActividades = (long) db.createQuery("SELECT COUNT(a) FROM Actividad a").getSingleResult();
            if (numSocios == 0 && numEncargados == 0 && numSalas == 0 && numActividades == 0) {
                // Si la base de datos está vacía, se fuerza la inicialización
                initializeDB();
                System.out.println("DataAccess: Base de datos creada e inicializada (forzado SIEMPRE)");
                System.out.println("[DEBUG] Ruta real de la base de datos: " + fileName);
            }
        } catch (Exception ex) {
            System.err.println("[ERROR] Error comprobando si la base de datos está vacía: " + ex.getMessage());
            // Si hay error, forzamos inicialización
            initializeDB();
            System.out.println("DataAccess: Base de datos creada e inicializada (forzado SIEMPRE)");
            System.out.println("[DEBUG] Ruta real de la base de datos: " + fileName);
        }
    }

    public void open() {

        String fileName = c.getDbFilename();
        if (c.isDatabaseLocal()) {
            emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
            db = emf.createEntityManager();
        } else {
            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.user", c.getUser());
            properties.put("javax.persistence.jdbc.password", c.getPassword());

            emf = Persistence.createEntityManagerFactory(
                "objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + c.getDbFilename(), properties);
            db = emf.createEntityManager();
        }
        System.out.println("DataAccess opened => isDatabaseLocal: " + c.isDatabaseLocal());

    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
            System.out.println("DataAccess closed");
        }
    }

    public DataAccess(EntityManager db) {
        this.db = db;
    }

    public void initializeDB() {

        db.getTransaction().begin();

        try {

            Calendar today = Calendar.getInstance();

            int month = today.get(Calendar.MONTH);
            if (month == 12) {
                month = 1;
            }

            // Crear socios
            System.out.println("[DEBUG] Creando socio de prueba...");
            Socio so1 = new Socio("11111111", "Socio 1", "socio1@gmail.com", "Socio1", 11111111);
            so1.setMaximoReservas(3);
            db.persist(so1);
            System.out.println("[DEBUG] Socio creado y persistido"); // Crear Encargados
            System.out.println("[DEBUG] Creando encargados de prueba...");
            Encargado e1 = new Encargado("11111111", "Encargado 1", "encargado1@gmail.com", "Encargado1");
            db.persist(e1);
            // Crear un encargado de ejemplo
            Encargado e2 = new Encargado("22222222", "Encargado 2", "encargado2@gmail.com", "Encargado2");
            db.persist(e2);
            System.out.println("[DEBUG] Encargados creados y persistidos");

            // Crear Salas
            System.out.println("[DEBUG] Creando salas de prueba...");
            Sala sa1 = new Sala("Sala 1", 20, false);
            Sala sa2 = new Sala("Sala 2", 20, true);
            Sala sa3 = new Sala("Sala 3", 25, false);
            Sala sa4 = new Sala("Sala 4", 30, true);
            Sala sa5 = new Sala("Sala 5", 15, false);
            db.persist(sa1);
            db.persist(sa2);
            db.persist(sa3);
            db.persist(sa4);
            db.persist(sa5);
            System.out.println("[DEBUG] Salas creadas y persistidas"); // Crear Actividades
            System.out.println("[DEBUG] Creando actividades de prueba...");
            Actividad a1 = new Actividad("Zumba", 2, 6);
            Actividad a2 = new Actividad("Yoga", 2, 5);
            Actividad a3 = new Actividad("Aerobic", 3, 5);
            Actividad a4 = new Actividad("Pilates", 2, 5);
            Actividad a5 = new Actividad("Spinning", 3, 7);
            Actividad a6 = new Actividad("Boxeo", 4, 8);
            Actividad a7 = new Actividad("CrossFit", 5, 9);
            Actividad a8 = new Actividad("BodyPump", 3, 7);
            Actividad a9 = new Actividad("TRX", 4, 8);
            db.persist(a1);
            db.persist(a2);
            db.persist(a3);
            db.persist(a4);
            db.persist(a5);
            db.persist(a6);
            db.persist(a7);
            db.persist(a8);
            db.persist(a9);
            System.out.println("[DEBUG] Actividades creadas y persistidas"); // Crear sesiones y asociarlas
                                                                             // correctamente
            LocalDate hoy = LocalDate.now();
            LocalDate lunes = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);

            a1 = db.merge(a1);
            a2 = db.merge(a2);
            a3 = db.merge(a3);
            sa1 = db.merge(sa1);
            sa2 = db.merge(sa2);

            for (int i = 0; i < 5; i++) {
                LocalDate fechaSesion = lunes.plusDays(i);
                Sesion sesionSemana = new Sesion(a1, sa1, fechaSesion, LocalTime.of(10, 0), LocalTime.of(11, 0));
                a1.addSesion(sesionSemana);
                sa1.getSesiones().add(sesionSemana);
                db.persist(sesionSemana);
            }
            for (int i = 0; i < 3; i++) {
                LocalDate fechaSesion = lunes.plusDays(i);
                Sesion sesionSemana = new Sesion(a2, sa2, fechaSesion, LocalTime.of(17, 0), LocalTime.of(18, 0));
                a2.addSesion(sesionSemana);
                sa2.getSesiones().add(sesionSemana);
                db.persist(sesionSemana);
            }
            for (int i = 0; i < 2; i++) {
                LocalDate fechaSesion = lunes.plusDays(i);
                Sesion sesionSemana = new Sesion(a3, sa1, fechaSesion, LocalTime.of(9, 0), LocalTime.of(10, 30));
                a3.addSesion(sesionSemana);
                sa1.getSesiones().add(sesionSemana);
                db.persist(sesionSemana);
            }

            System.out.println("[DEBUG] Sesiones creadas y persistidas");

            db.getTransaction().commit();
            System.out.println("Db initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socio registrarSocio(String dni, String nombre, String mail, String contraseña, int numeroTarjeta)
            throws SocioAlreadyExistException {
        System.out.println("[DEBUG] registrarSocio llamado");
        Socio nuevoSocio = null;
        try {
            if (db == null || !db.isOpen()) {
                System.out.println("[DEBUG] Abriendo EntityManager en registrarSocio");
                open();
            }
            // Comprobar si ya existe un socio con el mismo mail (NO por mail+contraseña)
            System.out.println("[DEBUG] Comprobando si existe socio con mail: " + mail);
            TypedQuery<Socio> query = db.createQuery(
                    "SELECT s FROM Socio s WHERE s.mail = :mail", Socio.class);
            query.setParameter("mail", mail);

            List<Socio> existentes = query.getResultList();
            System.out.println("[DEBUG] Socios existentes encontrados: " + existentes.size());
            if (!existentes.isEmpty()) {
                System.err.println("[ERROR] Ya existe un socio con ese mail.");
                throw new SocioAlreadyExistException("Ya existe un socio con ese mail.");
            }

            // Crear y guardar nuevo socio
            nuevoSocio = new Socio(dni, nombre, mail, contraseña, numeroTarjeta);
            System.out.println("[DEBUG] Iniciando transacción para persistir nuevo socio");
            db.getTransaction().begin();
            db.persist(nuevoSocio);
            db.getTransaction().commit();
            System.out.println("[DEBUG] Socio registrado correctamente");
        } catch (SocioAlreadyExistException e) {
            System.err.println("[ERROR] " + e.getMessage());
            throw e; // se propaga al llamador
        } catch (Exception e) {
            System.err.println("[ERROR] Error inesperado al registrar socio: " + e.getMessage());
            e.printStackTrace();
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
        } finally {
            System.out.println("[DEBUG] Cerrando EntityManager en registrarSocio");
            close();
        }
        return nuevoSocio;
    }

    public Usuario iniciarSesion(String correo, String contraseña) {
        try {
            if (db == null || !db.isOpen()) {
                open();
            }
            // Buscar si es un socio
            TypedQuery<Socio> querySocio = db.createQuery(
                    "SELECT s FROM Socio s WHERE s.mail = :correo AND s.contraseña = :contraseña", Socio.class);
            querySocio.setParameter("correo", correo);
            querySocio.setParameter("contraseña", contraseña);

            List<Socio> resultadosSocio = querySocio.getResultList();
            if (!resultadosSocio.isEmpty()) {
                return resultadosSocio.get(0); // Devuelve el Socio autenticado
            }

            // Buscar si es un encargado
            TypedQuery<Encargado> queryEncargado = db.createQuery(
                    "SELECT e FROM Encargado e WHERE e.mail = :correo AND e.contraseña = :contraseña", Encargado.class);
            queryEncargado.setParameter("correo", correo);
            queryEncargado.setParameter("contraseña", contraseña);

            List<Encargado> resultadosEncargado = queryEncargado.getResultList();
            if (!resultadosEncargado.isEmpty()) {
                return resultadosEncargado.get(0); // Devuelve el Encargado autenticado
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no encontró ni socio ni encargado
    }

    // Método para obtener todas las actividades.
    public List<Actividad> getActividades() {
        if (db == null || !db.isOpen()) {
            open();
        }
        List<Actividad> actividades = new ArrayList<>();
        try {
            TypedQuery<Actividad> query = db.createQuery("SELECT a FROM Actividad a", Actividad.class);
            actividades = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actividades;
    }

    // Método para obtener las salas, independientemente de si están libres o no
    public List<Sala> getTodasLasSalas() {
        if (db == null || !db.isOpen()) {
            open();
        }
        List<Sala> salas = new ArrayList<>();
        try {
            salas = db.createQuery("SELECT s FROM Sala s", Sala.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salas;
    }

    public void crearSesion(Actividad actividad, Sala sala, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        System.out.println("[DEBUG] crearSesion llamado con:");
        System.out.println("  Actividad: " + (actividad != null ? actividad.getNombreActividad() : "null"));
        System.out.println("  Sala: " + (sala != null ? sala.getNombreSala() : "null"));
        System.out.println("  Fecha: " + fecha);
        System.out.println("  Hora inicio: " + horaInicio);
        System.out.println("  Hora fin: " + horaFin);

        if (actividad == null) throw new IllegalArgumentException("Actividad es null");
        if (sala == null) throw new IllegalArgumentException("Sala es null");
        if (fecha == null) throw new IllegalArgumentException("Fecha es null");
        if (horaInicio == null) throw new IllegalArgumentException("Hora inicio es null");
        if (horaFin == null) throw new IllegalArgumentException("Hora fin es null");

        try {
            actividad = db.merge(actividad);
            sala = db.merge(sala);

            Sesion sesion = new Sesion(actividad, sala, fecha, horaInicio, horaFin);

            // Añadir la sesión a la actividad (relación bidireccional)
            actividad.addSesion(sesion);

            // Persistir la sesión
            db.getTransaction().begin();
            db.persist(sesion);
            db.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("[ERROR] Excepción en crearSesion: " + e.getMessage());
            e.printStackTrace();
            if (db.getTransaction().isActive()) db.getTransaction().rollback();
            throw e;
        }
    }

    public List<Sesion> getSesionesDeActividad(Actividad actividad) {
        if (db == null || !db.isOpen()) {
            open();
        }
        List<Sesion> sesiones = new ArrayList<>();
        try {
            TypedQuery<Sesion> query = db.createQuery(
                    "SELECT s FROM Sesion s WHERE s.actividad = :actividad", Sesion.class);
            query.setParameter("actividad", actividad);
            sesiones = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sesiones;
    }

    public List<Reserva> getReservasDeSocio(Socio socio) {
        if (db == null || !db.isOpen()) {
            open();
        }
        List<Reserva> reservas = new ArrayList<>();
        try {
            String jpql = "SELECT r FROM Reserva r WHERE r.socio.dni = :dniParam";
            TypedQuery<Reserva> query = db.createQuery(jpql, Reserva.class);
            query.setParameter("dniParam", socio.getDni());
            List<Reserva> todas = query.getResultList();
            for (Reserva r : todas) {
                // Solo mostrar reservas que NO estén asociadas a una factura enviada (pagada)
                String jpqlFactura = "SELECT f FROM Factura f WHERE f.socio.dni = :dniParam AND f.enviada = true";
                TypedQuery<Factura> qf = db.createQuery(jpqlFactura, Factura.class);
                qf.setParameter("dniParam", socio.getDni());
                List<Factura> facturas = qf.getResultList();
                boolean pagada = false;
                for (Factura f : facturas) {
                    if (f.getReservas() != null && f.getReservas().contains(r)) {
                        pagada = true;
                        break;
                    }
                }
                if (!pagada) reservas.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservas;
    }

    public List<Sesion> getSesionesPorFiltros(String nombreActividad, Integer gradoExigencia) {
        // Asegurarse de que el EntityManager está abierto
        if (db == null || !db.isOpen()) {
            open();
        }
        List<Sesion> sesionesFiltradas = new ArrayList<>();

        try {
            StringBuilder jpql = new StringBuilder("SELECT s FROM Sesion s JOIN FETCH s.sala WHERE 1=1 ");
            if (nombreActividad != null && !nombreActividad.isEmpty()) {
                jpql.append("AND s.actividad.nombreActividad = :nombreActividad ");
            }
            if (gradoExigencia != null) {
                jpql.append("AND s.actividad.gradoExigencia = :gradoExigencia ");
            }

            TypedQuery<Sesion> query = db.createQuery(jpql.toString(), Sesion.class);

            if (nombreActividad != null && !nombreActividad.isEmpty()) {
                query.setParameter("nombreActividad", nombreActividad);
            }
            if (gradoExigencia != null) {
                query.setParameter("gradoExigencia", gradoExigencia);
            }

            sesionesFiltradas = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sesionesFiltradas;
    }

    public String getTipoUsuario(String correo) {
        // Primero se verifica si es un Socio
        TypedQuery<Socio> querySocio = db.createQuery("SELECT s FROM Socio s WHERE s.mail = :correo", Socio.class);
        querySocio.setParameter("correo", correo);
        List<Socio> socios = querySocio.getResultList();

        // Si es un Socio, se retorna "socio"
        if (!socios.isEmpty()) {
            return "Socio";
        }

        // Si no es Socio, se verifica si es un Encargado
        TypedQuery<Encargado> queryEncargado = db.createQuery("SELECT e FROM Encargado e WHERE e.mail = :correo",
                Encargado.class);
        queryEncargado.setParameter("correo", correo);
        List<Encargado> encargados = queryEncargado.getResultList();

        // Si es un Encargado, se retorna "encargado"
        if (!encargados.isEmpty()) {
            return "Encargado";
        }

        // Si no es ni Socio ni Encargado, se retorna "desconocido"
        return "Desconocido";
    }

    public void reservarSesion(Socio socio, Sesion sesion) throws Exception {
        // Debug: Log de los objetos recibidos
        System.out.println("[DEBUG] reservarSesion - Socio recibido: " + socio);
        System.out.println("[DEBUG] reservarSesion - Sesion recibida: " + sesion);

        // Validar que los parámetros no sean null
        if (socio == null) {
            throw new Exception("El socio no puede ser null.");
        }
        if (sesion == null) {
            throw new Exception("La sesión no puede ser null.");
        } // Debug: Log de los IDs
        System.out.println("[DEBUG] reservarSesion - DNI del socio: '" + socio.getDni() + "'");
        System.out.println("[DEBUG] reservarSesion - ID de la sesion: '" + sesion.getId() + "'");

        // Recuperar los objetos actuales de la base de datos
        String socioId = socio.getDni();
        String sesionId = sesion.getId();

        // Validar que los IDs no sean null
        if (socioId == null || socioId.trim().isEmpty()) {
            throw new Exception("El DNI del socio no puede ser null o vacío.");
        }
        if (sesionId == null || sesionId.trim().isEmpty()) {
            throw new Exception("El ID de la sesión no puede ser null o vacío.");
        }

        // Recuperar el socio actual de la base de datos
        Socio socioActual = db.find(Socio.class, socioId);
        if (socioActual == null) {
            throw new Exception("Socio no encontrado en la base de datos con DNI: " + socioId);
        }

        // Recuperar la sesión actual de la base de datos
        Sesion sesionActual = db.find(Sesion.class, sesionId);
        if (sesionActual == null) {
            throw new Exception("Sesión no encontrada en la base de datos con ID: " + sesionId);
        }

        // Comprobar máximo de reservas
        if (socioActual.getReservas().size() >= socioActual.getMaximoReservas()) {
            throw new Exception("Has alcanzado el máximo de reservas permitidas.");
        }
        // Comprobar si el socio ya tiene una reserva para esta sesión
        for (Reserva r : socioActual.getReservas()) {
            if (r.getSesion() != null && r.getSesion().getId().equals(sesionActual.getId())) {
                throw new Exception("Ya tienes una reserva para esta sesión.");
            }
        }
        // Comprobar si hay plazas disponibles en la sala para la sesión
        Sala sala = sesionActual.getSala();
        System.out.println("[DEBUG] Sala asociada: " + sala);
        if (sala == null) {
            throw new Exception("La sala de la sesión no está definida.");
        }
        System.out.println("[DEBUG] Aforo máximo de la sala: " + sala.getAforoMaximo());
        System.out.println("[DEBUG] Plazas ocupadas en la sesión: " + sesionActual.getPlazasOcupadas());
        if (sesionActual.getPlazasOcupadas() >= sala.getAforoMaximo()) {
            throw new Exception("No hay plazas disponibles en la sala para esta sesión.");
        }
        // Generar idReserva único
        String idReserva;
        boolean idUnico = false;
        do {
            idReserva = java.util.UUID.randomUUID().toString();
            javax.persistence.Query q = db.createQuery("SELECT COUNT(r) FROM Reserva r WHERE r.idReserva = :id");
            q.setParameter("id", idReserva);
            long count = (Long) q.getSingleResult();
            if (count == 0)
                idUnico = true;
        } while (!idUnico);
        db.getTransaction().begin();
        try {
            // Los objetos ya están gestionados por el EntityManager al haberlos recuperado
            // con find()

            // Crear la reserva y actualizar aforo
            Reserva reserva = new Reserva(idReserva, new java.util.Date(), true);
            reserva.setSesion(sesionActual);
            reserva.setSocio(socioActual);
            socioActual.addReserva(reserva);
            sesionActual.addReserva(reserva);
            // Incrementar el contador de reservas activas
            socioActual.setNumReservas(socioActual.getNumReservas() + 1);
            int nuevasPlazasOcupadas = sesionActual.getPlazasOcupadas() + 1;
            sesionActual.setPlazasOcupadas(nuevasPlazasOcupadas); // Suma 1 a plazas ocupadas

            // Persistir la reserva (ahora todos los objetos referenciados están
            // gestionados)
            db.persist(reserva);

            // Crear la factura
            double precio = sesionActual.getActividad().getPrecio();
            String codigoFactura = java.util.UUID.randomUUID().toString();
            java.util.Date fechaFactura = new java.util.Date();
            domain.Factura factura = new domain.Factura(precio, codigoFactura, fechaFactura);
            factura.setEnviada(false); // Siempre pendiente al crear
            factura.setSocio(socioActual); // Asocia la factura al socio
            factura.addReserva(reserva);
            socioActual.addFactura(factura);
            db.persist(factura);

            db.getTransaction().commit();
        } catch (Exception e) {
            if (db.getTransaction().isActive())
                db.getTransaction().rollback();
            throw e;
        }
    }

    // Método para pagar una factura
    public void pagarFactura(domain.Factura factura) throws Exception {
        if (db == null || !db.isOpen()) {
            open();
        }
        db.getTransaction().begin();
        try {
            System.out.println("[DEBUG] Simulando cobro de factura: " + factura.getCodigoFactura() + ", importe: "
                    + factura.getPrecio());
            Factura facturaGestionada = db.merge(factura); 
            Socio socio = facturaGestionada.getSocio();
            if (socio != null) {
                socio = db.merge(socio);
                // Eliminar todas las reservas asociadas a la factura
                if (facturaGestionada.getReservas() != null) {
                    for (Reserva reserva : new ArrayList<>(facturaGestionada.getReservas())) {
                        reserva = db.merge(reserva);
                        socio.removeReserva(reserva);
                        db.remove(reserva);
                        int numReservasActual = socio.getNumReservas();
                        if (numReservasActual > 0) {
                            socio.setNumReservas(numReservasActual - 1);
                        }
                    }
                    db.merge(socio);
                }
                socio.removeFactura(facturaGestionada);
                db.merge(socio);
            }
            db.remove(facturaGestionada);
            db.getTransaction().commit();
            System.out.println("[DEBUG] Factura pagada y eliminada correctamente");
        } catch (Exception e) {
            if (db.getTransaction().isActive())
                db.getTransaction().rollback();
            System.out.println("[DEBUG] Error al pagar factura: " + e.getMessage());
            throw e;
        }
    }

    public List<Factura> getFacturasDeSocio(Socio socio) {
        if (db == null || !db.isOpen()) {
            open();
        }
        List<Factura> facturas = new ArrayList<>();
        try {
            String jpql = "SELECT f FROM Factura f WHERE f.socio.dni = :dniParam AND f.enviada = true";
            TypedQuery<Factura> query = db.createQuery(jpql, Factura.class);
            query.setParameter("dniParam", socio.getDni());
            facturas = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facturas;
    }

    public Actividad crearActividad(String nombre, int nivelExigencia)
            throws exceptions.ActividadAlreadyExistException {
        if (db == null || !db.isOpen())
            open();
        try {
            TypedQuery<Actividad> query = db.createQuery(
                    "SELECT a FROM Actividad a WHERE a.nombreActividad = :nombre AND a.gradoExigencia = :nivel",
                    Actividad.class);
            query.setParameter("nombre", nombre);
            query.setParameter("nivel", nivelExigencia);
            java.util.List<Actividad> existentes = query.getResultList();
            if (!existentes.isEmpty()) {
                throw new exceptions.ActividadAlreadyExistException(
                        "Ya existe una actividad con ese nombre y nivel de exigencia.");
            }
            db.getTransaction().begin();
            Actividad nueva = new Actividad(nombre, nivelExigencia, 0);
            db.persist(nueva);
            db.getTransaction().commit();
            return nueva;
        } catch (exceptions.ActividadAlreadyExistException e) {
            if (db.getTransaction().isActive())
                db.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            if (db.getTransaction().isActive())
                db.getTransaction().rollback();
            System.err.println("[ERROR][crearActividad] " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            String msg = e.getMessage();
            if (msg == null || msg.isEmpty())
                msg = e.getClass().getName();
            throw new RuntimeException("Error al crear la actividad: " + msg, e);
        }
    }

    public Actividad crearActividad(String nombre, int nivelExigencia, double precio)
            throws exceptions.ActividadAlreadyExistException {
        if (db == null || !db.isOpen())
            open();
        try {
            TypedQuery<Actividad> query = db.createQuery(
                    "SELECT a FROM Actividad a WHERE a.nombreActividad = :nombre AND a.gradoExigencia = :nivel",
                    Actividad.class);
            query.setParameter("nombre", nombre);
            query.setParameter("nivel", nivelExigencia);
            java.util.List<Actividad> existentes = query.getResultList();
            if (!existentes.isEmpty()) {
                throw new exceptions.ActividadAlreadyExistException(
                        "Ya existe una actividad con ese nombre y nivel de exigencia.");
            }
            db.getTransaction().begin();
            Actividad nueva = new Actividad(nombre, nivelExigencia, precio);
            db.persist(nueva);
            db.getTransaction().commit();
            return nueva;
        } catch (exceptions.ActividadAlreadyExistException e) {
            if (db.getTransaction().isActive())
                db.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            if (db.getTransaction().isActive())
                db.getTransaction().rollback();
            System.err.println("[ERROR][crearActividad] " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            String msg = e.getMessage();
            if (msg == null || msg.isEmpty())
                msg = e.getClass().getName();
            throw new RuntimeException("Error al crear la actividad: " + msg, e);
        }
    }

    public List<Socio> getSocios() {
        if (db == null || !db.isOpen())
            open();
        try {
            TypedQuery<Socio> query = db.createQuery("SELECT s FROM Socio s", Socio.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Factura> mandarFacturas(Socio socio) {
        if (db == null || !db.isOpen())
            open();
        List<Factura> facturasPendientes = new ArrayList<>();
        try {
            TypedQuery<Factura> query = db.createQuery(
                    "SELECT f FROM Factura f WHERE f.socio.dni = :dniParam AND f.enviada = false", Factura.class);
            query.setParameter("dniParam", socio.getDni());
            facturasPendientes = query.getResultList();
            // Marcar como enviadas
            db.getTransaction().begin();
            for (Factura f : facturasPendientes) {
                f.setEnviada(true);
                db.merge(f);
            }
            db.getTransaction().commit();
        } catch (Exception e) {
            if (db.getTransaction().isActive())
                db.getTransaction().rollback();
            e.printStackTrace();
        }
        return facturasPendientes;
    }
    // Método para obtener las sesiones de una sala en una fecha específica
    public List<Sesion> getSesionesDeSala(Sala sala, LocalDate fecha) {
        if (db == null || !db.isOpen()) {
            open();
        }
        List<Sesion> sesiones = new ArrayList<>();
        try {
            TypedQuery<Sesion> query = db.createQuery(
                    "SELECT s FROM Sesion s WHERE s.sala = :sala AND s.fechaImparticion = :fecha", Sesion.class);
            query.setParameter("sala", sala);
            query.setParameter("fecha", fecha);
            sesiones = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sesiones;
    }

    public List<Factura> getFacturasPendientesDeSocio(Socio socio) {
        if (db == null || !db.isOpen())
            open();
        List<Factura> facturasPendientes = new ArrayList<>();
        try {
            TypedQuery<Factura> query = db.createQuery(
                    "SELECT f FROM Factura f WHERE f.socio.dni = :dniParam AND f.enviada = false", Factura.class);
            query.setParameter("dniParam", socio.getDni());
            facturasPendientes = query.getResultList();
            System.out.println("[DEBUG] Facturas pendientes de enviar para socio " + socio.getDni() + ":");
            for (Factura f : facturasPendientes) {
                System.out.println("[DEBUG] " + f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facturasPendientes;
    }

    public void cancelarReserva(Reserva reserva) throws Exception {
        if (db == null || !db.isOpen()) open();
        db.getTransaction().begin();
        try {
            Reserva reservaActual = db.find(Reserva.class, reserva.getIdReserva());
            if (reservaActual == null) throw new Exception("Reserva no encontrada");
            Socio socio = reservaActual.getSocio();
            Sesion sesion = reservaActual.getSesion();
            if (socio != null) {
                socio = db.merge(socio);
                socio.removeReserva(reservaActual);
                int num = socio.getNumReservas();
                if (num > 0) socio.setNumReservas(num - 1);
                db.merge(socio);
            }
            if (sesion != null) {
                sesion = db.merge(sesion);
                sesion.removeReserva(reservaActual);
                int plazas = sesion.getPlazasOcupadas();
                if (plazas > 0) sesion.setPlazasOcupadas(plazas - 1);
                db.merge(sesion);
            }
            db.remove(reservaActual);
            db.getTransaction().commit();
        } catch (Exception e) {
            if (db.getTransaction().isActive()) db.getTransaction().rollback();
            throw e;
        }
    }
}

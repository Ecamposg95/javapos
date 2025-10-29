package com.datax.datax_pos.config;

import com.datax.datax_pos.model.*;
import com.datax.datax_pos.model.enums.UserRole;
import com.datax.datax_pos.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * Este componente se ejecuta una vez al iniciar la aplicación.
 * Se usa para poblar la base de datos H2 en memoria con datos de prueba.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    // Inyección de todos los repositorios necesarios
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PriceListRepository priceListRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // *** NUEVA LÍNEA DE PRUEBA ***
        log.info("--- EJECUTANDO VERSIÓN CORREGIDA DEL DATALOADER ---");
        // *******************************

        log.info("Cargando datos de prueba...");

        try {
            // 1. Crear Sucursal
            Branch branch = new Branch();
            branch.setName("Sucursal Principal");
            // FIX: Capturar la entidad administrada (managed) devuelta por .save()
            Branch savedBranch = branchRepository.save(branch);
            log.info("Sucursal creada: {}", savedBranch.getName());

            // 2. Crear un Cajero (para asociar ventas futuras)
            User cashier = new User();
            cashier.setName("Cajero 01");
            cashier.setPinHash("1234"); // En un futuro, esto debería ser un hash
            cashier.setRole(UserRole.CASHIER); // Esto ya funciona gracias a tu arreglo
            // FIX: Usar la 'savedBranch' administrada
            cashier.setBranch(savedBranch);
            // FIX 2: Capturar el usuario guardado para posible uso futuro
            User savedCashier = userRepository.save(cashier);
            log.info("Usuario Cajero creado: {}", savedCashier.getName());

            // 3. Crear Departamento
            Department hardware = new Department();
            hardware.setName("Ferretería");
            // FIX: Capturar la entidad administrada
            Department savedHardware = departmentRepository.save(hardware);
            log.info("Departamento creado: {}", savedHardware.getName());

            // 4. Crear Listas de Precios
            PriceList retail = new PriceList();
            retail.setName("Menudeo");
            // FIX: Capturar la entidad administrada
            PriceList savedRetail = priceListRepository.save(retail);

            PriceList wholesale = new PriceList();
            wholesale.setName("Mayoreo");
            // FIX: Capturar la entidad administrada
            PriceList savedWholesale = priceListRepository.save(wholesale);
            log.info("Listas de precios creadas: {} y {}", savedRetail.getName(), savedWholesale.getName());

            // 5. Crear Producto 1: Tornillo
            Product screw = new Product();
            screw.setName("Tornillo 1/4");
            screw.setSku("SCRW-14");
            screw.setBarcode("1234567890123");
            screw.setDescription("Tornillo de cabeza plana 1/4");
            // FIX: Usar el 'savedHardware' administrado
            screw.setDepartment(savedHardware);
            screw.setUnit("Pza");
            screw.setCost(new BigDecimal("0.50"));
            screw.setUsesInventory(true);
            screw.setMinStock(new BigDecimal("100"));
            screw.setMaxStock(new BigDecimal("1000"));
            screw.setImageUrl(null); // Sin imagen por ahora
            // FIX: Capturar el producto guardado
            Product savedScrew = productRepository.save(screw);
            log.info("Producto creado: {}", savedScrew.getName());

            // 6. Crear Producto 2: Martillo
            Product hammer = new Product();
            hammer.setName("Martillo de Uña");
            hammer.setSku("HAMR-CLAW");
            hammer.setBarcode("9876543210987");
            hammer.setDescription("Martillo de uña con mango de madera");
            // FIX: Usar el 'savedHardware' administrado
            hammer.setDepartment(savedHardware);
            hammer.setUnit("Pza");
            hammer.setCost(new BigDecimal("80.00"));
            hammer.setUsesInventory(true);
            hammer.setMinStock(new BigDecimal("10"));
            hammer.setMaxStock(new BigDecimal("50"));
            hammer.setImageUrl(null);
            // FIX: Capturar el producto guardado
            Product savedHammer = productRepository.save(hammer);
            log.info("Producto creado: {}", savedHammer.getName());

            // 7. Asignar Precios
            // Precio Menudeo Tornillo (desde 1 pieza)
            ProductPrice screwP1 = new ProductPrice();
            // FIX: Usar las entidades guardadas
            screwP1.setProduct(savedScrew);
            screwP1.setPriceList(savedRetail);
            screwP1.setMinQuantity(BigDecimal.ONE); // Cantidad mínima de 1
            screwP1.setUnitPrice(new BigDecimal("1.50"));
            productPriceRepository.save(screwP1);

            // Precio Mayoreo Tornillo (desde 100 piezas)
            ProductPrice screwP2 = new ProductPrice();
            // FIX: Usar las entidades guardadas
            screwP2.setProduct(savedScrew);
            screwP2.setPriceList(savedWholesale);
            screwP2.setMinQuantity(new BigDecimal("100"));
            screwP2.setUnitPrice(new BigDecimal("1.00"));
            productPriceRepository.save(screwP2);

            // Precio Menudeo Martillo (desde 1 pieza)
            ProductPrice hammerP1 = new ProductPrice();
            // FIX: Usar las entidades guardadas
            hammerP1.setProduct(savedHammer);
            hammerP1.setPriceList(savedRetail);
            hammerP1.setMinQuantity(BigDecimal.ONE);
            hammerP1.setUnitPrice(new BigDecimal("150.00"));
            productPriceRepository.save(hammerP1);
            log.info("Precios asignados a los productos.");

            log.info("¡Datos de prueba cargados exitosamente!");

        } catch (Exception e) {
            log.error("Error al cargar los datos de prueba", e);
        }
    }
}

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class DoraProjectGenerator {
    private static final String BASE_PACKAGE = "com.example.dorametrics";
    private static final String BASE_PATH = "src/main/java/" + BASE_PACKAGE.replace(".", "/");

    public static void main(String[] args) {
        try {
            String projectRoot = args.length > 0 ? args[0] : ".";
            generateProjectStructure(projectRoot);
            System.out.println("¡Estructura del proyecto generada exitosamente!");
        } catch (IOException e) {
            System.err.println("Error generando la estructura del proyecto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void generateProjectStructure(String projectRoot) throws IOException {
        // Crear directorios principales
        createDirectories(projectRoot,
                BASE_PATH,
                BASE_PATH + "/config",
                BASE_PATH + "/model/deployment",
                BASE_PATH + "/model/incident",
                BASE_PATH + "/model/changerequest",
                BASE_PATH + "/repository",
                BASE_PATH + "/service",
                BASE_PATH + "/controller",
                BASE_PATH + "/metrics/collector",
                BASE_PATH + "/metrics/service"
        );

        // Crear archivos Java
        createJavaFiles(projectRoot);
    }

    private static void createDirectories(String projectRoot, String... paths) throws IOException {
        for (String path : paths) {
            Files.createDirectories(Paths.get(projectRoot, path));
            System.out.println("Creado directorio: " + path);
        }
    }

    private static void createJavaFiles(String projectRoot) throws IOException {
        // Enums
        createEnum(projectRoot, "model/deployment", "DeploymentStatus",
                "SUCCESS", "FAILED", "IN_PROGRESS");
        createEnum(projectRoot, "model/incident", "IncidentStatus",
                "OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED");
        createEnum(projectRoot, "model/changerequest", "ChangeRequestStatus",
                "PENDING", "APPROVED", "REJECTED", "IMPLEMENTED");

        // Entidades
        createDeploymentEntity(projectRoot);
        createIncidentEntity(projectRoot);
        createChangeRequestEntity(projectRoot);

        // Repositorios
        createRepository(projectRoot, "Deployment");
        createRepository(projectRoot, "Incident");
        createRepository(projectRoot, "ChangeRequest");

        // Servicios
        createService(projectRoot, "Deployment");
        createService(projectRoot, "Incident");
        createService(projectRoot, "ChangeRequest");

        // Controladores
        createController(projectRoot, "Deployment");
        createController(projectRoot, "Incident");
        createController(projectRoot, "ChangeRequest");

        // Clase principal
        createMainClass(projectRoot);
    }

    private static void createEnum(String projectRoot, String path, String enumName, String... values) throws IOException {
        String content = String.format("""
            package %s.%s;
                        
            public enum %s {
                %s
            }
            """, BASE_PACKAGE, path.replace("/", "."), enumName, String.join(", ", values));

        writeFile(projectRoot, BASE_PATH + "/" + path + "/" + enumName + ".java", content);
    }

    private static void createDeploymentEntity(String projectRoot) throws IOException {
        String content = String.format("""
            package %s.model.deployment;
                        
            import jakarta.persistence.*;
            import lombok.Data;
            import java.time.LocalDateTime;
                        
            @Entity
            @Data
            @Table(name = "deployments")
            public class Deployment {
                @Id
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                private Long id;
                
                @Column(nullable = false)
                private LocalDateTime startTime;
                
                @Column(nullable = false)
                private LocalDateTime endTime;
                
                @Enumerated(EnumType.STRING)
                @Column(nullable = false)
                private DeploymentStatus status;
                
                @Column
                private String version;
                
                @Column
                private String commitId;
                
                @Column
                private String environment;
            }
            """, BASE_PACKAGE);

        writeFile(projectRoot, BASE_PATH + "/model/deployment/Deployment.java", content);
    }

    private static void createIncidentEntity(String projectRoot) throws IOException {
        // Similar al Deployment pero para Incident
        // Implementación similar...
    }

    private static void createChangeRequestEntity(String projectRoot) throws IOException {
        // Similar al Deployment pero para ChangeRequest
        // Implementación similar...
    }

    private static void createRepository(String projectRoot, String entity) throws IOException {
        String content = String.format("""
            package %s.repository;
                        
            import %s.model.%s.%s;
            import org.springframework.data.jpa.repository.JpaRepository;
            import org.springframework.stereotype.Repository;
                        
            @Repository
            public interface %sRepository extends JpaRepository<%s, Long> {
            }
            """, BASE_PACKAGE, BASE_PACKAGE, entity.toLowerCase(), entity, entity, entity);

        writeFile(projectRoot, BASE_PATH + "/repository/" + entity + "Repository.java", content);
    }

    private static void createService(String projectRoot, String entity) throws IOException {
        String content = String.format("""
            package %s.service;
                        
            import %s.model.%s.%s;
            import %s.repository.%sRepository;
            import lombok.RequiredArgsConstructor;
            import org.springframework.stereotype.Service;
                        
            @Service
            @RequiredArgsConstructor
            public class %sService {
                private final %sRepository repository;
                
                // Implementar métodos del servicio
            }
            """, BASE_PACKAGE, BASE_PACKAGE, entity.toLowerCase(), entity,
                BASE_PACKAGE, entity, entity, entity);

        writeFile(projectRoot, BASE_PATH + "/service/" + entity + "Service.java", content);
    }

    private static void createController(String projectRoot, String entity) throws IOException {
        String content = String.format("""
            package %s.controller;
                        
            import %s.model.%s.%s;
            import %s.service.%sService;
            import lombok.RequiredArgsConstructor;
            import org.springframework.web.bind.annotation.RequestMapping;
            import org.springframework.web.bind.annotation.RestController;
                        
            @RestController
            @RequestMapping("/api/%s")
            @RequiredArgsConstructor
            public class %sController {
                private final %sService service;
                
                // Implementar endpoints
            }
            """, BASE_PACKAGE, BASE_PACKAGE, entity.toLowerCase(), entity,
                BASE_PACKAGE, entity, entity.toLowerCase(), entity, entity);

        writeFile(projectRoot, BASE_PATH + "/controller/" + entity + "Controller.java", content);
    }

    private static void createMainClass(String projectRoot) throws IOException {
        String content = String.format("""
            package %s;
                        
            import org.springframework.boot.SpringApplication;
            import org.springframework.boot.autoconfigure.SpringBootApplication;
                        
            @SpringBootApplication
            public class DoraMetricsApplication {
                public static void main(String[] args) {
                    SpringApplication.run(DoraMetricsApplication.class, args);
                }
            }
            """, BASE_PACKAGE);

        writeFile(projectRoot, BASE_PATH + "/DoraMetricsApplication.java", content);
    }

    private static void writeFile(String projectRoot, String path, String content) throws IOException {
        File file = new File(projectRoot, path);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        System.out.println("Creado archivo: " + path);
    }
}
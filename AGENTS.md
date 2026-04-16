# Microservice Domain Blueprint (CodeXP Standard)

## 1. Objetivo

Este documento define como esta implementado el microservicio de challenges y como repetir el mismo enfoque para cualquier otro dominio (por ejemplo: courses, submissions, badges, rankings).

El enfoque combina:
- Arquitectura por capas
- DDD tactico (Entity + Value Objects)
- CQRS liviano (commands y queries separados)
- Patron Assembler para aislamiento entre API y dominio
- Seguridad stateless con JWT
- Manejo consistente de errores HTTP

## 2. Principios de arquitectura

### 2.1 Separacion por capas

Estructura base usada:
- src/main/java/com/codexp/challenges/<bounded-context>/domain
- src/main/java/com/codexp/challenges/<bounded-context>/application
- src/main/java/com/codexp/challenges/<bounded-context>/infrastructure
- src/main/java/com/codexp/challenges/<bounded-context>/interfaces
- src/main/java/com/codexp/challenges/shared

Responsabilidades:
- domain: modelo de negocio, comandos, queries, value objects, contratos de servicios
- application: implementacion de casos de uso (command services y query services)
- infrastructure: adaptadores tecnicos (JPA repositories, security config, etc.)
- interfaces: controladores REST, requests/responses, assemblers
- shared: capacidades transversales (security, exceptions, user context, abstract entity)

### 2.2 Dependencias

Regla usada:
- interfaces -> domain services (interfaces), assemblers
- application -> domain + infrastructure repositories
- domain -> no depende de controllers ni de infraestructura concreta
- shared -> reusable por todos los bounded contexts

### 2.3 CQRS liviano

Lectura y escritura estan separadas:
- command services para cambios de estado
- query services para consultas

No se usa event sourcing ni buses complejos para todas las operaciones; se mantiene simple para servicios CRUD+reglas.

## 3. Estandares de modelado de dominio

## 3.1 Entidades y aggregate root

Patron actual en entidades:
- Clase entidad con @Entity
- Constructor vacio con @NoArgsConstructor
- Creacion por fabrica estatica create(...)
- Metodos de comportamiento del agregado (ejemplo: publish, updatePartially)
- Herencia de AbstractEntity para auditoria tecnica

Estado actual:
- Challenge (aggregate root)
- CodeTemplate (entidad hija por challengeId)
- TestCase (entidad hija por challengeId)

## 3.2 Value Objects

Estandar actual:
- Se implementan como record
- Se anotan con @Embeddable
- Exponen fabrica estatica para validacion (fromString, fromInt, fromBoolean)
- Encapsulan invariantes de negocio y formato

Ejemplos de invariantes:
- ChallengeTitle: longitud minima y maxima
- ChallengeDescription: longitud minima y maxima
- EntryFunctionName: no espacios, largo valido
- TemplateLanguage: normalizacion lowercase y max length
- IDs: parseo UUID y validacion de formato

## 3.3 IDs UUID sobre Long

Politica recomendada (y aplicada):
- Todos los IDs de dominio son UUID envueltos en Value Objects
- No usar Long autoincremental como identidad de dominio

Ventajas:
- Unicidad global entre microservicios
- Menor acoplamiento al datastore
- Mejor interoperabilidad entre servicios/eventos
- Facilita integracion externa sin exponer secuenciales

Estandar:
- ChallengeId.generate()
- CodeTemplateId.generate()
- TestCaseId.generate()
- fromString(...) para parseo seguro

## 3.4 Asociacion Value Objects en entidades JPA

Patron de mapeo usado:
- ID principal: @EmbeddedId + @AttributeOverride(name = "value", column = ...)
- Campos VO: @Embedded + @AttributeOverride(name = "value", column = ...)

Esto evita tipos primitivos dispersos y mantiene persistencia alineada al modelo de dominio.

## 4. Patron Assembler

Objetivo del Assembler:
- Evitar que controllers conozcan el dominio interno
- Evitar mapear manualmente en cada endpoint
- Mantener consistencia de conversiones

Tipos de assembler usados:
- CommandAssembler: Request -> Command
- QueryAssembler: parametros REST -> Query
- EntityAssembler: Entity -> Response

Flujo tipico:
1) Controller recibe request JSON
2) CommandAssembler construye comando con VO
3) Service procesa comando
4) QueryAssembler construye query (cuando aplica)
5) QueryService devuelve entidad
6) EntityAssembler convierte a response

Regla:
- Nunca mapear String directo a Entity dentro del controller
- Toda conversion a VO ocurre en assembler

## 5. Contratos por interfaces

Patron actual:
- En domain/services se definen interfaces de casos de uso
- En application/*services se implementan
- Controllers dependen de interfaces, no de implementaciones

Beneficios:
- Bajo acoplamiento
- Mejor testeabilidad
- Sustitucion de implementaciones sin romper API

## 6. Reglas de negocio en application services

Los command services contienen:
- Autorizacion por rol
- Validacion de ownership
- Validacion de precondiciones de negocio
- Operaciones transaccionales cuando corresponde

Ejemplo real implementado:
- Publish challenge valida:
  - rol docente
  - owner del challenge
  - existencia de al menos un code template
  - entryFunctionName valido en templates
  - existencia de al menos un test case

## 7. Persistencia y repositorios

Patron actual:
- Spring Data JPA repository por agregado/entidad
- IDs tipados con Value Objects
- Metodos derivados para queries simples

Ejemplos:
- paginacion por titulo: findByTitle_ValueContainingIgnoreCase(..., Pageable)
- borrado por relacion: deleteByChallengeId(...)

Nota de integridad:
- El borrado de challenge elimina test cases y code templates asociados en service transaccional.

## 8. API REST y separacion de responsabilidades

Controller hace:
- Orquestacion HTTP
- Lectura de principal por UserContext
- Llamado a assemblers
- Seleccion de status code

Controller no hace:
- Reglas de dominio profundas
- Validaciones de invariantes complejas
- Llamadas directas a repositorios

## 9. UserContext y seguridad JWT (estandar reusable)

## 9.1 Configuracion de seguridad

Estandar actual:
- SecurityFilterChain stateless
- JwtAuthFilter en cadena antes de UsernamePasswordAuthenticationFilter
- Endpoints publicos limitados (actuator/docs)
- Resto autenticado

## 9.2 Contrato JWT comun para otros microservicios

Claims requeridos:
- sub: userId
- nickname
- email
- role

Normalizacion de rol:
- UserRole.fromClaim acepta ROLE_* o valor simple y normaliza a enum

Principal comun:
- JwtPrincipal(UserId, NickName, UserEmail, UserRole)

Regla para todos los microservicios:
- Reutilizar exactamente este contrato de claims y este parseo
- Evitar variantes de nombres de claim por servicio

## 9.3 Uso de UserContext

Patron:
- Controller llama userContext.getPrincipal()
- Se pasa userId/role a commands via assembler
- Application service decide autorizacion final

Beneficio:
- El acceso al SecurityContext queda centralizado y uniforme.

## 10. Manejo de errores y codigos HTTP

Estandar actual en GlobalExceptionHandler:
- 400: malformed JSON (HttpMessageNotReadableException)
- 403: UnauthorizedActionException
- 404: ChallengeNotFound / CodeTemplateNotFound / TestCaseNotFound
- 422: IllegalArgumentException (validacion o precondicion)

Payload de error estandar:
- code
- message
- timestamp

Regla para todos los contextos:
- Excepciones de dominio mapeadas a HTTP en handler global, no en cada controller.

## 11. AbstractEntity y auditoria tecnica

Clase base compartida:
- AbstractEntity (@MappedSuperclass)
- createdAt con @CreationTimestamp
- updatedAt con @UpdateTimestamp

Uso:
- Toda entidad JPA de dominio extiende AbstractEntity
- Responses exponen createdAt/updatedAt cuando corresponda

## 12. Convenciones de codigo recomendadas

1. DTOs REST como records (Request y Response)
2. Commands y Queries como records inmutables
3. Conversiones de tipos solo en assembler
4. Validaciones de formato en Value Objects
5. Reglas de autorizacion y ownership en command services
6. Controllers delgados, servicios ricos en reglas
7. Operaciones multi-entidad con @Transactional
8. Errores de validacion con IllegalArgumentException -> 422
9. IDs de dominio siempre UUID encapsulado
10. No exponer entidades JPA directamente al contrato REST

## 13. Blueprint repetible para un nuevo dominio

Secuencia recomendada para crear otro bounded context:

1) Definir entidades y value objects
- Crear IDs VO UUID y VOs de negocio con validacion
- Mapear en JPA con @Embedded/@EmbeddedId

2) Definir comandos y queries
- records por caso de uso
- contratos en domain/services

3) Implementar application services
- reglas de negocio
- autorizacion y ownership
- transacciones donde aplique

4) Crear repositorios JPA
- interfaces por agregado
- metodos derivados para consultas

5) Exponer interfaces REST
- requests/responses
- command/query/entity assemblers
- controllers delgados

6) Integrar seguridad comun
- usar UserContext + JWT claims estandar

7) Integrar manejo de errores
- mapear nuevas excepciones de dominio en GlobalExceptionHandler

8) Probar flujo completo
- test de integracion de caso feliz y precondiciones fallidas
- validar status code y estado persistido

## 14. Checklist de Definition of Done por dominio

- IDs VO UUID implementados y usados en entidades
- Value Objects validan invariantes por fabrica estatica
- Entity hereda AbstractEntity
- Commands/Queries definidos como records
- Service interfaces en domain y implementaciones en application
- Controllers usan assemblers (sin mapeo manual de dominio)
- UserContext usado para principal autenticado
- JWT claims estandar respetados
- Excepciones mapeadas en handler global
- Casos de integracion con status code esperado (200/201/204/403/404/422)
- Documentacion de endpoints y reglas de negocio actualizada

## 15. Recomendacion para estandar organizacional

Para escalar esto a multiples microservicios:
- Extraer modulo shared-security con JwtPrincipal, UserRole, JwtUtils base y UserContext
- Extraer modulo shared-error-handling con ErrorResponse y handler base
- Publicar una plantilla interna de bounded context con esta estructura de carpetas
- Automatizar validaciones arquitectonicas en CI (package boundaries, naming y convenciones)

Con esta base, el equipo puede replicar implementaciones consistentes de dominio sin perder reglas de negocio ni estandar de seguridad/error handling entre microservicios.
